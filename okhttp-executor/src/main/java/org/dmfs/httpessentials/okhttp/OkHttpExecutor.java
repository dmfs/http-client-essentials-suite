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

package org.dmfs.httpessentials.okhttp;

import okhttp3.OkHttpClient;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.httpurlconnection.utils.executors.BottomBranded;
import org.dmfs.httpessentials.httpurlconnection.utils.types.Platform;
import org.dmfs.httpessentials.okhttp.okhttpclient.BaseOkHttpClient;
import org.dmfs.httpessentials.okhttp.utils.OkHttpProduct;
import org.dmfs.httpessentials.types.VersionedProduct;
import org.dmfs.jems.single.Single;

import java.io.IOException;
import java.net.URI;


/**
 * A {@link HttpRequestExecutor} based on OkHttp.
 *
 * @author Marten Gajda
 */
public final class OkHttpExecutor implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegate;


    public OkHttpExecutor()
    {
        this(new BaseOkHttpClient());
    }


    public OkHttpExecutor(Single<OkHttpClient> okHttpClient)
    {
        this(new PlainOkHttpExecutor(okHttpClient));
    }


    private OkHttpExecutor(HttpRequestExecutor executor)
    {
        mDelegate = new BottomBranded(
                new BottomBranded(
                        new BottomBranded(executor, Platform.INSTANCE),
                        new OkHttpProduct()),
                new VersionedProduct(BuildConfig.NAME, BuildConfig.VERSION));
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mDelegate.execute(uri, request);
    }
}
