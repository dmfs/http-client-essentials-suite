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

        assertValid("a");
        assertValid("3bc8D0");

        assertInvalid("space notAllowed");

        // The explicitly allowed non-alphanumeric characters:
        assertValid("!");
        assertValid("#");
        assertValid("$");
        assertValid("%");
        assertValid("&");
        assertValid("'");
        assertValid("*");
        assertValid("+");
        assertValid("-");
        assertValid(".");
        assertValid("^");
        assertValid("_");
        assertValid("`");
        assertValid("|");
        assertValid("~");

        // The following delimiters are not allowed:(DQUOTE and "(),/:;<=>?@[\]{}").
        assertInvalid("\"");
        assertInvalid("(");
        assertInvalid(")");
        assertInvalid(",");
        assertInvalid("/");
        assertInvalid(":");
        assertInvalid(";");
        assertInvalid("<");
        assertInvalid("=");
        assertInvalid(">");
        assertInvalid("?");
        assertInvalid("@");
        assertInvalid("[");
        assertInvalid("\\");
        assertInvalid("]");
        assertInvalid("{");
        assertInvalid("}");

        assertInvalid("sdf453(sdlfk)sd");
        assertInvalid("with=equals");
        assertInvalid("with@at");

        assertValid("sdf43D_sdf.sdf+234#sdfuo|sdf's");
    }


    private void assertValid(String value)
    {
        new StringToken(value);
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
        }
    }

}




