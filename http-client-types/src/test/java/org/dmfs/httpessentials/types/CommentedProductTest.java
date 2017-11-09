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
 * Unit test for {@link CommentedProduct}
 *
 * @author Gabor Keszthelyi
 */
public class CommentedProductTest extends BaseProductTest
{

    @Test
    public void testWithNameAndComment()
    {
        CommentedProduct product = new CommentedProduct("name", "comment");

        assertProductToStringAndAppendTo(product, "name (comment)");
    }


    @Test
    public void testWithNameVersionAndComment()
    {
        CommentedProduct product = new CommentedProduct("name", "version", "comment");

        assertProductToStringAndAppendTo(product, "name/version (comment)");
    }


    @Test(expected = NullPointerException.class)
    public void test_whenNameIsNull_shouldThrowException()
    {
        new CommentedProduct(null, "comment");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenNameIsEmpty_shouldThrowException()
    {
        new CommentedProduct("", "comment");
    }


    @Test(expected = NullPointerException.class)
    public void test_whenVersionIsNull_shouldThrowException()
    {
        new CommentedProduct("name", null, "comment");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenVersionIsEmpty_shouldThrowException()
    {
        new CommentedProduct("name", "", "comment");
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_whenCommentIsNull_shouldThrowException()
    {
        new CommentedProduct("name", "version", null);
    }

}