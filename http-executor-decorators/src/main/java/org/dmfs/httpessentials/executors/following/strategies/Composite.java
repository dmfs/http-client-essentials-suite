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

import org.dmfs.httpessentials.executors.following.RedirectStrategy;
import org.dmfs.jems.iterable.decorators.Mapped;
import org.dmfs.jems.iterable.elementary.Seq;
import org.dmfs.jems.optional.adapters.FirstPresent;


/**
 * A {@link RedirectStrategy} which follows the first {@link RedirectStrategy} which returns a result.
 */
public final class Composite extends DelegatingRedirectStrategy
{
    public Composite(RedirectStrategy... delegates)
    {
        this(new Seq<>(delegates));
    }


    public Composite(Iterable<? extends RedirectStrategy> delegates)
    {
        super(((response, redirectNumber) ->
                new FirstPresent<>(
                        new Mapped<>(
                                strategy -> strategy.location(response, redirectNumber),
                                delegates))));
    }
}
