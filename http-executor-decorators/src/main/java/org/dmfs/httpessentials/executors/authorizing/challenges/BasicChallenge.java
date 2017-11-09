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


/**
 * A {@link Challenge} derived from a header value {@link CharSequence}.
 *
 * @author Marten Gajda
 */
public final class BasicChallenge implements Challenge
{
    private final CharSequence mChallengeText;


    public BasicChallenge(CharSequence challengeText)
    {
        mChallengeText = challengeText;
    }


    @Override
    public Token scheme()
    {
        int spacePos = space();
        return spacePos > 0 ? new CharToken(mChallengeText.subSequence(0, space()).toString()) : new CharToken(mChallengeText.toString());
    }


    @Override
    public CharSequence challenge()
    {
        int spacePos = space();
        return spacePos > 0 ? mChallengeText.subSequence(space() + 1, mChallengeText.length()).toString().trim() : "";
    }


    private int space()
    {
        for (int i = 0, count = mChallengeText.length(); i < count; ++i)
        {
            if (mChallengeText.charAt(i) == ' ')
            {
                return i;
            }
        }
        return -1;
    }
}
