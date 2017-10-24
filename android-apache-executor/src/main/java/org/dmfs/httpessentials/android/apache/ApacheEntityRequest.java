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

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.dmfs.httpessentials.client.HttpRequest;

import java.net.URI;


/**
 * An Apache {@link org.apache.http.HttpRequest} for requests with a body.
 *
 * @author Marten Gajda
 */
final class ApacheEntityRequest<T> extends HttpEntityEnclosingRequestBase
{
    private final HttpRequest<T> mRequest;
    private final URI mUri;


    public ApacheEntityRequest(URI uri, HttpRequest<T> request)
    {
        mRequest = request;
        mUri = uri;
    }


    @Override
    public boolean expectContinue()
    {
        return false;
    }


    @Override
    public String getMethod()
    {
        return mRequest.method().verb();
    }


    @Override
    public URI getURI()
    {
        return mUri;
    }


    @Override
    public HttpEntity getEntity()
    {
        return new ApacheHttpEntity(mRequest);
    }

}
