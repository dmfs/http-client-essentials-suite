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

package org.dmfs.httpessentials.executors.authenticating.authcaches;

import org.dmfs.httpessentials.executors.authenticating.AuthCache;
import org.dmfs.httpessentials.executors.authenticating.AuthStrategy;
import org.dmfs.httpessentials.executors.authenticating.strategies.PassThroughStrategy;
import org.dmfs.optional.adapters.MapEntry;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link AuthCache}.
 *
 * @author Marten Gajda
 */
public final class SimpleAuthCache implements AuthCache
{
    private final Map<URI, AuthStrategy> mMap = new HashMap<>();


    @Override
    public AuthStrategy authStrategy(URI uri)
    {
        return new MapEntry<>(mMap, uri).value(new PassThroughStrategy());
    }


    @Override
    public void update(URI uri, AuthStrategy strategy)
    {
        mMap.put(uri, strategy);
    }
}