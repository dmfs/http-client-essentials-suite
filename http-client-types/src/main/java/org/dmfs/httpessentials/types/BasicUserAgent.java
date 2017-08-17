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

package org.dmfs.httpessentials.types;

import org.dmfs.iterators.SerialIterator;
import org.dmfs.iterators.SingletonIterator;

import java.util.Iterator;


/**
 * The basic decorator implementation of {@link UserAgent}, it can add a new {@link Product} to a given original {@link UserAgent}.
 *
 * @author Gabor Keszthelyi
 */
public final class BasicUserAgent implements UserAgent
{

    private final UserAgent mOriginalUserAgent;
    private final Product mProductToAppend;


    public BasicUserAgent(UserAgent originalUserAgent, Product productToAppend)
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
        return new SerialIterator<Product>(new SingletonIterator<Product>(mProductToAppend),
                mOriginalUserAgent.iterator());
    }


    @Override
    public void appendTo(StringBuilder sb)
    {
        mProductToAppend.appendTo(sb);
        sb.append(" ");
        mOriginalUserAgent.appendTo(sb);
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }
}
