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
 * Unit test for {@link SafeToken}.
 *
 * @author Gabor Keszthelyi
 */
public class SafeTokenTest
{

    @Test
    public void testCharacterReplacements()
    {
        assertEquals("a", new SafeToken("a").toString());
        assertEquals("!#$%&'*+-.^_`|~1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
                new SafeToken("!#$%&'*+-.^_`|~1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ").toString());

        // Delimiters are replaced
        assertEquals("________________", new SafeToken("(),/:;<=>?@[\\]{}").toString());
        assertEquals("_", new SafeToken("(").toString());
        assertEquals("_", new SafeToken(")").toString());
        assertEquals("_", new SafeToken(",").toString());
        assertEquals("_", new SafeToken("/").toString());
        assertEquals("_", new SafeToken(":").toString());
        assertEquals("_", new SafeToken(";").toString());
        assertEquals("_", new SafeToken("<").toString());
        assertEquals("_", new SafeToken("=").toString());
        assertEquals("_", new SafeToken(">").toString());
        assertEquals("_", new SafeToken("?").toString());
        assertEquals("_", new SafeToken("@").toString());
        assertEquals("_", new SafeToken("[").toString());
        assertEquals("_", new SafeToken("\\").toString());
        assertEquals("_", new SafeToken("]").toString());
        assertEquals("_", new SafeToken("{").toString());
        assertEquals("_", new SafeToken("}").toString());

        // Space is not allowed, so it gets replaced:
        assertEquals("_", new SafeToken(" ").toString());
        assertEquals("a_", new SafeToken("a ").toString());
        assertEquals("_2", new SafeToken(" 2").toString());
        assertEquals("__", new SafeToken("  ").toString());
        assertEquals("__sdf__234_f_", new SafeToken("  sdf  234 f ").toString());
    }

}