package com.github.gfx.android.orma.example.orma;

import android.content.ContentValues;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.github.gfx.android.orma.OrmaConnection;
import com.github.gfx.android.orma.Relation;
import com.github.gfx.android.orma.annotation.OnConflict;
import com.github.gfx.android.orma.example.tool.TypeAdapters;
import com.github.gfx.android.orma.function.Function1;
import java.util.Arrays;
import java.util.Collection;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZonedDateTime;

public class Item2_Relation extends Relation<Item2, Item2_Relation> {
  final Item2_Schema schema;

  public Item2_Relation(OrmaConnection conn, Item2_Schema schema) {
    super(conn);
    this.schema = schema;
  }

  public Item2_Relation(Item2_Relation that) {
    super(that);
    this.schema = that.getSchema();
  }

  @Override
  public Item2_Relation clone() {
    return new Item2_Relation(this);
  }

  @NonNull
  @Override
  public Item2_Schema getSchema() {
    return schema;
  }

  @NonNull
  @CheckResult
  public Item2 reload(@NonNull Item2 model) {
    return selector().nameEq(model.name).value();
  }

  @NonNull
  @Override
  public Item2 upsertWithoutTransaction(@NonNull Item2 model) {
    ContentValues contentValues = new ContentValues();
    contentValues.put("`category1`", new Category_Relation(conn, Category_Schema.INSTANCE).upsertWithoutTransaction(model.category1).id);
    contentValues.put("`category2`", model.category2 != null ? new Category_Relation(conn, Category_Schema.INSTANCE).upsertWithoutTransaction(model.category2).id : null);
    contentValues.put("`zonedTimestamp`", TypeAdapters.serializeZonedDateTime(model.zonedTimestamp));
    contentValues.put("`localDateTime`", TypeAdapters.serializeLocalDateTime(model.localDateTime));
    contentValues.put("`name`", model.name);
    int updatedRows = updater().nameEq(model.name).putAll(contentValues).execute();
    if (updatedRows != 0) {
      return selector().nameEq(model.name).value();
    }
    long rowId = conn.insert(schema, contentValues, OnConflict.NONE);
    return conn.findByRowId(schema, rowId);
  }

  @NonNull
  @Override
  public Item2_Selector selector() {
    return new Item2_Selector(this);
  }

  @NonNull
  @Override
  public Item2_Updater updater() {
    return new Item2_Updater(this);
  }

  @NonNull
  @Override
  public Item2_Deleter deleter() {
    return new Item2_Deleter(this);
  }

  public Item2_Relation category1Eq(@NonNull Category category1) {
    return where(schema.category1, "=", category1.id);
  }

  public Item2_Relation category1Eq(long category1Id) {
    return where(schema.category1, "=", category1Id);
  }

  public Item2_Relation category2IsNull() {
    return where(schema.category2, " IS NULL");
  }

  public Item2_Relation category2IsNotNull() {
    return where(schema.category2, " IS NOT NULL");
  }

  public Item2_Relation category2Eq(@NonNull Category category2) {
    return where(schema.category2, "=", category2.id);
  }

  public Item2_Relation category2Eq(long category2Id) {
    return where(schema.category2, "=", category2Id);
  }

  @Deprecated
  public Item2_Relation zonedTimestampEq(@NonNull ZonedDateTime zonedTimestamp) {
    return where(schema.zonedTimestamp, "=", TypeAdapters.serializeZonedDateTime(zonedTimestamp));
  }

  @Deprecated
  public Item2_Relation zonedTimestampNotEq(@NonNull ZonedDateTime zonedTimestamp) {
    return where(schema.zonedTimestamp, "<>", TypeAdapters.serializeZonedDateTime(zonedTimestamp));
  }

  @Deprecated
  public Item2_Relation zonedTimestampIn(@NonNull Collection<ZonedDateTime> values) {
    return in(false, schema.zonedTimestamp, values, new Function1<ZonedDateTime, String>() {
      @Override
      public String apply(ZonedDateTime value) {
        return TypeAdapters.serializeZonedDateTime(value);
      }
    });
  }

  @Deprecated
  public Item2_Relation zonedTimestampNotIn(@NonNull Collection<ZonedDateTime> values) {
    return in(true, schema.zonedTimestamp, values, new Function1<ZonedDateTime, String>() {
      @Override
      public String apply(ZonedDateTime value) {
        return TypeAdapters.serializeZonedDateTime(value);
      }
    });
  }

  @Deprecated
  public final Item2_Relation zonedTimestampIn(@NonNull ZonedDateTime... values) {
    return zonedTimestampIn(Arrays.asList(values));
  }

  @Deprecated
  public final Item2_Relation zonedTimestampNotIn(@NonNull ZonedDateTime... values) {
    return zonedTimestampNotIn(Arrays.asList(values));
  }

  @Deprecated
  public Item2_Relation zonedTimestampLt(@NonNull ZonedDateTime zonedTimestamp) {
    return where(schema.zonedTimestamp, "<", TypeAdapters.serializeZonedDateTime(zonedTimestamp));
  }

  @Deprecated
  public Item2_Relation zonedTimestampLe(@NonNull ZonedDateTime zonedTimestamp) {
    return where(schema.zonedTimestamp, "<=", TypeAdapters.serializeZonedDateTime(zonedTimestamp));
  }

