/*
 * Copyright 2020 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following.strategies;

import org.dmfs.jems.predicate.Predicate;
import org.dmfs.jems.single.Single;
import org.dmfs.jems.single.decorators.DelegatingSingle;
import org.dmfs.jems.single.elementary.ValueSingle;


/**
 * A {@link Single} {@link Boolean} derived from other values.
 *
 * @deprecated consider moving this into jems.
 */
@Deprecated
final class Bool extends DelegatingSingle<Boolean>
{
    <T> Bool(T value, Predicate<? super T> predicate)
    {
        this(new ValueSingle<>(value), predicate);
    }


    <T> Bool(Single<? extends T> value, Predicate<? super T> predicate)
    {
        super(() -> predicate.satisfiedBy(value.value()));
    }
}
