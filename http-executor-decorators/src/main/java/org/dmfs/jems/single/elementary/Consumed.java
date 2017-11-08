/*
 * Copyright 2017 dmfs GmbH
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.jems.single.elementary;

import org.dmfs.jems.function.BiFunction;
import org.dmfs.jems.single.Single;


/**
 * A {@link Single} of a consumed {@link Iterable}.
 *
 * @author Marten Gajda
 */
public final class Consumed<V, T> implements Single<T>
{
    private final T mInitialValue;
    private final Iterable<V> mIterable;
    private final BiFunction<V, T, T> mFunction;


    public Consumed(T initialValue, BiFunction<V, T, T> function, Iterable<V> iterable)
    {
        mInitialValue = initialValue;
        mIterable = iterable;
        mFunction = function;
    }


    @Override
    public T value()
    {
        T result = mInitialValue;
        for (V value : mIterable)
        {
            result = mFunction.value(value, result);
        }
        return result;
    }
}
