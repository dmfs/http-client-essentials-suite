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

package org.dmfs.httpessentials.executors.authenticating.credentials;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.Credentials;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.charsequences.Quoted;
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.utils.Parameter;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterables.decorators.Flattened;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.iterators.Function;
import org.dmfs.jems.charsequence.elementary.Hex;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.single.elementary.Digested;
import org.dmfs.optional.Optional;
import org.dmfs.optional.decorators.Mapped;
import org.dmfs.optional.iterable.PresentValues;

import java.net.URI;

import static org.dmfs.optional.Absent.absent;


/**
 * The {@link Credentials} of the DIGEST authentication scheme with {@code qop=auth}.
 *
 * @author Marten Gajda
 */
public final class AuthDigestCredentials implements Credentials
{
    private final HttpMethod mMethod;
    private final URI mRequestUri;
    private final Challenge mDigestChallenge;
    private final UserCredentials mUserCredentials;
    private final CharSequence mCnonce;


    public AuthDigestCredentials(HttpMethod method, URI requestUri, Challenge digestChallenge, UserCredentials userCredentials, CharSequence cnonce)
    {
        mMethod = method;
        mRequestUri = requestUri;
        mDigestChallenge = digestChallenge;
        mUserCredentials = userCredentials;
        mCnonce = cnonce;
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
        String algorithm = mDigestChallenge.parameter(new StringToken("algorithm")).value("MD5").toString();
        return new Flattened<>(
                new Seq<Pair<Token, CharSequence>>(
                        new Parameter("username", new Quoted(mUserCredentials.userName())),
                        new Parameter("realm", new Quoted(mDigestChallenge.parameter(new StringToken("realm")).value())),
                        new Parameter("nonce", new Quoted(mDigestChallenge.parameter(new StringToken("nonce")).value())),
                        new Parameter("uri", new Quoted(mRequestUri.getRawPath())),
                        new Parameter("qop", "auth"),
                        new Parameter("nc", "00000001"),
                        new Parameter("cnonce", new Quoted(mCnonce)),
                        new Parameter("opaque", new Quoted(mDigestChallenge.parameter(new StringToken("opaque")).value())),
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
                                        ":00000001:",
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
                                }, mDigestChallenge.parameter(new StringToken("opaque")))));
    }
}
