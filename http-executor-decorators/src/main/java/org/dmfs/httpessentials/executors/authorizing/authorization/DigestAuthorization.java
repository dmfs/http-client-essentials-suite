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
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems2.Generator;
import org.dmfs.jems2.charsequence.Hex;
import org.dmfs.jems2.generator.DigestGenerator;
import org.dmfs.jems2.iterable.Joined;
import org.dmfs.jems2.iterable.PresentValues;
import org.dmfs.jems2.iterable.Seq;
import org.dmfs.jems2.optional.Mapped;
import org.dmfs.jems2.single.Backed;
import org.dmfs.jems2.single.Digest;

import java.net.URI;
import java.security.MessageDigest;

import static org.dmfs.jems.optional.elementary.Absent.absent;


/**
 * The {@link Authorization} value of a {@code Digest} authentication scheme as per <a href="https://tools.ietf.org/html/rfc2069">RFC 2069</a>
 *
 * @author Marten Gajda
 */
public final class DigestAuthorization implements Authorization
{
    private final HttpMethod mMethod;
    private final URI mRequestUri;
    private final Parametrized mDigestChallenge;
    private final UserCredentials mUserCredentials;


    public DigestAuthorization(HttpMethod method, URI requestUri, Parametrized digestChallenge, UserCredentials userCredentials)
    {
        mMethod = method;
        mRequestUri = requestUri;
        mDigestChallenge = digestChallenge;
        mUserCredentials = userCredentials;
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
        CharSequence algorithm = new Backed<>(mDigestChallenge.parameter(Tokens.ALGORITHM), "MD5").value();
        final Generator<MessageDigest> digestFactory = new DigestGenerator(algorithm.toString());
        final CharSequence realm = mDigestChallenge.parameter(Tokens.REALM).value();
        final CharSequence nonce = mDigestChallenge.parameter(Tokens.NONCE).value();

        return new Joined<>(
            new Seq<>(
                new Parameter(Tokens.USERNAME, new Quoted(mUserCredentials.userName())),
                new Parameter(Tokens.REALM, new Quoted(realm)),
                new Parameter(Tokens.URI, new Quoted(mRequestUri.getRawPath())),
                new Parameter(Tokens.NONCE, new Quoted(nonce)),
                new Parameter(Tokens.ALGORITHM, algorithm),
                new Parameter(Tokens.RESPONSE, new Quoted(
                    new Hex(new Digest(digestFactory,
                        new Hex(
                            new Digest(digestFactory,
                                mUserCredentials.userName(),
                                ":",
                                realm,
                                ":",
                                mUserCredentials.password()).value()),
                        ":",
                        nonce,
                        ":",
                        new Hex(
                            new Digest(digestFactory,
                                (CharSequence) mMethod.verb(),
                                ":",
                                mRequestUri.getRawPath()).value())
                    ).value())))
            ),
            new PresentValues<>(
                new Mapped<>(
                    charSequence -> new Parameter(Tokens.OPAQUE, new Quoted(charSequence)), mDigestChallenge.parameter(Tokens.OPAQUE))));
    }
}
