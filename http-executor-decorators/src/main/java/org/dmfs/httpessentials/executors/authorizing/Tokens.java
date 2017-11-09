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

package org.dmfs.httpessentials.executors.authorizing;

import org.dmfs.httpessentials.types.CharToken;
import org.dmfs.httpessentials.types.Token;


/**
 * Container class for frequently used Tokens.
 *
 * @author Marten Gajda
 */
public final class Tokens
{
    public final static Token ALGORITHM = new CharToken("algorithm");

    public final static Token BASIC = new CharToken("Basic");

    public final static Token CNONCE = new CharToken("cnonce");

    public final static Token DIGEST = new CharToken("Digest");

    public final static Token NC = new CharToken("nc");

    public final static Token NONCE = new CharToken("nonce");

    public final static Token OPAQUE = new CharToken("opaque");

    public final static Token QOP = new CharToken("qop");

    public final static Token REALM = new CharToken("realm");

    public final static Token RESPONSE = new CharToken("response");

    public final static Token URI = new CharToken("uri");

    public final static Token USERHASH = new CharToken("userhash");

    public final static Token USERNAME = new CharToken("username");


    private Tokens()
    {
        // no instances allowed
    }

}
