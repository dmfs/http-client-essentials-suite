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

package org.dmfs.httpessentials.executors.authorizing.strategies;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authorizing.AuthState;
import org.dmfs.httpessentials.executors.authorizing.AuthStrategy;

import java.net.URI;


/**
 * An {@link AuthStrategy} which doesn't know how to authenticate. It just proceeds with the fallback {@link AuthState}.
 *
 * @author Marten Gajda
 */
public final class PassThroughStrategy implements AuthStrategy
{
    @Override
    public AuthState authState(HttpMethod method, URI uri, AuthState fallback)
    {
        return fallback;
    }
}
