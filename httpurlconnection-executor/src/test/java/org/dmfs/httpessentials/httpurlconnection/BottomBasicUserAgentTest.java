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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.types.Product;
import org.dmfs.httpessentials.types.SimpleProduct;
import org.dmfs.httpessentials.types.SingletonUserAgent;
import org.dmfs.httpessentials.types.UserAgent;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * Functional test for {@link BottomBasicUserAgent}.
 *
 * @author Gabor Keszthelyi
 */
public class BottomBasicUserAgentTest
{

    @Test
    public void testAppendingOrderWorksCorrectly()
    {
        // ARRANGE
        UserAgent originalUserAgent = new SingletonUserAgent(new SimpleProduct("original1")).withProduct(
                new SimpleProduct("original2"));
        Product productToAppend = new SimpleProduct("bottom-product");

        // ACT
        UserAgent userAgent1 = new BottomBasicUserAgent(originalUserAgent, productToAppend);
        UserAgent userAgent2 = userAgent1.withProduct(new SimpleProduct("later-appended"));

        // ASSERT
        assertEquals("original2 original1 bottom-product", userAgent1.toString());
        Iterator<Product> iterator1 = userAgent1.iterator();
        assertEquals("original2", iterator1.next().toString());
        assertEquals("original1", iterator1.next().toString());
        assertEquals("bottom-product", iterator1.next().toString());
        assertFalse(iterator1.hasNext());

        assertEquals("later-appended original2 original1 bottom-product", userAgent2.toString());
        Iterator<Product> iterator2 = userAgent2.iterator();
        assertEquals("later-appended", iterator2.next().toString());
        assertEquals("original2", iterator2.next().toString());
        assertEquals("original1", iterator2.next().toString());
        assertEquals("bottom-product", iterator2.next().toString());
        assertFalse(iterator2.hasNext());
    }

}