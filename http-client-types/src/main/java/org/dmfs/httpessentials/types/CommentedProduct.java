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
 * A {@link Product} with name, and comment or with name, version, and comment.
 *
 * @author Gabor Keszthelyi
 */
public final class CommentedProduct implements Product
{

    private final Product mProduct;
    private final Comment mComment;


    public CommentedProduct(Token name, Token version, Comment comment)
    {
        this(new VersionedProduct(name, version), comment);
    }


    public CommentedProduct(String name, String version, String comment)
    {
        this(new VersionedProduct(new SafeToken(name), new SafeToken(version)), new SimpleComment(comment));
    }


    public CommentedProduct(Token name, Comment comment)
    {
        this(new SimpleProduct(name), comment);
    }


    public CommentedProduct(String name, String comment)
    {
        // TODO Okay to use SafeToken and SimpleComment as defaults? Or remove constructors with string params?
        this(new SimpleProduct(new SafeToken(name)), new SimpleComment(comment));
    }


    private CommentedProduct(Product product, Comment comment)
    {
        mProduct = product;
        mComment = Validate.notNull(comment, "Comment must not be null.");
    }


    @Override
    public Token name()
    {
        return mProduct.name();
    }


    @Override
    public Token version()
    {
        return mProduct.version();
    }


    @Override
    public Comment comment()
    {
        return mComment;
    }


    @Override
    public void appendTo(StringBuilder sb)
    {
        mProduct.appendTo(sb);
        sb.append(" ");
        sb.append(mComment);
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }
}
