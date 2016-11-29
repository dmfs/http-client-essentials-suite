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

package org.dmfs.httpessentials.executors.useragent;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.decoration.HeaderDecorated;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.types.Product;

import java.io.IOException;
import java.net.URI;


/**
 * {@link HttpRequestExecutor} decorator that adds the given {@link Product} to the User-Agent header of the request.
 *
 * @author Gabor Keszthelyi
 */
public final class Branded implements HttpRequestExecutor
{

    private final HttpRequestExecutor mDecoratedExecutor;
    private final Product mProduct;


    public Branded(HttpRequestExecutor decoratedExecutor, Product product)
    {
        mDecoratedExecutor = decoratedExecutor;
        mProduct = product;
    }


    @Override
    public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
    {
        return mDecoratedExecutor.execute(uri,
                new HeaderDecorated<T>(request, new UserAgentHeaderDecoration(mProduct)));
    }

}
