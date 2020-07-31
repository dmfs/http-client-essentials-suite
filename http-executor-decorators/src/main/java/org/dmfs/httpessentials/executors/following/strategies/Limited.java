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
import org.dmfs.jems.optional.decorators.Restrained;

import java.net.URI;


/**
 * A {@link RedirectStrategy} which restricts the number of redirects it follows.
 */
public final class Limited implements RedirectStrategy
{
    /**
     * The default maximum redirects per request.
     */
    private static final int DEFAULT_MAX_REDIRECTS = 5;

    private final int mMaxNumberOfRedirects;
    private final RedirectStrategy mDelegate;


    /**
     * Creates a {@link Limited} {@link RedirectStrategy} with the default redirect limit of {@value DEFAULT_MAX_REDIRECTS}.
     */
    public Limited(RedirectStrategy delegate)
    {
        this(DEFAULT_MAX_REDIRECTS, delegate);
    }


    public Limited(int maxNumberOfRedirects, RedirectStrategy delegate)
    {
        mMaxNumberOfRedirects = maxNumberOfRedirects;
        mDelegate = delegate;
    }


    @Override
    public Optional<URI> location(HttpResponse response, int redirectNumber)
    {
        return new Restrained<>(
                () -> redirectNumber <= mMaxNumberOfRedirects,
                mDelegate.location(response, redirectNumber));
    }
}
