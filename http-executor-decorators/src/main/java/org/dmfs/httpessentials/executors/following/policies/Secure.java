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

package org.dmfs.httpessentials.executors.following.policies;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.TooManyRedirectsException;
import org.dmfs.httpessentials.executors.following.RedirectPolicy;

import java.io.IOException;
import java.net.URI;


/**
 * A {@link RedirectPolicy} decorator that allows following redirects for only https addresses. (Source and destination both need to be https urls.)
 * <p>
 * Any insecure redirect will throw a {@link RedirectionException}.
 *
 * @author Gabor Keszthelyi
 */
public final class Secure implements RedirectPolicy
{

    private static final String HTTPS_SCHEME = "https";

    private final RedirectPolicy mDecoratedPolicy;


    public Secure(RedirectPolicy decoratedPolicy)
    {
        mDecoratedPolicy = decoratedPolicy;
    }


    @Override
    public boolean affects(HttpResponse response) throws IOException
    {
        return mDecoratedPolicy.affects(response);
    }


    @Override
    public URI location(HttpResponse response, int redirectNumber) throws RedirectionException, TooManyRedirectsException, IOException
    {
        URI redirectingLocation = response.requestUri();
        URI newLocation = mDecoratedPolicy.location(response, redirectNumber);

        if (!isHttps(redirectingLocation) || !isHttps(newLocation))
        {
            throw new RedirectionException(response.status(), "Not secure (HTTPS) redirect.", redirectingLocation,
                    newLocation);
        }

        return newLocation;
    }


    private boolean isHttps(URI uri)
    {
        return HTTPS_SCHEME.equals(uri.getScheme());
    }
}
