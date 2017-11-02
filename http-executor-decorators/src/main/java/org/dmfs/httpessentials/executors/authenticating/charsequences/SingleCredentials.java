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

package org.dmfs.httpessentials.executors.authenticating.charsequences;

import org.dmfs.httpessentials.executors.authenticating.Credentials;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.single.Single;


/**
 * @author Marten Gajda
 */
public final class SingleCredentials implements Single<CharSequence>
{
    private final Credentials mCredentials;


    public SingleCredentials(Credentials credentials)
    {
        mCredentials = credentials;
    }


    @Override
    public CharSequence value()
    {
        StringBuilder sb = new StringBuilder(128);
        sb.append(mCredentials.scheme());
        sb.append(' ');
        if (mCredentials.token().isPresent())
        {
            sb.append(mCredentials.token().value());
        }
        else
        {
            boolean first = true;
            for (Pair<Token, CharSequence> param : mCredentials.parameters())
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    sb.append(", ");
                }
                sb.append(param.left());
                sb.append('=');
                sb.append(param.right());
            }
        }
        System.out.println(sb.toString());
        return sb;
    }
}
