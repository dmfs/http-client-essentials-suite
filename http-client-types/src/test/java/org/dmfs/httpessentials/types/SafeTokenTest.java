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

import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link SafeToken}.
 *
 * @author Marten Gajda
 */
public class SafeTokenTest
{

    @Test
    public void testCharacterReplacements()
    {

        for (int i = 0; i < 0x0ffff; ++i)
        {
            if ("!#$%&'*+-.^_`|~1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(i) < 0)
            {
                assertThat(new SafeToken(new String(new char[] { (char) i })), hasToString("_"));
            }
            else
            {
                assertThat(new SafeToken(new String(new char[] { (char) i })), hasToString(new String(new char[] { (char) i })));
            }
        }

        // also test a few CharSequences

        assertThat(new SafeToken("!#$%&'*+-.^_`|~1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
                hasToString("!#$%&'*+-.^_`|~1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertThat(new SafeToken("(),/:;<=>?@[\\]{}"), hasToString("________________"));
        assertThat(new SafeToken("a "), hasToString("a_"));
        assertThat(new SafeToken(" 2"), hasToString("_2"));
        assertThat(new SafeToken("  "), hasToString("__"));
        assertThat(new SafeToken("  sdf  234 f "), hasToString("__sdf__234_f_"));

    }

}