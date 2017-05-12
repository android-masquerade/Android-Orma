package com.github.gfx.android.orma.example.orma;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.gfx.android.orma.OrmaConnection;
import com.github.gfx.android.orma.Selector;
import java.util.Arrays;
import java.util.Collection;

public class Entry_Selector extends Selector<Entry, Entry_Selector> {
  final Entry_Schema schema;

  public Entry_Selector(OrmaConnection conn, Entry_Schema schema) {
    super(conn);
    this.schema = schema;
  }

  public Entry_Selector(Entry_Selector that) {
    super(that);
    this.schema = that.getSchema();
  }

  public Entry_Selector(Entry_Relation relation) {
    super(relation);
    this.schema = relation.getSchema();
  }

  @Override
  public Entry_Selector clone() {
    return new Entry_Selector(this);
  }

  @NonNull
  @Override
  public Entry_Schema getSchema() {
    return schema;
  }

  @Deprecated
  public Entry_Selector resourceTypeEq(@NonNull String resourceType) {
    return where(schema.resourceType, "=", resourceType);
  }

  @Deprecated
  public Entry_Selector resourceTypeNotEq(@NonNull String resourceType) {
    return where(schema.resourceType, "<>", resourceType);
  }

  @Deprecated
  public Entry_Selector resourceTypeIn(@NonNull Collection<String> values) {
    return in(false, schema.resourceType, values);
  }

  @Deprecated
  public Entry_Selector resourceTypeNotIn(@NonNull Collection<String> values) {
    return in(true, schema.resourceType, values);
  }

  @Deprecated
  public final Entry_Selector resourceTypeIn(@NonNull String... values) {
    return resourceTypeIn(Arrays.asList(values));
  }

  @Deprecated
  public final Entry_Selector resourceTypeNotIn(@NonNull String... values) {
    return resourceTypeNotIn(Arrays.asList(values));
  }

  @Deprecated
  public Entry_Selector resourceTypeLt(@NonNull String resourceType) {
    return where(schema.resourceType, "<", resourceType);
  }

  @Deprecated
  public Entry_Selector resourceTypeLe(@NonNull String resourceType) {
    return where(schema.resourceType, "<=", resourceType);
  }

  @Deprecated
  public Entry_Selector resourceTypeGt(@NonNull String resourceType) {
    return where(schema.resourceType, ">", resourceType);
  }

  @Deprecated
  public Entry_Selector resourceTypeGe(@NonNull String resourceType) {
    return where(schema.resourceType, ">=", resourceType);
  }

  @Deprecated
  public Entry_Selector resourceIdEq(long resourceId) {
    return where(schema.resourceId, "=", resourceId);
  }

  @Deprecated
  public Entry_Selector resourceIdNotEq(long resourceId) {
    return where(schema.resourceId, "<>", resourceId);
  }

  @Deprecated
  public Entry_Selector resourceIdIn(@NonNull Collection<Long> values) {
    return in(false, schema.resourceId, values);
  }

  @Deprecated
  public Entry_Selector resourceIdNotIn(@NonNull Collection<Long> values) {
    return in(true, schema.resourceId, values);
  }

  @Deprecated
  public final Entry_Selector resourceIdIn(@NonNull Long... values) {
    return resourceIdIn(Arrays.asList(values));
  }

  @Deprecated
  public final Entry_Selector resourceIdNotIn(@NonNull Long... values) {
    return resourceIdNotIn(Arrays.asList(values));
  }

  @Deprecated
  public Entry_Selector resourceIdLt(long resourceId) {
    return where(schema.resourceId, "<", resourceId);
  }

  @Deprecated
  public Entry_Selector resourceIdLe(long resourceId) {
    return where(schema.resourceId, "<=", resourceId);
  }

  @Deprecated
  public Entry_Selector resourceIdGt(long resourceId) {
    return where(schema.resourceId, ">", resourceId);
  }

  @Deprecated
  public Entry_Selector resourceIdGe(long resourceId) {
    return where(schema.resourceId, ">=", resourceId);
  }

  /**
   * To build a condition <code>resourceId BETWEEN a AND b</code>, which is equivalent to <code>a <= resourceId AND resourceId <= b</code>.
   */
  @Deprecated
  public Entry_Selector resourceIdBetween(long resourceIdA, long resourceIdB) {
    return whereBetween(schema.resourceId, resourceIdA, resourceIdB);
  }

