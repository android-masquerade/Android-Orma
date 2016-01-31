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
package com.github.gfx.android.orma.migration;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SQLiteMaster {

    public static String TAG = "SQLiteMaster";

    public String type;

    public String name;

    public String tableName;

    public String sql;

    public List<SQLiteMaster> indexes = new ArrayList<>();

    public SQLiteMaster() {
    }

    public SQLiteMaster(String type, String name, String tableName, String sql) {
        this.type = type;
        this.name = name;
        this.tableName = tableName;
        this.sql = sql;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(sql);
        s.append("; ");

        for (SQLiteMaster index : indexes) {
            s.append(index);
            s.append("; ");
        }
        s.setLength(s.length() - "; ".length());
        return s.toString();
    }

    public static Map<String, SQLiteMaster> loadTables(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT type,name,tbl_name,sql FROM sqlite_master", null);

        Map<String, SQLiteMaster> tables = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        if (cursor.moveToFirst()) {
            do {
                String type = cursor.getString(0); // "table" or "index"
                String name = cursor.getString(1); // table or index name
                String tableName = cursor.getString(2);
                String sql = cursor.getString(3);

                SQLiteMaster meta = tables.get(tableName);
                if (meta == null) {
                    meta = new SQLiteMaster();
                    tables.put(tableName, meta);
                }

                switch (type) {
                    case "table":
                        meta.type = type;
                        meta.name = name;
                        meta.tableName = tableName;
                        meta.sql = sql;
                        break;
                    case "index":
                        // sql=null for sqlite_autoindex_${table}_${columnIndex}
                        if (sql != null) {
                            meta.indexes.add(new SQLiteMaster(type, name, tableName, sql));
                        }
                        break;
                    default:
                        Log.w(TAG, "unsupported type:" + type);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return tables;
    }
}
