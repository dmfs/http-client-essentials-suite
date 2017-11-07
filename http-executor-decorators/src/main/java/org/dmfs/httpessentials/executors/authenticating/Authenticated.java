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

package org.dmfs.httpessentials.executors.authenticating;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.executors.authenticating.challenges.ResponseChallenges;
import org.dmfs.httpessentials.executors.authenticating.charsequences.SingleCredentials;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.HeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.typedentity.EntityConverter;
import org.dmfs.iterators.Function;
import org.dmfs.optional.decorators.Mapped;

import java.io.IOException;
import java.net.URI;


/**
 * An authenticated {@link HttpRequest}. It takes an {@link AuthState} and adds any {@link Authorization} it returns to the request. "401" responses will be
 * handled by an {@link AuthenticatingResponseHandler}.
 *
 * @author Marten Gajda
 */
final class Authenticated<T> implements HttpRequest<T>
{
    /**
     * HeaderType for the AUTHORIZATION header. Private for now.
     */
    private final static HeaderType<Authorization> AUTHORIZATION = new BasicSingletonHeaderType<>("Authorization", new EntityConverter<Authorization>()
    {
        @Override
        public Authorization value(String valueString)
        {
            throw new UnsupportedOperationException("Parsing Credentials is not supported yet.");
        }


        @Override
        public String valueString(Authorization authorization)
        {
            return new SingleCredentials(authorization).value().toString();
        }
    });

    private final HttpRequestExecutor mExecutor;
    private final AuthState mAuthState;
    private final URI mUri;
    private final HttpRequest<T> mRequest;


    Authenticated(HttpRequestExecutor executor, URI uri, HttpRequest<T> request, AuthState authState)
    {
        mExecutor = executor;
        mAuthState = authState;
        mUri = uri;
        mRequest = request;
    }


    @Override
    public HttpMethod method()
    {
        return mRequest.method();
    }


    @Override
    public Headers headers()
    {
        final Headers originalHeaders = mRequest.headers();

        // add an Authorization header if we have credentials and there is none yet
        return originalHeaders.contains(AUTHORIZATION)
                ? originalHeaders
                : new Mapped<>(
                new Function<Authorization, Headers>()
                {
                    @Override
                    public Headers apply(Authorization authorization)
                    {
                        return originalHeaders.withHeader(AUTHORIZATION.entity(authorization));
                    }
                }, mAuthState.authorization()).value(originalHeaders);
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return mRequest.requestEntity();
    }


    @Override
    public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        if (HttpStatus.UNAUTHORIZED.equals(response.status()) && !mRequest.headers().contains(AUTHORIZATION))
        {
            // authentication failed, return a response handler which authenticates and re-sends the request
            return new AuthenticatingResponseHandler<>(
                    mExecutor,
                    mUri,
                    mRequest,
                    mAuthState.withChallenges(new ResponseChallenges(response))
            );
        }
        return mRequest.responseHandler(response);
    }
}
