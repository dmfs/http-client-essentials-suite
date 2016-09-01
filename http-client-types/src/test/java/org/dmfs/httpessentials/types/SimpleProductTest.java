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

import org.junit.Test;


/**
 * Unit test for {@link SimpleProduct}.
 *
 * @author Gabor Keszthelyi
 */
public class SimpleProductTest extends BaseProductTest
{

    @Test
    public void test()
    {
        SimpleProduct product = new SimpleProduct("name");

        assertProductToStringAndAppendTo(product, "name");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenNameIsNull_shouldThrowException()
    {
        new SimpleProduct((Token) null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenNameIsEmpty_shouldThrowException()
    {
        new SimpleProduct("");
    }
}
