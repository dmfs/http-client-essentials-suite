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
import org.dmfs.jems.single.elementary.ValueSingle;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


/**
 * A {@link Single} of a byte array which represents the digested value of the given byte arrays.
 * <p>
 * TODO: this should go to jems
 *
 * @author Marten Gajda
 */
public final class Digested implements Single<byte[]>
{
    private final String mAlgorithm;
    private final Iterable<Single<byte[]>> mParts;


    public Digested(String algorithm, byte[]... parts)
    {
        this(algorithm, new Mapped<>(new Seq<>(parts), new Function<byte[], Single<byte[]>>()
        {
            // TODO: this could go to jems as `SingleFunction`
            @Override
            public Single<byte[]> apply(byte[] bytes)
            {
                return new ValueSingle<>(bytes);
            }
        }));
    }


    public Digested(String algorithm, CharSequence... parts)
    {
        this(algorithm, "UTF-8", parts);
    }

    // TODO: can we get a better distinction between this and the previous ctor?
    // The follwoing is rather ambiguous:
    // new Digested("MD5","b","c")


    public Digested(String algorithm, final String encoding, CharSequence... parts)
    {
        this(algorithm, new Mapped<>(new Seq<>(parts), new Function<CharSequence, Single<byte[]>>()
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


    public Digested(final String algorithm, Single<byte[]>... parts)
    {
        this(algorithm, new Seq<>(parts));
    }


    public Digested(String algorithm, Iterable<Single<byte[]>> parts)
    {
        mAlgorithm = algorithm;
        mParts = parts;
    }


    @Override
    public byte[] value()
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance(mAlgorithm);
            for (Single<byte[]> part : mParts)
            {
                md.update(part.value());
            }
            return md.digest();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(String.format(Locale.ENGLISH, "Algorithm %s not supported by Runtime", mAlgorithm), e);
        }
    }
}
