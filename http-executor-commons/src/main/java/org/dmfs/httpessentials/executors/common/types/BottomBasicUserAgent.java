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

package org.dmfs.httpessentials.executors.common.types;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.types.BasicUserAgent;
import org.dmfs.httpessentials.types.Product;
import org.dmfs.httpessentials.types.UserAgent;
import org.dmfs.iterators.SingletonIterator;
import org.dmfs.iterators.decorators.Serialized;

import java.util.Iterator;


/**
 * A special {@link UserAgent} that appends the {@link Product} provided in the constructor to the end of the products (and header value string), instead of
 * appending to the beginning as {@link BasicUserAgent} does.
 * <p>
 * It can be used in the actual {@link HttpRequestExecutor} implementations (i.e. the ones that provide the underlying http layer using an actual low level http
 * library) to add their name and the platform description to the user-agent header.
 *
 * @author Gabor Keszthelyi
 */
public final class BottomBasicUserAgent implements UserAgent
{

    private final UserAgent mOriginalUserAgent;
    private final Product mProductToAppend;


    public BottomBasicUserAgent(UserAgent originalUserAgent, Product productToAppend)
    {
        mOriginalUserAgent = originalUserAgent;
        mProductToAppend = productToAppend;
    }


    @Override
    public UserAgent withProduct(Product product)
    {
        return new BasicUserAgent(this, product);
    }


    @Override
    public Iterator<Product> iterator()
    {
        return new Serialized<>(mOriginalUserAgent.iterator(),
                new SingletonIterator<>(mProductToAppend));
    }


    @Override
    public void appendTo(StringBuilder sb)
    {
        mOriginalUserAgent.appendTo(sb);
        sb.append(" ");
        mProductToAppend.appendTo(sb);
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }
}
