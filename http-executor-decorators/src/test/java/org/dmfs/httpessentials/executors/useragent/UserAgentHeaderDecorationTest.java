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

package org.dmfs.httpessentials.executors.useragent;

import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.SingletonHeaders;
import org.dmfs.httpessentials.types.CommentedProduct;
import org.dmfs.httpessentials.types.Product;
import org.dmfs.httpessentials.types.SimpleProduct;
import org.dmfs.httpessentials.types.SingletonUserAgent;
import org.dmfs.httpessentials.types.UserAgent;
import org.dmfs.httpessentials.types.VersionedProduct;
import org.junit.Test;

import static org.dmfs.httpessentials.headers.HttpHeaders.USER_AGENT_HEADER;
import static org.junit.Assert.assertEquals;


/**
 * Functional test for {@link UserAgentHeaderDecoration}.
 *
 * @author Gabor Keszthelyi
 */
public class UserAgentHeaderDecorationTest
{

    private UserAgentHeaderDecoration agentDecoration;


    @Test
    public void testDecorate_NoOriginalProduct()
    {
        Product product = new CommentedProduct("name", "ver", "comment");

        Headers headers = EmptyHeaders.INSTANCE;

        String expectedHeaderString = "name/ver (comment)";

        testDecorate(product, headers, expectedHeaderString);
    }


    @Test
    public void testDecorate_OneOriginalProduct()
    {
        Product productToAdd = new VersionedProduct("name", "ver");

        Product originalProduct = new CommentedProduct("Godzilla", "2.2", "cool");
        Headers headers = new SingletonHeaders(USER_AGENT_HEADER.entity(new SingletonUserAgent(originalProduct)));

        String expectedHeaderString = "name/ver Godzilla/2.2 (cool)";

        testDecorate(productToAdd, headers, expectedHeaderString);
    }


    @Test
    public void testDecorate_MultipleProductsAlready()
    {
        Product productToAdd = new CommentedProduct("name", "ver", "comment");

        Product originalProduct1 = new CommentedProduct("prod1", "9.9.9", "cool");
        Product originalProduct2 = new SimpleProduct("prod2");
        Product originalProduct3 = new VersionedProduct("prod3", "2.2");
        UserAgent originalUserAgent = new SingletonUserAgent(originalProduct1).withProduct(originalProduct2)
                .withProduct(originalProduct3);
        Headers headers = new SingletonHeaders(USER_AGENT_HEADER.entity(originalUserAgent));

        String expectedHeaderString = "name/ver (comment) prod3/2.2 prod2 prod1/9.9.9 (cool)";

        testDecorate(productToAdd, headers, expectedHeaderString);
    }


    private void testDecorate(Product productToAdd, Headers originalHeaders, String expectedHeaderString)
    {
        agentDecoration = new UserAgentHeaderDecoration(productToAdd);
        Headers newHeaders = agentDecoration.decorated(originalHeaders);
        Header<UserAgent> userAgentHeader = newHeaders.header(USER_AGENT_HEADER);
        assertEquals(expectedHeaderString, userAgentHeader.toString());
    }

}