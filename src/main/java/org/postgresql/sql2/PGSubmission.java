package org.postgresql.sql2;

import jdk.incubator.sql2.Result;
import jdk.incubator.sql2.SqlType;
import jdk.incubator.sql2.Submission;
import org.postgresql.sql2.communication.packets.DataRow;
import org.postgresql.sql2.operations.helpers.ParameterHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PGSubmission<T> implements Submission<T> {
  public enum Types {
    COUNT,
    ROW,
    CLOSE,
    TRANSACTION,
    ARRAY_COUNT,
    VOID,
    PROCESSOR,
    OUT_PARAMETER,
    LOCAL,
    GROUP;
  }

  final private Supplier<Boolean> cancel;
  private CompletableFuture<T> publicStage;
  private boolean connectionSubmission;
  private String sql;
  private final AtomicBoolean sendConsumed = new AtomicBoolean(false);
  private ParameterHolder holder;
  private Types completionType;

  private Collector collector;
  private Object collectorHolder;
  private SubmissionPublisher<Result.Row> publisher;
  private Consumer<Throwable> errorHandler;
  private Map<String, SqlType> outParameterTypeMap;
  private Function<Result.OutParameterMap, ? extends T> outParameterProcessor;
  private Callable<T> localAction;

  private List<Long> countResults = new ArrayList<>();
  private PGSubmission groupSubmission;
  private T outParameterValueHolder;

  public PGSubmission(Supplier<Boolean> cancel, Types completionType, Consumer<Throwable> errorHandler) {
    this.cancel = cancel;
    this.completionType = completionType;
    this.errorHandler = errorHandler;
  }

  @Override
  public CompletionStage<Boolean> cancel() {
    return new CompletableFuture<Boolean>().completeAsync(cancel);
  }

  @Override
  public CompletionStage<T> getCompletionStage() {
    if (publicStage == null)
      publicStage = new CompletableFuture<>();
    return publicStage;
  }

  public boolean isConnectionSubmission() {
    return connectionSubmission;
  }

  public void setConnectionSubmission(boolean connectionSubmission) {
    this.connectionSubmission = connectionSubmission;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public String getSql() {
    return sql;
  }

  public AtomicBoolean getSendConsumed() {
    return sendConsumed;
  }

  public void setHolder(ParameterHolder holder) {
    this.holder = holder;
  }

  public ParameterHolder getHolder() {
    return holder;
  }

  public Types getCompletionType() {
    return completionType;
  }

  public void setCollector(Collector collector) {
    this.collector = collector;

    collectorHolder = collector.supplier().get();
  }

  public void setPublisher(SubmissionPublisher<Result.Row> publisher) {
    this.publisher = publisher;
  }

  public SubmissionPublisher<Result.Row> getPublisher() {
    return publisher;
  }

  public Object finish() {
    Object o = null;
    if(collector != null) {
      o = collector.finisher().apply(collectorHolder);
      if(groupSubmission != null) {
        groupSubmission.addGroupResult(o);
      }
    }
    if(publisher != null) {
      publisher.close();
    }
    return o;
  }

  public void addRow(DataRow row) {
    try {
      collector.accumulator().accept(collectorHolder, row);
    } catch (Throwable e) {
      publicStage.completeExceptionally(e);
    }
  }

  public void addGroupResult(Object result) {
    try {
      collector.accumulator().accept(collectorHolder, result);
    } catch (Throwable e) {
      publicStage.completeExceptionally(e);
    }
  }

  public void processRow(DataRow row) {
    publisher.offer(row, (subscriber, rowItem) -> {
      subscriber.onError(new IllegalStateException("failed to offer item to subscriber"));
      return false;
    });
  }

  public void applyOutRow(DataRow row) {
    outParameterValueHolder = outParameterProcessor.apply(row);
  }

  public List<Integer> getParamTypes() throws ExecutionException, InterruptedException {
    return holder.getParamTypes();
  }

  public int numberOfQueryRepetitions() throws ExecutionException, InterruptedException {
    if (completionType == Types.ARRAY_COUNT) {
      return holder.numberOfQueryRepetitions();
    } else {
      return 1;
    }
  }

  public List<Long> countResult() {
    return countResults;
  }

  public void setOutParameterTypeMap(Map<String, SqlType> outParameterTypeMap) {
    this.outParameterTypeMap = outParameterTypeMap;
  }

  public Map<String, SqlType> getOutParameterTypeMap() {
    return outParameterTypeMap;
  }

  public void setOutParameterProcessor(Function<Result.OutParameterMap, ? extends T> outParameterProcessor) {
    this.outParameterProcessor = outParameterProcessor;
  }

  public void setLocalAction(Callable<T> localAction) {
    this.localAction = localAction;
  }

  public Callable<T> getLocalAction() {
    return localAction;
  }

  public Function<Result.OutParameterMap, ? extends T> getOutParameterProcessor() {
    return outParameterProcessor;
  }

  public Consumer<Throwable> getErrorHandler() {
    return errorHandler;
  }

  public void addGroupSubmission(PGSubmission groupSubmission) {
    this.groupSubmission = groupSubmission;
  }

  public PGSubmission getGroupSubmission() {
    return groupSubmission;
  }

  public T getOutParameterValueHolder() {
    return outParameterValueHolder;
  }
}
