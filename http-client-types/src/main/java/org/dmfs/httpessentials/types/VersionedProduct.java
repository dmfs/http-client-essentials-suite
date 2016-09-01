/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.types;

/**
 * A {@link Product} with name and version.
 *
 * @author Gabor Keszthelyi
 */
public final class VersionedProduct implements Product
{

    private final Product mProduct;
    private final Token mVersion;


    public VersionedProduct(Token name, Token version)
    {
        mProduct = new SimpleProduct(name);
        mVersion = Validate.notNull(version, "Version must not be null.");
    }


    public VersionedProduct(String name, String version)
    {
        this(new SafeToken(name), new SafeToken(version));
    }


    @Override
    public void appendTo(StringBuilder sb)
    {
        mProduct.appendTo(sb);
        sb.append("/");
        sb.append(mVersion);
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }
}
