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
package com.github.gfx.android.orma.example.activity;

import com.github.gfx.android.orma.AccessThreadConstraint;
import com.github.gfx.android.orma.ModelFactory;
import com.github.gfx.android.orma.Relation;
import com.github.gfx.android.orma.example.R;
import com.github.gfx.android.orma.example.databinding.ActivityRecyclerViewBinding;
import com.github.gfx.android.orma.example.databinding.CardTodoBinding;
import com.github.gfx.android.orma.example.orma.OrmaDatabase;
import com.github.gfx.android.orma.example.orma.Todo;
import com.github.gfx.android.orma.widget.OrmaRecyclerViewAdapter;

import org.threeten.bp.ZonedDateTime;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;

import rx.schedulers.Schedulers;

public class RecyclerViewActivity extends AppCompatActivity {

    OrmaDatabase orma;

    ActivityRecyclerViewBinding binding;

    Adapter adapter;

    int number = 0;

    public static Intent createIntent(Context context) {
        return new Intent(context, RecyclerViewActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view);

        orma = OrmaDatabase.builder(this)
                .readOnMainThread(AccessThreadConstraint.NONE)
                .build();

        adapter = new Adapter(this, orma.relationOfTodo().doneEq(true).orderByCreatedTimeAsc());
        binding.list.setAdapter(adapter);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItemAsObservable(new ModelFactory<Todo>() {
                    @NonNull
                    @Override
                    public Todo call() {
                        Todo todo = new Todo();
                        number++;
                        todo.title = "RecyclerView item #" + number;
                        todo.content = ZonedDateTime.now().toString();
                        todo.createdTime = new Date();
                        todo.done = System.currentTimeMillis() % 2 == 0;
                        return todo;
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }
        });
    }

    static class VH extends RecyclerView.ViewHolder {

        CardTodoBinding binding;

        public VH(LayoutInflater inflater, ViewGroup parent) {
            super(CardTodoBinding.inflate(inflater, parent, false).getRoot());
            binding = DataBindingUtil.getBinding(itemView);
        }
    }

    static class Adapter extends OrmaRecyclerViewAdapter<Todo, VH> {

        public Adapter(@NonNull Context context, @NonNull Relation<Todo, ?> relation) {
            super(context, relation);
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            CardTodoBinding binding = holder.binding;
            final Todo todo = getItem(position);

            binding.title.setText(todo.title);
            binding.content.setText(todo.content);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItemAsObservable(todo)
                            .subscribeOn(Schedulers.io())
                            .subscribe();
                }
            });
        }
    }
}
