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

package org.dmfs.httpessentials.executors.following;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.*;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.RedirectionLoopException;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.optional.elementary.Absent;
import org.dmfs.jems.optional.elementary.Present;
import org.dmfs.jems.single.combined.Backed;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.dmfs.httpessentials.HttpStatus.*;
import static org.dmfs.jems.optional.elementary.Absent.absent;


/**
 * A {@link HttpRequestExecutor} decorator that follows redirects according to the provided {@link RedirectStrategy}.
 * <p>
 * (It also detects redirect loops, throws {@link RedirectionLoopException} when finds one.)
 */
public final class Following implements HttpRequestExecutor
{

    private final HttpRequestExecutor mDelegate;
    private final RedirectStrategy mRedirectStrategy;


    /**
     * Adapter to old {@link RedirectPolicy}.
     */
    public Following(HttpRequestExecutor delegate, RedirectPolicy redirectPolicy)
    {
        this((response, redirectNumber) -> {
                if (!(MOVED_PERMANENTLY.equals(response.status())
                    || FOUND.equals(response.status())
                    || SEE_OTHER.equals(response.status())
                    || TEMPORARY_REDIRECT.equals(response.status())
                    || PERMANENT_REDIRECT.equals(response.status())))
                {
                    return absent();
                }
                if (!redirectPolicy.affects(response))
                {
                    return new Absent<>();
                }
                try
                {
                    return new Present<>(redirectPolicy.location(response, redirectNumber));
                }
                catch (RedirectionException e)
                {
                    return new Absent<>();
                }
            },
            delegate);
    }


    public Following(RedirectStrategy redirectStrategy, HttpRequestExecutor delegate)
    {
        mDelegate = delegate;
        mRedirectStrategy = redirectStrategy;
    }


    @Override
    public <T> T execute(URI uri, final HttpRequest<T> request) throws ProtocolException, ProtocolError, IOException
    {
        return mDelegate.execute(uri, new FollowingRequest<>(request, uri));
    }


    private final class FollowingRequest<T> implements HttpRequest<T>
    {

        private final HttpRequest<T> mOriginalRequest;
        private final URI mOriginalRequestUri;
        private final Set<URI> mRedirectHistory; // TODO Use immutable Set when ready.


        private FollowingRequest(HttpRequest<T> originalRequest, URI originalRequestUri, Set<URI> redirectHistory)
        {
            mOriginalRequest = originalRequest;
            mOriginalRequestUri = originalRequestUri;
            mRedirectHistory = redirectHistory;
        }


        private FollowingRequest(HttpRequest<T> originalRequest, URI originalRequestUri)
        {
            this(originalRequest, originalRequestUri, new HashSet<>());
        }


        @Override
        public HttpMethod method()
        {
            return mOriginalRequest.method();
        }


        @Override
        public Headers headers()
        {
            return mOriginalRequest.headers();
        }


        @Override
        public HttpRequestEntity requestEntity()
        {
            return mOriginalRequest.requestEntity();
        }


        @Override
        public HttpResponseHandler<T> responseHandler(HttpResponse response)
        {
            return new Backed<HttpResponseHandler<T>>(
                new Mapped<>(
                    newLocation -> handledResponse -> {
                        // Close InputStream to free the connection
                        handledResponse.responseEntity().contentStream().close();

                        if (!mRedirectHistory.add(newLocation))
                        {
                            throw new RedirectionLoopException(handledResponse.status(), handledResponse.requestUri(), newLocation);
                        }

                        return mDelegate.execute(newLocation,
                            new FollowingRequest<>(mOriginalRequest, mOriginalRequestUri, mRedirectHistory));
                    },
                    mRedirectStrategy.location(response, mRedirectHistory.size())),
                () -> new RequestUriOverridingResponseHandler<>(mOriginalRequest, mOriginalRequestUri)).value();
        }
    }

}
