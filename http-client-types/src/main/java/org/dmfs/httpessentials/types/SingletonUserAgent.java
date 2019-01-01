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

import org.dmfs.iterators.SingletonIterator;

import java.util.Iterator;


/**
 * A {@link UserAgent} that contains only one {@link Product}.
 *
 * @author Gabor Keszthelyi
 */
public final class SingletonUserAgent implements UserAgent
{

    private final Product mProduct;


    public SingletonUserAgent(Product product)
    {
        mProduct = product;
    }


    @Override
    public UserAgent withProduct(Product product)
    {
        return new BasicUserAgent(this, product);
    }


    @Override
    public void appendTo(StringBuilder sb)
    {
        mProduct.appendTo(sb);
    }


    @Override
    public Iterator<Product> iterator()
    {
        return new SingletonIterator<>(mProduct);
    }


    @Override
    public String toString()
    {
        return mProduct.toString();
    }
}
