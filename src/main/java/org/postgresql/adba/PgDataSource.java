/*
 * Copyright (c) 2018, PostgreSQL Global Development Group
 * See the LICENSE file in the project root for more information.
 */

package org.postgresql.adba;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import jdk.incubator.sql2.DataSource;
import jdk.incubator.sql2.Session;
import jdk.incubator.sql2.SessionProperty;
import org.postgresql.adba.buffer.ByteBufferPool;
import org.postgresql.adba.buffer.DefaultByteBufferPool;
import org.postgresql.adba.execution.DefaultNioLoop;
import org.postgresql.adba.execution.NioLoop;
import org.postgresql.adba.util.PropertyHolder;

public class PgDataSource implements DataSource {
  private final NioLoop loop;
  private final ByteBufferPool bufferPool;
  private Queue<PgSession> connections = new ConcurrentLinkedQueue<>();
  private boolean closed;
  private PropertyHolder properties;
  private DefaultNioLoop defaultLoop = null;

  /**
   * Creates a datasource that represent a set of connections to a postgresql database.
   *
   * @param properties the properties that govern the connections
   */
  public PgDataSource(PropertyHolder properties) {
    this.properties = properties;

    // Obtain the NIO loop
    NioLoop loop = (NioLoop) this.properties.get(PgDataSourceProperty.NIO_LOOP);
    if (loop == null) {
      // Provide default loop
      this.defaultLoop = new DefaultNioLoop();
      new Thread(this.defaultLoop).start();
      loop = defaultLoop;
    }
    this.loop = loop;
    
    // Obtain the byte buffer pool
    ByteBufferPool pool = (ByteBufferPool) this.properties.get(PgDataSourceProperty.BYTE_BUFFER_POOL);
    if (pool == null) {
      // Provide default pool
      pool = new DefaultByteBufferPool();
    }
    this.bufferPool = pool;
  }

  /**
   * Obtains the {@link NioLoop}.
   * 
   * @return {@link NioLoop}.
   */
  public NioLoop getNioLoop() {
    return this.loop;
  }
  
  /**
   * Obtains the {@link ByteBufferPool}.
   * 
   * @return {@link ByteBufferPool}.
   */
  public ByteBufferPool getByteBufferPool() {
    return this.bufferPool;
  }

  /**
   * Returns a {@link Session} builder. By default that builder will return
   * {@link Session}s with the {@code SessionProperty}s specified when
   * creating this DataSource. Default and unspecified {@link SessionProperty}s
   * can be set with the returned builder.
   *
   * @return a new {@link Session} builder. Not {@code null}.
   */
  @Override
  public Session.Builder builder() {
    if (closed) {
      throw new IllegalStateException("this datasource has already been closed");
    }

    return new PgSessionBuilder(this, new PropertyHolder(properties));
  }
  
  public void unregisterConnection(PgSession connection) {
    this.connections.remove(connection);
  }

  @Override
  public void close() {
    for (PgSession connection : connections) {
      connection.close();
    }
    if (this.defaultLoop != null) {
      this.defaultLoop.close();
    }
    closed = true;
  }

  public void registerConnection(PgSession connection) {
    connections.add(connection);
  }

  public boolean isClosed() {
    return closed;
  }
}
