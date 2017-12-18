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

package org.dmfs.httpessentials.executors.authorizing.challenges;

import org.dmfs.httpessentials.executors.authorizing.Challenge;
import org.dmfs.httpessentials.types.CharToken;
import org.dmfs.httpessentials.types.Token;
import org.hamcrest.FeatureMatcher;
import org.junit.Test;

import static org.dmfs.jems.hamcrest.matchers.IterableMatcher.iteratesTo;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author Marten Gajda
 */
public class HeaderChallengesTest
{
    @Test
    public void test()
    {
        assertThat(HeaderChallenges.WWW_AUTHENTICATE.valueFromString("Foo realm=\"Realm2\", Bar realm=\"Realm1\""),
                iteratesTo(
                        allOf(new SchemeMatcher(new CharToken("Bar")), new ChallengeMatcher("realm=\"Realm1\"")),
                        allOf(new SchemeMatcher(new CharToken("Foo")), new ChallengeMatcher("realm=\"Realm2\""))));
    }


    private final static class SchemeMatcher extends FeatureMatcher<Challenge, Token>
    {
        public SchemeMatcher(Token expectedToken)
        {
            super(is(expectedToken), "scheme equals " + expectedToken, "scheme equals " + expectedToken);
        }


        @Override
        protected Token featureValueOf(Challenge challenge)
        {
            return challenge.scheme();
        }
    }


    private final static class ChallengeMatcher extends FeatureMatcher<Challenge, String>
    {
        public ChallengeMatcher(String expectedChallenge)
        {
            super(is(expectedChallenge), "challenge equals " + expectedChallenge, "challenge equals " + expectedChallenge);
        }


        @Override
        protected String featureValueOf(Challenge challenge)
        {
            return challenge.challenge().toString();
        }
    }

}