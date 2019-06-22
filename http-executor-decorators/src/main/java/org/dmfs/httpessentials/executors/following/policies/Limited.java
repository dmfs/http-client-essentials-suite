/*
 * Copyright 2019 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following.policies;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.TooManyRedirectsException;
import org.dmfs.httpessentials.executors.following.RedirectPolicy;

import java.net.URI;


/**
 * A {@link RedirectPolicy} which restricts the number of redirects it follows.
 *
 * @author Marten Gajda
 */
public final class Limited implements RedirectPolicy
{
    /**
     * The default maximum redirects per request.
     */
    public static final int DEFAULT_MAX_REDIRECTS = 5;

    private final int mMaxNumberOfRedirects;
    private final RedirectPolicy mDelegate;


    /**
     * Creates a {@link Limited} with the default redirect limit of {@value DEFAULT_MAX_REDIRECTS}.
     *
     * @param delegate
     */
    public Limited(RedirectPolicy delegate)
    {
        this(DEFAULT_MAX_REDIRECTS, delegate);
    }


    public Limited(int maxNumberOfRedirects, RedirectPolicy delegate)
    {
        mMaxNumberOfRedirects = maxNumberOfRedirects;
        mDelegate = delegate;
    }


    @Override
    public boolean affects(HttpResponse response)
    {
        return mDelegate.affects(response);
    }


    @Override
    public URI location(HttpResponse response, int redirectNumber) throws RedirectionException
    {
        URI newLocation = mDelegate.location(response, redirectNumber);
        if (redirectNumber > mMaxNumberOfRedirects)
        {
            throw new TooManyRedirectsException(response.status(), redirectNumber, response.requestUri(), newLocation);
        }
        return newLocation;
    }

}
