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

import org.dmfs.httpessentials.executors.authorizing.Parametrized;
import org.dmfs.httpessentials.executors.authorizing.charsequences.Unquoted;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterables.UnquotedSplit;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Filter;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.pair.elementary.ValuePair;
import org.dmfs.optional.First;
import org.dmfs.optional.Optional;

import java.util.Iterator;


/**
 * @author Marten Gajda
 */
public final class SimpleParametrized implements Parametrized
{
    private final CharSequence mDelegate;


    public SimpleParametrized(CharSequence delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public Optional<CharSequence> parameter(final Token name)
    {
        return new org.dmfs.optional.decorators.Mapped<>(
                charSequenceCharSequencePair -> new Unquoted(charSequenceCharSequencePair.right().toString().trim()),
                new First<>(new Mapped<>(
                        new UnquotedSplit(mDelegate, ','),
                        o -> {
                            Iterable<CharSequence> result = new UnquotedSplit(o, '=');
                            Iterator<CharSequence> it = result.iterator();
                            return new ValuePair<>(it.next(), it.next());
                        }), new Filter<Pair<CharSequence, CharSequence>>()
                {
                    @Override
                    public boolean iterate(Pair<CharSequence, CharSequence> charSequenceCharSequencePair)
                    {
                        return charSequenceCharSequencePair.left().toString().trim().equalsIgnoreCase(name.toString());
                    }
                }));
    }
}
