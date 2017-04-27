/*
 * Copyright 2016 dmfs GmbH
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
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionLoopException;
import org.dmfs.httpessentials.headers.Headers;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;


/**
 * A {@link HttpRequestExecutor} decorator that follows redirects according to the provided {@link RedirectPolicy}.
 * <p>
 * (It also detects redirect loops, throws {@link RedirectionLoopException} when finds one.)
 *
 * @author Gabor Keszthelyi
 */
public final class Following implements HttpRequestExecutor
{

    private final HttpRequestExecutor mDecoratedExecutor;
    private final RedirectPolicy mRedirectPolicy;


    public Following(HttpRequestExecutor decoratedExecutor, RedirectPolicy redirectPolicy)
    {
        mDecoratedExecutor = decoratedExecutor;
        mRedirectPolicy = redirectPolicy;
    }


    @Override
    public <T> T execute(URI uri, final HttpRequest<T> request) throws ProtocolException, ProtocolError, IOException
    {
        return mDecoratedExecutor.execute(uri, new FollowingRequest<T>(request, uri));
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
            this(originalRequest, originalRequestUri,
                    null); // Lazy init of redirect history Set in {@link #redirectHistory}.
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
        public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
        {
            if (response.status().isRedirect() && mRedirectPolicy.affects(response))
            {
                return new RedirectResponseHandler();
            }

            if (mRedirectHistory != null)
            {
                // 'Resetting' {@link HttpResponse#requestUri()} to the original one after the redirects.
                return new RequestUriOverridingResponseHandler<>(mOriginalRequest, mOriginalRequestUri);
            }

            return mOriginalRequest.responseHandler(response);
        }


        private T handleRedirectResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
        {
            // Close InputStream to free the connection
            response.responseEntity().contentStream().close();

            Set<URI> redirectHistory = redirectHistory();

            URI newLocation = mRedirectPolicy.location(response, redirectHistory.size() + 1);

            if (redirectHistory.contains(newLocation))
            {
                throw new RedirectionLoopException(response.status(), response.requestUri(), newLocation);
            }

            redirectHistory.add(newLocation);
            return mDecoratedExecutor.execute(newLocation,
                    new FollowingRequest<>(mOriginalRequest, mOriginalRequestUri, redirectHistory));
        }


        private Set<URI> redirectHistory()
        {
            if (mRedirectHistory == null)
            {
                return new HashSet<URI>();
            }
            return mRedirectHistory;
        }


        private class RedirectResponseHandler implements HttpResponseHandler<T>
        {
            @Override
            public T handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
            {
                return handleRedirectResponse(response);
            }
        }
    }

}
