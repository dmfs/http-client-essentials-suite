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

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.following.RedirectStrategy;
import org.dmfs.jems.optional.Optional;

import java.net.URI;


/**
 * A {@link RedirectStrategy} to delegate to another {@link RedirectStrategy}.
 */
public abstract class DelegatingRedirectStrategy implements RedirectStrategy
{
    private final RedirectStrategy mDelegate;


    public DelegatingRedirectStrategy(RedirectStrategy delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public final Optional<URI> location(HttpResponse response, int redirectNumber)
    {
        return mDelegate.location(response, redirectNumber);
    }
}
