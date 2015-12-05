# Orma for Android [![Circle CI](https://circleci.com/gh/gfx/Android-Orma/tree/master.svg?style=svg)](https://circleci.com/gh/gfx/Android-Orma/tree/master) [ ![Download](https://api.bintray.com/packages/gfx/maven/orma/images/download.svg) ](https://bintray.com/gfx/maven/orma/_latestVersion)

* Note that this is an **alpha** software and the interface will change until v1.0.0.

Orma is a lightning-fast ORM (Object-Relation Mapper) for [Android SQLiteDatabase](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html),
generating helper classes at compile time with annotation processing.

There are already [a lot of ORMs](https://android-arsenal.com/tag/69). Why I have to add another?

The answer is that I need ORM that have the following features:

* As fast as hand-written code is
* Model classes must have no restriction
  * They might be POJO, Parcelable and/or even models that are managed by another ORM
  * They should be passed to another thread
* Database handles must be instances
  * Not a singleton nor static-method based class
* Automatic migration
  * For what can be detected logically
  * i.e. simple `add column` and `drop column`

They are just what Orma has. This is as fast as Realm, its models have no restriction, database handle is
not a singleton, and has `SchemaDiffMigration` for automatic migration.

# Install

```groovy
// To use "apt" in dependencies

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

apply plugin: 'com.neenbedankt.android-apt'
```

```groovy
// To use orma in your Android applications or libraries

dependencies {
    apt 'com.github.gfx.android.orma:orma-processor:0.9.0'
    compile 'com.github.gfx.android.orma:orma:0.9.0'
}
```

# Synopsis

First, define model classes annotated with `@Table`, `@Column`, and `@PrimaryKey`.

```java
package com.github.gfx.android.orma.example;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import android.support.annotation.Nullable;

@Table
public class Todo {

    @PrimaryKey
    public long id;

    @Column(indexed = true)
    public String title;

    @Column
    @Nullable // indicates NOT NULL constraints
    public String content;

    @Column
    public long createdTimeMillis;
}
```

Second, create a database handle `OrmaDatabase`, which is generated by `orma-processor`.

To make it:

```java
// see OrmaConfiguration for options
// each value is the default value.
OrmaDatabase orma = OrmaDatabase.builder(context)
    .name(context.getPackageName() + ".orma.db") // optional
    .migrationEngine(new SchemaDiffMigration(context, BuildConfig.DEBUG)) // optional
    .typeAdapters(TypeAdapterRegistry.defaultTypeAdapters()) // optional
    .writeAheadLogging(true) // optional
    .trace(BuildConfig.DEBUG) // optional
    .readOnMainThread(AccessThreadConstraint.WARNING) // optional
    .writeOnMainThread(AccessThreadConstraint.FATAL) // optional
    .build();
```

Then, you can create, read, update and delete models:

```java
Todo todo = ...;

// create
orma.insertIntoTodo(todo);

// prepared statements with transaction
orma.transactionSync( -> { // or transactionAsync() to execute tasks in background
    Inserter<Todo> inserter = orma.prepareInsertIntoTodo();
    inserter.execute(todo);
});

// read
orma.selectFromTodo()
  .titleEq("foo") // equivalent to `where("title = ?", "foo")`
  .observable() // first-class RxJava interface
  .subscribe(...);

// update
orma.updateTodo()
  .titleEq("foo")
  .content("a new content")
  .execute();

// delete
orma.deleteTodo()
  .titleEq("foo")
  .execute();
```

Note that **Orma checks DB access on main thread** in trace build by default.
Use background threads explicitly or RxJava interfaces with `Schedulers.io()`.

# Models

## Condition Helpers

Condition Helpers, e.g. `titleEq()` shown above, are methods
to make `WHERE` clauses.

They are generated for `indexed` columns.

## Accessors

You can define private columns with `@Getter` and `@Setter`, which tells `orma-processor` to use accessors.

```
@Table
public class KeyValuePair {

    static final String kKey = "key";

    static final String kValue = "value";

    @Column(kKey)
    private String key;

    @Column(kValue)
    private String value;

    @Getter(kKey)
    public String getKey() {
        return key;
    }

    @Setter(kKey)
    public void setKey(String key) {
        this.key = key;
    }

    @Getter(kValue)
    public String getValue() {
        return value;
    }

    @Setter(kValue)
    public void setValue(String value) {
        this.value = value;
    }
}
```

# Migration

Orma has pluggable migration mechanism via `MigrationEngine`.

The default migration engine is `SchemaDiffMigration`, which handles
schema changes by making diff with old and new schemas.

You can set a custom migration engine to OrmaDatabase builders:

```java
class CustomMigrationEngine implements MigrationEngine { ... }

OrmaDatabase orma = OrmaDatabase.builder(context)
  .migrationEngine(new CustomMigrationEngine())
  .build();
```

See [migration/README.md](migration/README.md) for details.

# Type Adapters

Type adapters, which serializes and deserializes custom classes, are supported.

If you use type adapters, you can add them to `OrmaDatabase`:

```java
class FooAdapter extends AbstractTypeAdapter<Foo> {
    @Override
    @NonNull
    public String serialize(@NonNull Foo source) {
        return ... serialize ...;
    }

    @Override
    @NonNull
    public Foo deserialize(@NonNull String serialized) {
        return ... deserialize ...;
    }
}

OrmaDatabase orma = OrmaDatabase.builder(context)
    .addTypeAdapters(new FooAdapter())
    .build();
```

## Built-In Type Adapters

There are a few built-in type adapter provided by default:

* `StringListAdapter` for `List<String>`
* `StringSetAdapter` for `Set<String>`
* `DateAdapter` for `Date`
* `UriAdapter` for `Uri`

# Example

There is an example app to demonstrate what Orma is.

It is also including a simple benchmark with Realm and hand-written SQLiteDatabase
operations.

See [example/](example/) for details.

# Support

* Use [GitHub issues](https://github.com/gfx/Android-Orma/issues) for the issue tracker
* Feel free to ask for questions to the author [@\_\_gfx\_\_](https://twitter.com/__gfx__)

# Licenses in Runtime Dependencies

* https://github.com/ReactiveX/RxJava - Apache Software License 2.0
* https://github.com/JSQLParser/JSqlParser - LGPL v2.1 and Apache Software License 2.0 (dual licenses)

# Release Engineering

```shell
./gradlew bumpMajor # or bumpMinor / bumpPatch
make publish # does release engineering
```

# See Also

* [SQLite](http://sqlite.org/)
* [SQLiteDatabase](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html)
* [Version of SQLite used in Android? - Stack Overflow](http://stackoverflow.com/questions/2421189/version-of-sqlite-used-in-android)

# Author

FUJI Goro (gfx).

# License

The MIT License.

Copyright (c) 2015 FUJI Goro (gfx) <gfuji@cpan.org>.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
