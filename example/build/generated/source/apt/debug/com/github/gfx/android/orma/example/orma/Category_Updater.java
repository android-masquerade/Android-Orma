package com.github.gfx.android.orma.example.orma;

import android.support.annotation.NonNull;
import com.github.gfx.android.orma.OrmaConnection;
import com.github.gfx.android.orma.Updater;
import java.util.Arrays;
import java.util.Collection;

public class Category_Updater extends Updater<Category, Category_Updater> {
  final Category_Schema schema;

  public Category_Updater(OrmaConnection conn, Category_Schema schema) {
    super(conn);
    this.schema = schema;
  }

  public Category_Updater(Category_Updater that) {
    super(that);
    this.schema = that.getSchema();
  }

  public Category_Updater(Category_Relation relation) {
    super(relation);
    this.schema = relation.getSchema();
  }

  @Override
  public Category_Updater clone() {
    return new Category_Updater(this);
  }

  @NonNull
  @Override
  public Category_Schema getSchema() {
    return schema;
  }

  public Category_Updater name(@NonNull String name) {
    contents.put("`name`", name);
    return this;
  }

  @Deprecated
  public Category_Updater nameEq(@NonNull String name) {
    return where(schema.name, "=", name);
  }

  @Deprecated
  public Category_Updater nameNotEq(@NonNull String name) {
    return where(schema.name, "<>", name);
  }

  @Deprecated
  public Category_Updater nameIn(@NonNull Collection<String> values) {
    return in(false, schema.name, values);
  }

  @Deprecated
  public Category_Updater nameNotIn(@NonNull Collection<String> values) {
    return in(true, schema.name, values);
  }

  @Deprecated
  public final Category_Updater nameIn(@NonNull String... values) {
    return nameIn(Arrays.asList(values));
  }

  @Deprecated
  public final Category_Updater nameNotIn(@NonNull String... values) {
    return nameNotIn(Arrays.asList(values));
  }

  @Deprecated
  public Category_Updater nameLt(@NonNull String name) {
    return where(schema.name, "<", name);
  }

  @Deprecated
  public Category_Updater nameLe(@NonNull String name) {
    return where(schema.name, "<=", name);
  }

  @Deprecated
  public Category_Updater nameGt(@NonNull String name) {
    return where(schema.name, ">", name);
  }

  @Deprecated
  public Category_Updater nameGe(@NonNull String name) {
    return where(schema.name, ">=", name);
  }

  public Category_Updater idEq(long id) {
    return where(schema.id, "=", id);
  }

  public Category_Updater idNotEq(long id) {
    return where(schema.id, "<>", id);
  }

  public Category_Updater idIn(@NonNull Collection<Long> values) {
    return in(false, schema.id, values);
  }

  public Category_Updater idNotIn(@NonNull Collection<Long> values) {
    return in(true, schema.id, values);
  }

  public final Category_Updater idIn(@NonNull Long... values) {
    return idIn(Arrays.asList(values));
  }

  public final Category_Updater idNotIn(@NonNull Long... values) {
    return idNotIn(Arrays.asList(values));
  }

  public Category_Updater idLt(long id) {
    return where(schema.id, "<", id);
  }

  public Category_Updater idLe(long id) {
    return where(schema.id, "<=", id);
  }

  public Category_Updater idGt(long id) {
    return where(schema.id, ">", id);
  }

  public Category_Updater idGe(long id) {
    return where(schema.id, ">=", id);
  }

  /**
   * To build a condition <code>id BETWEEN a AND b</code>, which is equivalent to <code>a <= id AND id <= b</code>.
   */
  public Category_Updater idBetween(long idA, long idB) {
    return whereBetween(schema.id, idA, idB);
  }
}
