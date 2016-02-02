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
package com.github.gfx.android.orma.adapter;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

@Deprecated
public class BigDecimalAdapter extends AbstractTypeAdapter<BigDecimal> {

    @NonNull
    @Override
    public String serialize(@NonNull BigDecimal source) {
        return source.toString();
    }

    @NonNull
    @Override
    public BigDecimal deserialize(@NonNull String serialized) {
        return new BigDecimal(serialized);
    }
}
