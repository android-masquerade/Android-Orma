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

package com.github.gfx.android.orma.internal;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A helper class to get the type instance of parameterized types.
 *
 * @see <a href="https://github.com/google/gson/blob/master/gson/src/main/java/com/google/gson/reflect/TypeToken.java">google/gson/TypeToken.java</a>
 */
@SuppressWarnings("unused")
public abstract class TypeHolder<T> {


    public Type getType() {
        Class<?> c = getClass();
        ParameterizedType t;
        try {
            t = (ParameterizedType) c.getGenericSuperclass();
        } catch (ClassCastException e) {
            throw new RuntimeException("No type signature found. Missing -keepattributes Signature in progurad-rules.pro?", e);
        }
        return EquatableTypeWrapper.wrap(t.getActualTypeArguments()[0]);
    }
}
