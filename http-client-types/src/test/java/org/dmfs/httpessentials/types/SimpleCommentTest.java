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

import static org.junit.Assert.assertEquals;


/**
 * Unit test for {@link SimpleComment}.
 *
 * @author Gabor Keszthelyi
 */
public class SimpleCommentTest
{

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_whenNullInput_shouldThrowException()
    {
        new SimpleComment(null);
    }


    @Test
    public void testCharReplacementsWithVariousVariations()
    {
        assertInputAndToString("", "()");
        assertInputAndToString(" ", "( )");
        assertInputAndToString("a", "(a)");

        // Almost all special characters are allowed:
        assertInputAndToString("aB2340 delimiters:,/:;<=>?@[]{} tokenchars:azAZ09^_`|~!#$%&'*+.-",
                "(aB2340 delimiters:,/:;<=>?@[]{} tokenchars:azAZ09^_`|~!#$%&'*+.-)");

        // Not allowed and non-escapable chars get replaced with "_":
        assertInputAndToString("\u0008", "(_)");

        assertInputAndToString("a\u0007", "(a_)");
        assertInputAndToString("\u0007b", "(_b)");
        assertInputAndToString("a\u0007\u0007b", "(a__b)");
        assertInputAndToString("a\u0007\u0007\u0007b", "(a___b)");

        assertInputAndToString("c\u0007\u007Fd", "(c__d)");
        assertInputAndToString("f\u0006g\u0005h", "(f_g_h)");

        // '\', '(', ')' get escaped with '\':
        assertInputAndToString("\\", "(\\\\)");
        assertInputAndToString("a\\", "(a\\\\)");
        assertInputAndToString("\\a", "(\\\\a)");
        assertInputAndToString(" \\ ", "( \\\\ )");
        assertInputAndToString("c\\d\\e", "(c\\\\d\\\\e)");

        assertInputAndToString("(", "(\\()");
        assertInputAndToString("a(", "(a\\()");
        assertInputAndToString("(a", "(\\(a)");
        assertInputAndToString(" ( ", "( \\( )");
        assertInputAndToString("c(d(e", "(c\\(d\\(e)");

        assertInputAndToString(")", "(\\))");
        assertInputAndToString("a)", "(a\\))");
        assertInputAndToString(")a", "(\\)a)");
        assertInputAndToString(" ) ", "( \\) )");
        assertInputAndToString("c)d)e", "(c\\)d\\)e)");

        assertInputAndToString("\\()cde\\\\", "(\\\\\\(\\)cde\\\\\\\\)");
    }


    private void assertInputAndToString(String input, String expectedToString)
    {
        assertEquals(expectedToString, new SimpleComment(input).toString());
    }

}