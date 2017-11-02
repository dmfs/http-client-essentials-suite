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

package org.dmfs.jems.single.elementary;

import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.iterators.Function;
import org.dmfs.jems.single.Single;
import org.dmfs.jems.single.decorators.DelegatingSingle;

import java.io.UnsupportedEncodingException;
import java.util.Locale;


/**
 * A {@link Single} of a byte array of the MD5 sum of specific input values.
 * <p>
 * TODO: this should go to jems
 *
 * @author Marten Gajda
 */
public final class Md5 extends DelegatingSingle<byte[]>
{
    public Md5(byte[]... parts)
    {
        this(new Mapped<>(new Seq<>(parts), new Function<byte[], Single<byte[]>>()
        {
            @Override
            public Single<byte[]> apply(byte[] bytes)
            {
                return new ValueSingle<>(bytes);
            }
        }));
    }


    public Md5(CharSequence... parts)
    {
        this("UTF-8", parts);
    }


    public Md5(final String encoding, CharSequence... parts)
    {
        this(new Mapped<>(new Seq<>(parts), new Function<CharSequence, Single<byte[]>>()
        {
            @Override
            public Single<byte[]> apply(CharSequence bytes)
            {
                try
                {
                    return new ValueSingle<>(bytes.toString().getBytes(encoding));
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException(String.format(Locale.ENGLISH, "%s encoding not supported by runtime", encoding));
                }
            }
        }));
    }


    public Md5(Single<byte[]>... parts)
    {
        this(new Seq<>(parts));
    }


    public Md5(Iterable<Single<byte[]>> parts)
    {
        super(new Digested("MD5", parts));
    }
}
