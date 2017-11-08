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
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.executors.authorizing.charsequences.Quoted;
import org.dmfs.httpessentials.executors.authorizing.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authorizing.utils.Parameter;
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
                        new Parameter("uri", new Quoted(mRequestUri.getRawPath())),
                        new Parameter("nonce", new Quoted(mDigestChallenge.parameter(new StringToken("nonce")).value())),
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
                                        new Hex(new Digested(algorithm,
                                                (CharSequence) mMethod.verb(),
                                                ":",
                                                mRequestUri.getRawPath()).value())
                                ).value())))
                ),
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
