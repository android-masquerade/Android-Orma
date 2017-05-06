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

package com.github.gfx.android.orma.example_kotlin

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import java.sql.Timestamp

@Table
class Item {

    @PrimaryKey(autoincrement = true)
    public val id: Long

    @Column
    public val content: String

    @Column
    public val createdTime: Timestamp

    constructor(@Setter("id") id: Long, @Setter("content") content: String, @Setter("createdTime") createdTime: Timestamp) {
        this.id = id
        this.content = content
        this.createdTime = createdTime
    }

    constructor(id: Long, content: String) {
        this.id = id
        this.content = content
        this.createdTime = Timestamp(System.currentTimeMillis())
    }
}
