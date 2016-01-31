/*
 * Copyright (c) 2015 FUJI Goro (gfx).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.gfx.android.orma;

import com.github.gfx.android.orma.annotation.OnConflict;
import com.github.gfx.android.orma.internal.OrmaConditionBase;

import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;

/**
 * Representation of a relation, or a {@code SELECT} query.
 *
 * @param <Model> An Orma model
 * @param <R>     The derived class itself. e.g {@code class Foo_Schema extends Relation<Foo, Foo_Schema>}
 */
public abstract class Relation<Model, R extends Relation<Model, ?>> extends OrmaConditionBase<Model, R>
        implements Cloneable, Iterable<Model> {

    final protected ArrayList<OrderSpec<Model>> orderSpecs = new ArrayList<>();

    public Relation(@NonNull OrmaConnection connection, @NonNull Schema<Model> schema) {
        super(connection, schema);
    }

    public Relation(@NonNull Relation<Model, ?> relation) {
        super(relation);
    }

    @SuppressWarnings("unchecked")
    public R orderBy(@NonNull OrderSpec<Model> orderSpec) {
        orderSpecs.add(orderSpec);
        return (R) this;
    }

    @Nullable
    protected String buildOrderingTerms() {
        if (orderSpecs.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (OrderSpec<Model> orderSpec : orderSpecs) {
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(orderSpec);
        }
        return sb.toString();
    }

    @IntRange(from = 0)
    public int count() {
        return selector().count();
    }

    @NonNull
    public Model get(@IntRange(from = 0) int position) {
        return selector().get(position);
    }


    @NonNull
    public Model getOrCreate(@IntRange(from = 0) long position, @NonNull ModelFactory<Model> factory) {
        Model model = selector().getOrNull(position);
        if (model == null) {
            return conn.createModel(schema, factory);
        } else {
            return model;
        }
    }

    @NonNull
    public Single<Model> getWithTransactionAsObservable(@IntRange(from = 0) final int position) {
        return Single.create(new Single.OnSubscribe<Model>() {
            @Override
            public void call(final SingleSubscriber<? super Model> subscriber) {
                conn.transactionNonExclusiveSync(new TransactionTask() {
                    @Override
                    public void execute() throws Exception {
                        subscriber.onSuccess(get(position));
                    }

                    @Override
                    public void onError(@NonNull Exception exception) {
                        subscriber.onError(exception);
                    }
                });
            }
        });
    }

    /**
     * Finds the index of the item, assuming an order specified by a set of {@code orderBy*()} methods.
     *
     * @param item The item to find
     * @return The position of the item
     */
    @SuppressWarnings("unchecked")
    public int indexOf(@NonNull Model item) {
        Selector<Model, ?> selector = selector();
        for (OrderSpec<Model> orderSpec : orderSpecs) {
            ColumnDef<Model, ?> column = orderSpec.column;
            if (orderSpec.ordering.equals(OrderSpec.ASC)) {
                selector.where(column, "<", column.get(item));
            } else {
                selector.where(column, ">", column.get(item));
            }
        }
        return selector.count();
    }

    /**
     * Deletes a specified model and yields where it was. Suitable to implement {@link android.widget.Adapter}.
     * Operations are executed in a transaction.
     *
     * @param item A model to delete.
     * @return An {@link Observable} that yields the position of the deleted item if the item is deleted.
     */
    @NonNull
    public Observable<Integer> deleteWithTransactionAsObservable(@NonNull final Model item) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                conn.transactionSync(new TransactionTask() {
                    @Override
                    public void execute() throws Exception {
                        int position = indexOf(item);

                        ColumnDef<Model, ?> column = schema.getPrimaryKey();
                        int deletedRows = deleter()
                                .where(column, "=", column.get(item))
                                .execute();

                        if (deletedRows > 0) {
                            subscriber.onNext(position);
                        }
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(@NonNull Exception exception) {
                        subscriber.onError(exception);
                    }
                });
            }
        });
    }

    /**
     * Truncates the table to the specified size. Operations are executed in a transaction.
     *
     * @param size Size to truncate the table
     * @return A {@link Single} that yields the number of rows deleted.
     */
    @NonNull
    public Single<Integer> truncateWithTransactionAsObservable(@IntRange(from = 0) final int size) {
        return Single.create(new Single.OnSubscribe<Integer>() {
            @Override
            public void call(SingleSubscriber<? super Integer> subscriber) {
                String select = SQLiteQueryBuilder.buildQueryString(
                        false, schema.getEscapedTableName(), new String[]{schema.getPrimaryKey().toString()},
                        getWhereClause(), null, null, buildOrderingTerms(), size + "," + Integer.MAX_VALUE);

                int deletedRows = conn.delete(schema, schema.getPrimaryKey() + " IN (" + select + ")", getBindArgs());
                subscriber.onSuccess(deletedRows);
            }
        });
    }

    /**
     * Inserts an item. Operations are executed in a transaction.
     *
     * @param factory A model to insert.
     * @return An {@link Single} that yields the newly inserted row id.
     */
    @NonNull
    public Single<Long> insertWithTransactionAsObservable(@NonNull final ModelFactory<Model> factory) {
        return Single.create(new Single.OnSubscribe<Long>() {
            @Override
            public void call(final SingleSubscriber<? super Long> subscriber) {
                conn.transactionSync(new TransactionTask() {
                    @Override
                    public void execute() throws Exception {
                        long rowId = inserter().execute(factory);
                        subscriber.onSuccess(rowId);
                    }

                    @Override
                    public void onError(@NonNull Exception exception) {
                        subscriber.onError(exception);
                    }
                });
            }
        });
    }

    @Override
    public abstract R clone();

    // Operation helpers

    @NonNull
    public abstract Selector<Model, ?> selector();

    @NonNull
    public abstract Updater<Model, ?> updater();

    @NonNull
    public abstract Deleter<Model, ?> deleter();

    @NonNull
    public Inserter<Model> inserter() {
        return inserter(OnConflict.NONE);
    }

    @NonNull
    public Inserter<Model> inserter(@OnConflict int onConflictAlgorithm) {
        return new Inserter<>(conn, schema, schema.getInsertStatement(onConflictAlgorithm));
    }

    // Iterator<Model>

    @Override
    public Iterator<Model> iterator() {
        return selector().iterator();
    }
}
