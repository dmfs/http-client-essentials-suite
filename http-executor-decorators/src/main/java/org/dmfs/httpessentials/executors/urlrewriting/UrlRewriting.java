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

package org.dmfs.httpessentials.executors.urlrewriting;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;

import java.io.IOException;
import java.net.URI;


/**
 * An {@link HttpRequestExecutor} decorator that rewrites requested URLs as per the given {@link RewritePolicy}.
 *
 * @author Marten Gajda
 */
public final class UrlRewriting implements HttpRequestExecutor
{
    private final HttpRequestExecutor mDelegate;
    private final RewritePolicy mRewritePolicy;


    /**
     * Creates an {@link HttpRequestExecutor} that rewrites request URLs.
     *
     * @param delegate
     *         The decorated {@link HttpRequestExecutor}.
     * @param rewritePolicy
     *         A {@link RewritePolicy} that determines how URLs have to be rewritten.
     */
    public UrlRewriting(HttpRequestExecutor delegate, RewritePolicy rewritePolicy)
    {
        mDelegate = delegate;
        mRewritePolicy = rewritePolicy;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException
    {
        return mDelegate.execute(mRewritePolicy.rewritten(uri, request), request);
    }
}
