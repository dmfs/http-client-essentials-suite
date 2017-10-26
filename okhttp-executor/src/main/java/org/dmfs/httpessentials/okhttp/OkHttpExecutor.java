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
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.executors.common.DelegatingHttpRequestExecutor;
import org.dmfs.httpessentials.executors.common.decorators.BottomBranded;
import org.dmfs.httpessentials.executors.common.types.Platform;
import org.dmfs.httpessentials.okhttp.okhttpclient.BaseOkHttpClient;
import org.dmfs.httpessentials.okhttp.utils.OkHttpProduct;
import org.dmfs.httpessentials.types.VersionedProduct;
import org.dmfs.jems.single.Single;


/**
 * An {@link HttpRequestExecutor} based on OkHttp.
 *
 * @author Marten Gajda
 */
public final class OkHttpExecutor extends DelegatingHttpRequestExecutor
{
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
        super(new BottomBranded(
                new VersionedProduct(BuildConfig.NAME, BuildConfig.VERSION),
                new BottomBranded(
                        new OkHttpProduct(),
                        new BottomBranded(Platform.INSTANCE, executor)
                )
        ));
    }
}
