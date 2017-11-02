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

package org.dmfs.httpessentials.executors.authenticating.authorization;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authenticating.Authorization;
import org.dmfs.httpessentials.executors.authenticating.Parametrized;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.charsequences.Quoted;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.utils.Parameter;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterables.decorators.Flattened;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.Function;
import org.dmfs.jems.charsequence.elementary.Hex;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.single.elementary.Digested;
import org.dmfs.optional.Optional;
import org.dmfs.optional.decorators.Filtered;
import org.dmfs.optional.decorators.Mapped;
import org.dmfs.optional.iterable.PresentValues;

import java.net.URI;

import static org.dmfs.optional.Absent.absent;


/**
 * The {@link Authorization} value of the {@code Digest} authentication scheme with {@code qop=auth}.
 *
 * @author Marten Gajda
 */
public final class AuthDigestAuthorization implements Authorization
{
    private final HttpMethod mMethod;
    private final URI mRequestUri;
    private final Parametrized mDigestChallenge;
    private final UserCredentials mUserCredentials;
    private final CharSequence mCnonce;
    private final int mNonceCount;


    public AuthDigestAuthorization(HttpMethod method, URI requestUri, Parametrized digestChallenge, UserCredentials userCredentials, CharSequence cnonce, int nonceCount)
    {
        mMethod = method;
        mRequestUri = requestUri;
        mDigestChallenge = digestChallenge;
        mUserCredentials = userCredentials;
        mCnonce = cnonce;
        mNonceCount = nonceCount;
    }


    @Override
    public Token scheme()
    {
        return new StringToken("Digest");
    }


    @Override
    public Optional<CharSequence> token()
    {
        return absent();
    }


    @Override
    public Iterable<Pair<Token, CharSequence>> parameters()
    {
        final String algorithm = mDigestChallenge.parameter(new StringToken("algorithm")).value("MD5").toString();
        final Optional<CharSequence> userhash = mDigestChallenge.parameter(new StringToken("userhash"));

        final CharSequence username = mUserCredentials.userName();
        CharSequence user = new Mapped<>(
                new Function<CharSequence, CharSequence>()
                {
                    @Override
                    public CharSequence apply(CharSequence argument)
                    {
                        return new Hex(new Digested(algorithm, username, ":", mDigestChallenge.parameter(new StringToken("realm")).value()).value());
                    }
                },
                new Filtered<>(new Filter<CharSequence>()
                {
                    @Override
                    public boolean iterate(CharSequence argument)
                    {
                        return "true".equalsIgnoreCase(argument.toString());
                    }
                }, userhash)).value(username);

        return new Flattened<>(
                new Seq<Pair<Token, CharSequence>>(
                        new Parameter("username", new Quoted(user)),
                        new Parameter("realm", new Quoted(mDigestChallenge.parameter(new StringToken("realm")).value())),
                        new Parameter("nonce", new Quoted(mDigestChallenge.parameter(new StringToken("nonce")).value())),
                        new Parameter("uri", new Quoted(mRequestUri.getRawPath())),
                        new Parameter("qop", "auth"),
                        new Parameter("nc", new Hex(bigEndianByteArray(mNonceCount))),
                        new Parameter("cnonce", new Quoted(mCnonce)),
                        new Parameter("algorithm", algorithm),
                        new Parameter("response", new Quoted(
                                new Hex(new Digested(algorithm,
                                        new Hex(new Digested(algorithm,
                                                mUserCredentials.userName(),
                                                ":",
                                                mDigestChallenge.parameter(new StringToken("realm")).value(),
                                                ":",
                                                mUserCredentials.password()).value()),
                                        ":",
                                        mDigestChallenge.parameter(new StringToken("nonce")).value(),
                                        ":",
                                        new Hex(bigEndianByteArray(mNonceCount)),
                                        ":",
                                        mCnonce,
                                        ":auth:",
                                        new Hex(new Digested(algorithm,
                                                (CharSequence) mMethod.verb(),
                                                ":",
                                                mRequestUri.getRawPath()).value())
                                ).value())))),
                new PresentValues<>(
                        new Mapped<>(
                                new Function<CharSequence, Pair<Token, CharSequence>>()
                                {
                                    @Override
                                    public Pair<Token, CharSequence> apply(CharSequence charSequence)
                                    {
                                        return new Parameter("opaque", new Quoted(charSequence));
                                    }
                                }, mDigestChallenge.parameter(new StringToken("opaque"))),
                        new Mapped<>(new Function<CharSequence, Pair<Token, CharSequence>>()
                        {
                            @Override
                            public Pair<Token, CharSequence> apply(CharSequence argument)
                            {
                                return new Parameter("userhash", argument);
                            }
                        }, mDigestChallenge.parameter(new StringToken("userhash")))));
    }


    // TODO: make this a Single<byte[]>
    private byte[] bigEndianByteArray(int i)
    {
        return new byte[] {
                (byte) (i >>> 24),
                (byte) (i >>> 16),
                (byte) (i >>> 8),
                (byte) (i)
        };
    }
}