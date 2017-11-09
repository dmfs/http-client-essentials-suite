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

package org.dmfs.httpessentials.executors.authorizing.utils;

import org.dmfs.httpessentials.types.CharToken;
import org.dmfs.httpessentials.types.StringToken;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.jems.pair.Pair;


/**
 * A simple parameter.
 *
 * @author Marten Gajda
 */
public final class Parameter implements Pair<Token, CharSequence>
{
    private final Token mName;
    private final CharSequence mValue;


    public Parameter(String name, CharSequence value)
    {
        this(new StringToken(name), value);
    }


    public Parameter(Token name, CharSequence value)
    {
        mName = name;
        mValue = value;
    }


    @Override
    public Token left()
    {
        return mName;
    }


    @Override
    public CharSequence right()
    {
        return mValue;
    }
}
