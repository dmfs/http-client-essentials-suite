/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.httpurlconnection.executor_utils.decoration;

import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.httpessentials.types.SimpleProduct;
import org.dmfs.httpessentials.types.SingletonUserAgent;
import org.dmfs.httpessentials.types.UserAgent;
import org.junit.Test;

import static org.dmfs.httpessentials.headers.HttpHeaders.USER_AGENT_HEADER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


/**
 * Functional test for {@link BottomUserAgentHeaderDecoration}.
 *
 * @author Gabor Keszthelyi
 */
public class BottomUserAgentHeaderDecorationTest
{

    @Test
    public void testDecoration_whenNoUserAgentInOriginal_shouldReturnTheSame()
    {
        // ARRANGE
        Decoration<Headers> decoration = new BottomUserAgentHeaderDecoration(new SimpleProduct("bottom-product"));
        Headers original = EmptyHeaders.INSTANCE;

        // ACT
        Headers decorated = decoration.decorated(original);

        // ASSERT
        assertSame(original, decorated);
    }


    @Test
    public void testDecoration_whenUserAgentIsPresentInOriginal_shouldAppendToTheEnd()
    {
        // ARRANGE
        Decoration<Headers> decoration = new BottomUserAgentHeaderDecoration(new SimpleProduct("bottom-product"));
        UserAgent originalUserAgent = new SingletonUserAgent(new SimpleProduct("first")).withProduct(
                new SimpleProduct("second"));
        Headers original = EmptyHeaders.INSTANCE.withHeader(HttpHeaders.USER_AGENT_HEADER.entity(originalUserAgent));

        // ACT
        Headers decorated = decoration.decorated(original);

        // ASSERT
        UserAgent newUserAgent = decorated.header(USER_AGENT_HEADER).value();
        assertEquals("second first bottom-product", newUserAgent.toString());
    }


    @Test
    public void testDecorate_multipleDecorations()
    {
        // ARRANGE
        Decoration<Headers> bottomDecoration1 = new BottomUserAgentHeaderDecoration(
                new SimpleProduct("bottom-product1"));
        Decoration<Headers> bottomDecoration2 = new BottomUserAgentHeaderDecoration(
                new SimpleProduct("bottom-product2"));
        UserAgent originalUserAgent = new SingletonUserAgent(new SimpleProduct("first")).withProduct(
                new SimpleProduct("second"));
        Headers original = EmptyHeaders.INSTANCE.withHeader(HttpHeaders.USER_AGENT_HEADER.entity(originalUserAgent));

        // ACT
        Headers decorated = bottomDecoration2.decorated(bottomDecoration1.decorated(original));

        // ASSERT
        UserAgent newUserAgent = decorated.header(USER_AGENT_HEADER).value();
        assertEquals("second first bottom-product1 bottom-product2", newUserAgent.toString());
    }

}