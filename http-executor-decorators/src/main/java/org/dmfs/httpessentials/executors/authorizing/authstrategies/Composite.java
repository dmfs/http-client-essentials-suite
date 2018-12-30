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

package org.dmfs.httpessentials.executors.authorizing.authstrategies;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authorizing.AuthState;
import org.dmfs.httpessentials.executors.authorizing.AuthStrategy;
import org.dmfs.iterables.decorators.Reverse;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.jems.single.elementary.Reduced;

import java.net.URI;


/**
 * An {@link AuthStrategy} which combines other {@link AuthStrategy}s and tries each one until one succeeds.
 *
 * @author Marten Gajda
 */
public final class Composite implements AuthStrategy
{
    private final Iterable<AuthStrategy> mStrategies;


    /**
     * Creates a composite {@link AuthStrategy} of the given {@link AuthStrategy}s.
     *
     * @param strategies
     *         The {@link AuthStrategy}s, the one with the highest priority being first.
     */
    public Composite(AuthStrategy... strategies)
    {
        this(new Seq<>(strategies));
    }


    /**
     * Creates a composite {@link AuthStrategy} of the given {@link AuthStrategy}s.
     *
     * @param strategies
     *         Iterable of {@link AuthStrategy}s, the one with the highest priority being first.
     */
    public Composite(Iterable<AuthStrategy> strategies)
    {
        mStrategies = strategies;
    }


    @Override
    public AuthState authState(final HttpMethod method, final URI uri, AuthState fallback)
    {
        return new Reduced<>(
                fallback,
                (authState, strategy) -> strategy.authState(method, uri, authState),
                new Reverse<>(mStrategies)).value();
    }
}
