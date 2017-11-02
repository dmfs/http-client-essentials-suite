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

package org.dmfs.httpessentials.executors.authenticating.strategies;

import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.AuthStrategy;
import org.dmfs.iterables.elementary.Seq;


/**
 * An {@link AuthStrategy} which combines other {@link AuthStrategy}s and tries each one in the reverse given order.
 *
 * @author Marten Gajda
 */
public final class Composite implements AuthStrategy
{
    private final Iterable<AuthStrategy> mStrategies;


    public Composite(AuthStrategy... strategies)
    {
        this(new Seq<>(strategies));
    }


    public Composite(Iterable<AuthStrategy> strategies)
    {
        mStrategies = strategies;
    }


    @Override
    public AuthState authState(AuthState fallback)
    {
        AuthState authState = fallback;
        for (AuthStrategy authStrategy : mStrategies)
        {
            authState = authStrategy.authState(authState);
        }
        return authState;
    }
}
