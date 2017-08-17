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

import org.junit.Test;


/**
 * Unit test for {@link VersionedProduct}.
 *
 * @author Gabor Keszthelyi
 */
public class VersionedProductTest extends BaseProductTest
{

    @Test
    public void test_happy()
    {
        VersionedProduct product = new VersionedProduct("name", "version");

        assertProductToStringAndAppendTo(product, "name/version");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenNameIsNull_shouldThrowException()
    {
        new VersionedProduct(null, "version");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenNameIsEmpty_shouldThrowException()
    {
        new VersionedProduct("", "version");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenVersionIsNull_shouldThrowException()
    {
        new VersionedProduct("name", null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenVersionIsEmpty_shouldThrowException()
    {
        new VersionedProduct("name", "");
    }

}