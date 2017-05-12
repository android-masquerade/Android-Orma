package com.github.gfx.android.orma.example.orma;

import android.support.annotation.NonNull;
import com.github.gfx.android.orma.BuiltInSerializers;
import com.github.gfx.android.orma.Deleter;
import com.github.gfx.android.orma.OrmaConnection;
import com.github.gfx.android.orma.function.Function1;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class Todo_Deleter extends Deleter<Todo, Todo_Deleter> {
  final Todo_Schema schema;

  public Todo_Deleter(OrmaConnection conn, Todo_Schema schema) {
    super(conn);
    this.schema = schema;
  }

  public Todo_Deleter(Todo_Deleter that) {
    super(that);
    this.schema = that.getSchema();
  }

  public Todo_Deleter(Todo_Relation relation) {
    super(relation);
    this.schema = relation.getSchema();
  }

  @Override
  public Todo_Deleter clone() {
    return new Todo_Deleter(this);
  }

  @NonNull
  @Override
  public Todo_Schema getSchema() {
    return schema;
  }

  public Todo_Deleter titleEq(@NonNull String title) {
    return where(schema.title, "=", title);
  }

  public Todo_Deleter titleNotEq(@NonNull String title) {
    return where(schema.title, "<>", title);
  }

  public Todo_Deleter titleIn(@NonNull Collection<String> values) {
    return in(false, schema.title, values);
  }

  public Todo_Deleter titleNotIn(@NonNull Collection<String> values) {
    return in(true, schema.title, values);
  }

  public final Todo_Deleter titleIn(@NonNull String... values) {
    return titleIn(Arrays.asList(values));
  }

  public final Todo_Deleter titleNotIn(@NonNull String... values) {
    return titleNotIn(Arrays.asList(values));
  }

  public Todo_Deleter titleLt(@NonNull String title) {
    return where(schema.title, "<", title);
  }

  public Todo_Deleter titleLe(@NonNull String title) {
    return where(schema.title, "<=", title);
  }

  public Todo_Deleter titleGt(@NonNull String title) {
    return where(schema.title, ">", title);
  }

  public Todo_Deleter titleGe(@NonNull String title) {
    return where(schema.title, ">=", title);
  }

  @Deprecated
  public Todo_Deleter contentIsNull() {
    return where(schema.content, " IS NULL");
  }

  @Deprecated
  public Todo_Deleter contentIsNotNull() {
    return where(schema.content, " IS NOT NULL");
  }

  @Deprecated
  public Todo_Deleter contentEq(@NonNull String content) {
    return where(schema.content, "=", content);
  }

  @Deprecated
  public Todo_Deleter contentNotEq(@NonNull String content) {
    return where(schema.content, "<>", content);
  }

  @Deprecated
  public Todo_Deleter contentIn(@NonNull Collection<String> values) {
    return in(false, schema.content, values);
  }

  @Deprecated
  public Todo_Deleter contentNotIn(@NonNull Collection<String> values) {
    return in(true, schema.content, values);
  }

  @Deprecated
  public final Todo_Deleter contentIn(@NonNull String... values) {
    return contentIn(Arrays.asList(values));
  }

  @Deprecated
  public final Todo_Deleter contentNotIn(@NonNull String... values) {
    return contentNotIn(Arrays.asList(values));
  }

  @Deprecated
  public Todo_Deleter contentLt(@NonNull String content) {
    return where(schema.content, "<", content);
  }

  @Deprecated
  public Todo_Deleter contentLe(@NonNull String content) {
    return where(schema.content, "<=", content);
  }

  @Deprecated
  public Todo_Deleter contentGt(@NonNull String content) {
    return where(schema.content, ">", content);
  }

  @Deprecated
  public Todo_Deleter contentGe(@NonNull String content) {
    return where(schema.content, ">=", content);
  }

  public Todo_Deleter doneEq(boolean done) {
    return where(schema.done, "=", done);
  }

  public Todo_Deleter doneNotEq(boolean done) {
    return where(schema.done, "<>", done);
  }

  public Todo_Deleter doneIn(@NonNull Collection<Boolean> values) {
    return in(false, schema.done, values);
  }

  public Todo_Deleter doneNotIn(@NonNull Collection<Boolean> values) {
    return in(true, schema.done, values);
  }

  public final Todo_Deleter doneIn(@NonNull Boolean... values) {
    return doneIn(Arrays.asList(values));
  }

  public final Todo_Deleter doneNotIn(@NonNull Boolean... values) {
    return doneNotIn(Arrays.asList(values));
  }

  public Todo_Deleter doneLt(boolean done) {
    return where(schema.done, "<", done);
  }

  public Todo_Deleter doneLe(boolean done) {
    return where(schema.done, "<=", done);
  }

  public Todo_Deleter doneGt(boolean done) {
    return where(schema.done, ">", done);
  }

  public Todo_Deleter doneGe(boolean done) {
    return where(schema.done, ">=", done);
  }

  @Deprecated
  public Todo_Deleter createdTimeEq(@NonNull Date createdTime) {
    return where(schema.createdTime, "=", BuiltInSerializers.serializeDate(createdTime));
  }

  @Deprecated
  public Todo_Deleter createdTimeNotEq(@NonNull Date createdTime) {
    return where(schema.createdTime, "<>", BuiltInSerializers.serializeDate(createdTime));
  }

  @Deprecated
  public Todo_Deleter createdTimeIn(@NonNull Collection<Date> values) {
    return in(false, schema.createdTime, values, new Function1<Date, Long>() {
      @Override
      public Long apply(Date value) {
        return BuiltInSerializers.serializeDate(value);
      }
    });
  }

  @Deprecated
  public Todo_Deleter createdTimeNotIn(@NonNull Collection<Date> values) {
    return in(true, schema.createdTime, values, new Function1<Date, Long>() {
      @Override
      public Long apply(Date value) {
        return BuiltInSerializers.serializeDate(value);
      }
    });
  }

  @Deprecated
  public final Todo_Deleter createdTimeIn(@NonNull Date... values) {
    return createdTimeIn(Arrays.asList(values));
  }

  @Deprecated
  public final Todo_Deleter createdTimeNotIn(@NonNull Date... values) {
    return createdTimeNotIn(Arrays.asList(values));
  }

  @Deprecated
  public Todo_Deleter createdTimeLt(@NonNull Date createdTime) {
    return where(schema.createdTime, "<", BuiltInSerializers.serializeDate(createdTime));
  }

  @Deprecated
  public Todo_Deleter createdTimeLe(@NonNull Date createdTime) {
    return where(schema.createdTime, "<=", BuiltInSerializers.serializeDate(createdTime));
  }

  @Deprecated
  public Todo_Deleter createdTimeGt(@NonNull Date createdTime) {
    return where(schema.createdTime, ">", BuiltInSerializers.serializeDate(createdTime));
  }

  @Deprecated
  public Todo_Deleter createdTimeGe(@NonNull Date createdTime) {
    return where(schema.createdTime, ">=", BuiltInSerializers.serializeDate(createdTime));
  }

  /**
   * To build a condition <code>createdTime BETWEEN a AND b</code>, which is equivalent to <code>a <= createdTime AND createdTime <= b</code>.
   */
  @Deprecated
  public Todo_Deleter createdTimeBetween(@NonNull Date createdTimeA, @NonNull Date createdTimeB) {
    return whereBetween(schema.createdTime, BuiltInSerializers.serializeDate(createdTimeA), BuiltInSerializers.serializeDate(createdTimeB));
  }

  public Todo_Deleter idEq(long id) {
    return where(schema.id, "=", id);
  }

  public Todo_Deleter idNotEq(long id) {
    return where(schema.id, "<>", id);
  }

  public Todo_Deleter idIn(@NonNull Collection<Long> values) {
    return in(false, schema.id, values);
  }

  public Todo_Deleter idNotIn(@NonNull Collection<Long> values) {
    return in(true, schema.id, values);
  }

  public final Todo_Deleter idIn(@NonNull Long... values) {
    return idIn(Arrays.asList(values));
  }

  public final Todo_Deleter idNotIn(@NonNull Long... values) {
    return idNotIn(Arrays.asList(values));
  }

  public Todo_Deleter idLt(long id) {
    return where(schema.id, "<", id);
  }

  public Todo_Deleter idLe(long id) {
    return where(schema.id, "<=", id);
  }

  public Todo_Deleter idGt(long id) {
    return where(schema.id, ">", id);
  }

  public Todo_Deleter idGe(long id) {
    return where(schema.id, ">=", id);
  }

  /**
   * To build a condition <code>id BETWEEN a AND b</code>, which is equivalent to <code>a <= id AND id <= b</code>.
   */
  public Todo_Deleter idBetween(long idA, long idB) {
    return whereBetween(schema.id, idA, idB);
  }
}
