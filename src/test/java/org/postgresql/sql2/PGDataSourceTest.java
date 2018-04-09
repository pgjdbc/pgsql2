package org.postgresql.sql2;

import static org.junit.Assert.*;

import org.junit.Test;

import java2.sql2.Connection;
import java2.sql2.DataSource;
import java2.sql2.DataSourceFactory;
import java2.sql2.JdbcConnectionProperty;

public class PGDataSourceTest {

  @Test
  public void builder() throws Exception {
    Class.forName("org.postgresql.sql2.PGDataSourceFactory", true, ClassLoader.getSystemClassLoader());
    DataSource ds = DataSourceFactory.forName("Postgres Database")
        .builder()
        .url("jdbc:postgresql://localhost/test")
        .username("test")
        .password("test")
        .connectionProperty(JdbcConnectionProperty.TRANSACTION_ISOLATION,
            JdbcConnectionProperty.TransactionIsolation.REPEATABLE_READ)
        .build();
    Connection con = ds.getConnection();
    //con.connect();
    Thread.sleep(10000);
  }

  @Test
  public void close() {
  }
}