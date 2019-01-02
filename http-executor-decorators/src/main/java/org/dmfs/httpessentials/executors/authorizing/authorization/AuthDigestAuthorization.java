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

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authorizing.Authorization;
import org.dmfs.httpessentials.executors.authorizing.Parametrized;
import org.dmfs.httpessentials.executors.authorizing.Tokens;
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.executors.authorizing.charsequences.Quoted;
import org.dmfs.httpessentials.executors.authorizing.utils.Parameter;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterables.elementary.PresentValues;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.jems.charsequence.elementary.Hex;
import org.dmfs.jems.iterable.composite.Joined;
import org.dmfs.jems.messagedigest.MessageDigestFactory;
import org.dmfs.jems.messagedigest.elementary.DigestFactory;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.optional.decorators.Sieved;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.single.combined.Backed;
import org.dmfs.jems.single.elementary.Digest;

import java.net.URI;

import static org.dmfs.jems.optional.elementary.Absent.absent;


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
        return Tokens.DIGEST;
    }


    @Override
    public Optional<CharSequence> token()
    {
        return absent();
    }


    @Override
    public Iterable<Pair<Token, CharSequence>> parameters()
    {
        final CharSequence algorithm = new Backed<>(mDigestChallenge.parameter(Tokens.ALGORITHM), "MD5").value();
        final MessageDigestFactory digestFactory = new DigestFactory(algorithm.toString());
        final Optional<CharSequence> userhash = mDigestChallenge.parameter(Tokens.USERHASH);
        final CharSequence realm = mDigestChallenge.parameter(Tokens.REALM).value();
        final CharSequence nonce = mDigestChallenge.parameter(Tokens.NONCE).value();

        final CharSequence username = mUserCredentials.userName();
        CharSequence user = new Mapped<CharSequence, CharSequence>(
                argument -> new Hex(new Digest(digestFactory, username, ":", realm).value()),
                new Sieved<>(argument -> "true".equalsIgnoreCase(argument.toString()), userhash)).value(username);

        return new Joined<>(
                new Seq<>(
                        new Parameter(Tokens.USERNAME, new Quoted(user)),
                        new Parameter(Tokens.REALM, new Quoted(realm)),
                        new Parameter(Tokens.NONCE, new Quoted(nonce)),
                        new Parameter(Tokens.URI, new Quoted(mRequestUri.getRawPath())),
                        new Parameter(Tokens.QOP, "auth"),
                        new Parameter(Tokens.NC, new Hex(bigEndianByteArray(mNonceCount))),
                        new Parameter(Tokens.CNONCE, new Quoted(mCnonce)),
                        new Parameter(Tokens.ALGORITHM, algorithm),
                        new Parameter(Tokens.RESPONSE, new Quoted(
                                new Hex(new Digest(digestFactory,
                                        new Hex(new Digest(digestFactory,
                                                mUserCredentials.userName(),
                                                ":",
                                                realm,
                                                ":",
                                                mUserCredentials.password()).value()),
                                        ":",
                                        nonce,
                                        ":",
                                        new Hex(bigEndianByteArray(mNonceCount)),
                                        ":",
                                        mCnonce,
                                        ":auth:",
                                        new Hex(new Digest(digestFactory,
                                                (CharSequence) mMethod.verb(),
                                                ":",
                                                mRequestUri.getRawPath()).value())
                                ).value())))),
                new PresentValues<>(
                        new Mapped<>(charSequence -> new Parameter(Tokens.OPAQUE, new Quoted(charSequence)), mDigestChallenge.parameter(Tokens.OPAQUE)),
                        new Mapped<>(argument -> new Parameter(Tokens.USERHASH, argument), mDigestChallenge.parameter(Tokens.USERHASH))));
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
