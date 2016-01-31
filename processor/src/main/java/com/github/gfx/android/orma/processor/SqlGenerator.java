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
package com.github.gfx.android.orma.processor;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.OnConflict;
import com.squareup.javapoet.CodeBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlGenerator {

    final ProcessingContext context;

    public SqlGenerator(ProcessingContext context) {
        this.context = context;
    }

    public String buildCreateTableStatement(SchemaDefinition schema) {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ");
        appendIdentifier(sb, schema.getTableName());
        sb.append(" (");

        int nColumns = schema.getColumns().size();
        for (int i = 0; i < nColumns; i++) {
            ColumnDefinition column = schema.getColumns().get(i);
            appendColumnDef(sb, column);

            if ((i + 1) != nColumns) {
                sb.append(", ");
            }
        }

        for (String constraint : schema.constraints) {
            if (Strings.isEmpty(constraint)) {
                throw new ProcessingException("Empty constraint found", schema.getElement());
            }
            sb.append(", ");
            sb.append(constraint);
        }

        sb.append(')');

        return sb.toString();

    }

    public void appendColumnDef(StringBuilder sb, ColumnDefinition column) {
        appendIdentifier(sb, column.columnName);
        sb.append(' ');

        sb.append(column.getStorageType());
        sb.append(' ');

        List<String> constraints = new ArrayList<>();

        if (column.primaryKey) {
            constraints.add("PRIMARY KEY");
            if (column.primaryKeyOnConflict != OnConflict.NONE) {
                constraints.add("ON CONFLICT");
                constraints.add(onConflictClause(column.primaryKeyOnConflict));
            }
            if (column.autoincrement) {
                constraints.add("AUTOINCREMENT");
            }
        } else {
            if (column.unique) {
                constraints.add("UNIQUE");
                if (column.uniqueOnConflict != OnConflict.NONE) {
                    constraints.add("ON CONFLICT");
                    constraints.add(onConflictClause(column.uniqueOnConflict));
                }
            }
            if (!column.nullable) {
                constraints.add("NOT NULL");
            }
        }

        if (!Strings.isEmpty(column.defaultExpr)) {
            constraints.add("DEFAULT " + column.defaultExpr);
        }

        if (column.collate != Column.Collate.BINARY) {
            constraints.add("COLLATE " + column.collate.name());
        }

        if (Types.isSingleAssociation(column.type)) {
            constraints.add(foreignKeyConstraints(column));
        }

        sb.append(constraints.stream().collect(Collectors.joining(" ")));
    }

    String onConflictClause(@OnConflict int conflictAlgorithm) {
        switch (conflictAlgorithm) {
            case OnConflict.ROLLBACK:
                return "ROLLBACK";
            case OnConflict.ABORT:
                return "ABORT";
            case OnConflict.FAIL:
                return "FAIL";
            case OnConflict.IGNORE:
                return "IGNORE";
            case OnConflict.REPLACE:
                return "REPLACE";
            default:
                return null;
        }
    }

    String foreignKeyConstraints(ColumnDefinition column) {
        AssociationDefinition a = column.getAssociation();
        SchemaDefinition foreignTableSchema = context.getSchemaDef(a.modelType);
        StringBuilder sb = new StringBuilder();
        sb.append("REFERENCES ");
        appendIdentifier(sb, foreignTableSchema.getTableName());
        sb.append('(');
        appendIdentifier(sb, foreignTableSchema.getPrimaryKeyName());
        sb.append(')');
        sb.append(" ON UPDATE CASCADE");
        sb.append(" ON DELETE CASCADE");
        return sb.toString();
    }

    public CodeBlock buildCreateIndexStatements(SchemaDefinition schema) {
        CodeBlock.Builder builder = CodeBlock.builder();

        List<ColumnDefinition> indexedColumns = new ArrayList<>();
        schema.getColumns().forEach(column -> {
            if (column.indexed && !column.primaryKey) {
                indexedColumns.add(column);
            }
        });

        if (indexedColumns.isEmpty()) {
            return builder.addStatement("return $T.emptyList()", Types.Collections).build();
        }

        builder.add("return $T.asList(\n", Types.Arrays).indent();

        int nColumns = indexedColumns.size();
        for (int i = 0; i < nColumns; i++) {
            ColumnDefinition column = indexedColumns.get(i);
            StringBuilder sb = new StringBuilder();

            sb.append("CREATE INDEX ");
            appendIdentifier(sb, "index_" + column.columnName + "_on_" + schema.getTableName());
            sb.append(" ON ");
            appendIdentifier(sb, schema.getTableName());
            sb.append(" (");
            appendIdentifier(sb, column.columnName);
            sb.append(")");

            builder.add("$S", sb);

            if ((i + 1) != nColumns) {
                builder.add(",\n");
            } else {
                builder.add("\n");
            }
        }
        builder.unindent().add(");\n");

        return builder.build();
    }

    public String buildDropTableStatement(SchemaDefinition schema) {
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE IF EXISTS ");
        appendIdentifier(sb, schema.getTableName());
        return sb.toString();
    }


    public CodeBlock buildInsertStatementCode(SchemaDefinition schema, String onConflictAlgorithmParamName) {
        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        codeBuilder.addStatement("$T s = new $T()", StringBuilder.class, StringBuilder.class);


        codeBuilder.addStatement("s.append($S)", "INSERT");

        codeBuilder.beginControlFlow("switch ($L)", onConflictAlgorithmParamName)
                .addStatement("case $T.NONE: /* nop */ break", OnConflict.class)
                .addStatement("case $T.ABORT: s.append($S); break", OnConflict.class, " OR ABORT")
                .addStatement("case $T.FAIL: s.append($S); break", OnConflict.class, " OR FAIL")
                .addStatement("case $T.IGNORE: s.append($S); break", OnConflict.class, " OR IGNORE")
                .addStatement("case $T.REPLACE: s.append($S); break", OnConflict.class, " OR REPLACE")
                .addStatement("case $T.ROLLBACK: s.append($S); break", OnConflict.class, " OR ROLLBACK")
                .endControlFlow();

        StringBuilder sb = new StringBuilder();
        sb.append(" INTO ");
        appendIdentifier(sb, schema.getTableName());
        sb.append(" (");

        List<ColumnDefinition> columns = schema.getColumns();
        int nColumns = columns.size();
        for (int i = 0; i < nColumns; i++) {
            ColumnDefinition c = columns.get(i);
            if (c.autoId) {
                continue;
            }
            appendIdentifier(sb, c.columnName);
            if ((i + 1) != nColumns && !columns.get(i + 1).autoId) {
                sb.append(',');
            }
        }
        sb.append(')');
        sb.append(" VALUES (");
        for (int i = 0; i < nColumns; i++) {
            ColumnDefinition c = columns.get(i);
            if (c.autoId) {
                continue;
            }
            sb.append('?');
            if ((i + 1) != nColumns && !columns.get(i + 1).autoId) {
                sb.append(',');
            }
        }
        sb.append(')');

        codeBuilder.addStatement("s.append($S)", sb.toString());
        codeBuilder.addStatement("return s.toString()");

        return codeBuilder.build();
    }


    public void appendIdentifier(StringBuilder sb, String identifier) {
        sb.append('"');
        sb.append(identifier);
        sb.append('"');
    }


    public String quoteIdentifier(String identifier) {
        StringBuilder sb = new StringBuilder();
        appendIdentifier(sb, identifier);
        return sb.toString();
    }
}