  public Entry_Selector idEq(long id) {
    return where(schema.id, "=", id);
  }

  public Entry_Selector idNotEq(long id) {
    return where(schema.id, "<>", id);
  }

  public Entry_Selector idIn(@NonNull Collection<Long> values) {
    return in(false, schema.id, values);
  }

  public Entry_Selector idNotIn(@NonNull Collection<Long> values) {
    return in(true, schema.id, values);
  }

  public final Entry_Selector idIn(@NonNull Long... values) {
    return idIn(Arrays.asList(values));
  }

  public final Entry_Selector idNotIn(@NonNull Long... values) {
    return idNotIn(Arrays.asList(values));
  }

  public Entry_Selector idLt(long id) {
    return where(schema.id, "<", id);
  }

  public Entry_Selector idLe(long id) {
    return where(schema.id, "<=", id);
  }

  public Entry_Selector idGt(long id) {
    return where(schema.id, ">", id);
  }

  public Entry_Selector idGe(long id) {
    return where(schema.id, ">=", id);
  }

  /**
   * To build a condition <code>id BETWEEN a AND b</code>, which is equivalent to <code>a <= id AND id <= b</code>.
   */
  public Entry_Selector idBetween(long idA, long idB) {
    return whereBetween(schema.id, idA, idB);
  }

  public Entry_Selector resourceTypeAndResourceIdEq(@NonNull String resourceType, long resourceId) {
    return where(schema.resourceType, "=", resourceType).where(schema.resourceId, "=", resourceId);
  }

  @Deprecated
  public Entry_Selector orderByResourceTypeAsc() {
    return orderBy(schema.resourceType.orderInAscending());
  }

  @Deprecated
  public Entry_Selector orderByResourceTypeDesc() {
    return orderBy(schema.resourceType.orderInDescending());
  }

  @Deprecated
  public Entry_Selector orderByResourceIdAsc() {
    return orderBy(schema.resourceId.orderInAscending());
  }

  @Deprecated
  public Entry_Selector orderByResourceIdDesc() {
    return orderBy(schema.resourceId.orderInDescending());
  }

  @Deprecated
  public Entry_Selector orderByIdAsc() {
    return orderBy(schema.id.orderInAscending());
  }

  @Deprecated
  public Entry_Selector orderByIdDesc() {
    return orderBy(schema.id.orderInDescending());
  }

  public Entry_Selector orderByresourceTypeAndResourceIdAsc() {
    return orderBy(schema.resourceType.orderInAscending()).orderBy(schema.resourceId.orderInAscending());
  }

  public Entry_Selector orderByresourceTypeAndResourceIdDesc() {
    return orderBy(schema.resourceType.orderInDescending()).orderBy(schema.resourceId.orderInDescending());
  }

  @Nullable
  public Long minByResourceId() {
    Cursor cursor = executeWithColumns(schema.resourceId.buildCallExpr("MIN"));
    try {
      cursor.moveToFirst();
      return cursor.isNull(0) ? null : schema.resourceId.getFromCursor(conn, cursor, 0);
    }
    finally {
      cursor.close();
    }
  }

  @Nullable
  public Long maxByResourceId() {
    Cursor cursor = executeWithColumns(schema.resourceId.buildCallExpr("MAX"));
    try {
      cursor.moveToFirst();
      return cursor.isNull(0) ? null : schema.resourceId.getFromCursor(conn, cursor, 0);
    }
    finally {
      cursor.close();
    }
  }

  @Nullable
  public Long sumByResourceId() {
    Cursor cursor = executeWithColumns(schema.resourceId.buildCallExpr("SUM"));
    try {
      cursor.moveToFirst();
      return cursor.isNull(0) ? null : cursor.getLong(0);
    }
    finally {
      cursor.close();
    }
  }

  @Nullable
  public Double avgByResourceId() {
    Cursor cursor = executeWithColumns(schema.resourceId.buildCallExpr("AVG"));
    try {
      cursor.moveToFirst();
      return cursor.isNull(0) ? null : cursor.getDouble(0);
    }
    finally {
      cursor.close();
    }
  }
}