  @Deprecated
  public Item2_Relation zonedTimestampGt(@NonNull ZonedDateTime zonedTimestamp) {
    return where(schema.zonedTimestamp, ">", TypeAdapters.serializeZonedDateTime(zonedTimestamp));
  }

  @Deprecated
  public Item2_Relation zonedTimestampGe(@NonNull ZonedDateTime zonedTimestamp) {
    return where(schema.zonedTimestamp, ">=", TypeAdapters.serializeZonedDateTime(zonedTimestamp));
  }

  @Deprecated
  public Item2_Relation localDateTimeEq(@NonNull LocalDateTime localDateTime) {
    return where(schema.localDateTime, "=", TypeAdapters.serializeLocalDateTime(localDateTime));
  }

  @Deprecated
  public Item2_Relation localDateTimeNotEq(@NonNull LocalDateTime localDateTime) {
    return where(schema.localDateTime, "<>", TypeAdapters.serializeLocalDateTime(localDateTime));
  }

  @Deprecated
  public Item2_Relation localDateTimeIn(@NonNull Collection<LocalDateTime> values) {
    return in(false, schema.localDateTime, values, new Function1<LocalDateTime, String>() {
      @Override
      public String apply(LocalDateTime value) {
        return TypeAdapters.serializeLocalDateTime(value);
      }
    });
  }

  @Deprecated
  public Item2_Relation localDateTimeNotIn(@NonNull Collection<LocalDateTime> values) {
    return in(true, schema.localDateTime, values, new Function1<LocalDateTime, String>() {
      @Override
      public String apply(LocalDateTime value) {
        return TypeAdapters.serializeLocalDateTime(value);
      }
    });
  }

  @Deprecated
  public final Item2_Relation localDateTimeIn(@NonNull LocalDateTime... values) {
    return localDateTimeIn(Arrays.asList(values));
  }

  @Deprecated
  public final Item2_Relation localDateTimeNotIn(@NonNull LocalDateTime... values) {
    return localDateTimeNotIn(Arrays.asList(values));
  }

  @Deprecated
  public Item2_Relation localDateTimeLt(@NonNull LocalDateTime localDateTime) {
    return where(schema.localDateTime, "<", TypeAdapters.serializeLocalDateTime(localDateTime));
  }

  @Deprecated
  public Item2_Relation localDateTimeLe(@NonNull LocalDateTime localDateTime) {
    return where(schema.localDateTime, "<=", TypeAdapters.serializeLocalDateTime(localDateTime));
  }

  @Deprecated
  public Item2_Relation localDateTimeGt(@NonNull LocalDateTime localDateTime) {
    return where(schema.localDateTime, ">", TypeAdapters.serializeLocalDateTime(localDateTime));
  }

  @Deprecated
  public Item2_Relation localDateTimeGe(@NonNull LocalDateTime localDateTime) {
    return where(schema.localDateTime, ">=", TypeAdapters.serializeLocalDateTime(localDateTime));
  }

  public Item2_Relation nameEq(@NonNull String name) {
    return where(schema.name, "=", name);
  }

  public Item2_Relation nameNotEq(@NonNull String name) {
    return where(schema.name, "<>", name);
  }

  public Item2_Relation nameIn(@NonNull Collection<String> values) {
    return in(false, schema.name, values);
  }

  public Item2_Relation nameNotIn(@NonNull Collection<String> values) {
    return in(true, schema.name, values);
  }

  public final Item2_Relation nameIn(@NonNull String... values) {
    return nameIn(Arrays.asList(values));
  }

  public final Item2_Relation nameNotIn(@NonNull String... values) {
    return nameNotIn(Arrays.asList(values));
  }

  public Item2_Relation nameLt(@NonNull String name) {
    return where(schema.name, "<", name);
  }

  public Item2_Relation nameLe(@NonNull String name) {
    return where(schema.name, "<=", name);
  }

  public Item2_Relation nameGt(@NonNull String name) {
    return where(schema.name, ">", name);
  }

  public Item2_Relation nameGe(@NonNull String name) {
    return where(schema.name, ">=", name);
  }

  public Item2_Relation orderByCategory1Asc() {
    return orderBy(schema.category1.orderInAscending());
  }

  public Item2_Relation orderByCategory1Desc() {
    return orderBy(schema.category1.orderInDescending());
  }

  public Item2_Relation orderByCategory2Asc() {
    return orderBy(schema.category2.orderInAscending());
  }

  public Item2_Relation orderByCategory2Desc() {
    return orderBy(schema.category2.orderInDescending());
  }

  @Deprecated
  public Item2_Relation orderByZonedTimestampAsc() {
    return orderBy(schema.zonedTimestamp.orderInAscending());
  }

  @Deprecated
  public Item2_Relation orderByZonedTimestampDesc() {
    return orderBy(schema.zonedTimestamp.orderInDescending());
  }

  @Deprecated
  public Item2_Relation orderByLocalDateTimeAsc() {
    return orderBy(schema.localDateTime.orderInAscending());
  }

  @Deprecated
  public Item2_Relation orderByLocalDateTimeDesc() {
    return orderBy(schema.localDateTime.orderInDescending());
  }

  public Item2_Relation orderByNameAsc() {
    return orderBy(schema.name.orderInAscending());
  }

  public Item2_Relation orderByNameDesc() {
    return orderBy(schema.name.orderInDescending());
  }
}
