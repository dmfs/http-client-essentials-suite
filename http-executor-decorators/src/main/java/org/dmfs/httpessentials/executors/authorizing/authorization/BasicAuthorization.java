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

package org.dmfs.httpessentials.executors.authorizing.authorization;

import net.iharder.Base64;
import org.dmfs.httpessentials.executors.authorizing.Authorization;
import org.dmfs.httpessentials.executors.authorizing.Tokens;
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterables.EmptyIterable;
import org.dmfs.jems.pair.Pair;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;


/**
 * The {@link Authorization} value of the {@code Basic} auth scheme.
 *
 * @author Marten Gajda
 */
public final class BasicAuthorization implements Authorization
{
    private final UserCredentials mCredentials;
    private final String mEncoding;


    public BasicAuthorization(UserCredentials credentials)
    {
        this(credentials, "UTF-8");
    }


    public BasicAuthorization(UserCredentials credentials, String encoding)
    {
        mCredentials = credentials;
        mEncoding = encoding;
    }


    @Override
    public Token scheme()
    {
        return Tokens.BASIC;
    }


    @Override
    public Optional<CharSequence> token()
    {
        try
        {
            return new Present<>(
                    Base64.encodeBytes(String.format(Locale.ENGLISH, "%s:%s", mCredentials.userName(), mCredentials.password()).getBytes(mEncoding),
                            Base64.NO_OPTIONS));
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(String.format("Runtime does not support %s encoding.", mEncoding), e);
        }
        catch (IOException e)
        {
            throw new RuntimeException("IOException while operating on Strings", e);
        }
    }


    @Override
    public Iterable<Pair<Token, CharSequence>> parameters()
    {
        // Basic authentication doesn't use credential parameters
        return EmptyIterable.instance();
    }
}
