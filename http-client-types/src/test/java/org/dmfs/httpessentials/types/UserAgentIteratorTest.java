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

package org.dmfs.httpessentials.types;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Test for the iterators of {@link UserAgent} types.
 *
 * @author Gabor Keszthelyi
 */
public class UserAgentIteratorTest
{

    @Test
    public void testSingleProduct()
    {
        Product p1 = new SimpleProduct("p1");

        UserAgent userAgent = new SingletonUserAgent(p1);

        testProductsIterate(userAgent, p1);
    }


    @Test
    public void testFourProducts()
    {
        SimpleProduct p1 = new SimpleProduct("p1");
        SimpleProduct p2 = new SimpleProduct("p2");
        SimpleProduct p3 = new SimpleProduct("p3");
        SimpleProduct p4 = new SimpleProduct("p4");

        UserAgent userAgent = new SingletonUserAgent(p1).withProduct(p2).withProduct(p3).withProduct(p4);

        testProductsIterate(userAgent, p4, p3, p2, p1);
    }


    @Test
    public void testUsingBasicProductsConstructorDirectly()
    {
        SimpleProduct p1 = new SimpleProduct("p1");
        SimpleProduct p2 = new SimpleProduct("p2");

        UserAgent userAgent = new BasicUserAgent(new SingletonUserAgent(p1), p2);

        testProductsIterate(userAgent, p2, p1);
    }


    private void testProductsIterate(UserAgent userAgent, Product... expectedContainedProducts)
    {
        List<Product> actualContainedProducts = new ArrayList<>();
        for (Product product : userAgent)
        {
            actualContainedProducts.add(product);
        }

        assertEquals(Arrays.asList(expectedContainedProducts), actualContainedProducts);
    }
}
