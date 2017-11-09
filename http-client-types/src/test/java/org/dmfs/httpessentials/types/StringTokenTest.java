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
import static org.junit.Assert.fail;


/**
 * Unit test for {@link StringToken}.
 *
 * @author Gabor Keszthelyi
 */
public class StringTokenTest
{
    /*
        https://tools.ietf.org/html/rfc7230#section-3.2.6

        Delimiters are chosen from the set of US-ASCII visual characters not allowed in a token
        (DQUOTE and "(),/:;<=>?@[\]{}").

        token  = 1*tchar
        tchar  = "!" / "#" / "$" / "%" / "&" / "'" / "*"
            / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
            / DIGIT / ALPHA
            ; any VCHAR, except delimiters
    */


    @Test
    public void testValidationWithVariousValues()
    {
        // Must have at least one character
        assertInvalid("");

        for (int i = 0; i < 0x0ffff; ++i)
        {
            if ("!#$%&'*+-.^_`|~1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(i) < 0)
            {
                assertInvalid(new String(new char[] { (char) i }));
            }
            else
            {
                assertThat(new SafeToken(new String(new char[] { (char) i })), hasToString(new String(new char[] { (char) i })));
            }
        }

        // also test a few CharSequences

        assertThat(new StringToken("3bc8D0"), hasToString("3bc8D0"));
        assertThat(new StringToken("sdf43D_sdf.sdf+234#sdfuo|sdf's"), hasToString("sdf43D_sdf.sdf+234#sdfuo|sdf's"));

        assertInvalid("space notAllowed");
        assertInvalid("sdf453(sdlfk)sd");
        assertInvalid("with=equals");
        assertInvalid("with@at");

    }


    private void assertInvalid(String value)
    {
        try
        {
            new StringToken(value).toString();
            fail(String.format("'%s' should be invalid", value));
        }
        catch (IllegalArgumentException e)
        {
            // pass
        }
    }

}




