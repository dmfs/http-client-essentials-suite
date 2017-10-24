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

package org.dmfs.httpessentials.android.apache;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.headers.Headers;

import java.net.URI;


/**
 * The {@link HttpResponse} of an Apache {@link org.apache.http.HttpResponse}.
 *
 * @author Marten Gajda
 */
final class EssentialsResponse implements HttpResponse
{
    private final URI mUri;
    private final org.apache.http.HttpResponse mResponse;


    public EssentialsResponse(URI uri, org.apache.http.HttpResponse httpResponse)
    {
        mUri = uri;
        mResponse = httpResponse;
    }


    @Override
    public HttpStatus status()
    {
        return new ApacheHttpStatus(mResponse.getStatusLine());
    }


    @Override
    public Headers headers()
    {
        return new ApacheHeaders(mResponse);
    }


    @Override
    public HttpResponseEntity responseEntity()
    {
        return new EssentialsHttpResponseEntity(mResponse);
    }


    @Override
    public URI requestUri()
    {
        return mUri;
    }


    @Override
    public URI responseUri()
    {
        return mUri;
    }

}
