/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.cache;

import net.iharder.Base64;
import org.dmfs.httpessentials.cache.cache.CacheKey;
import org.dmfs.httpessentials.client.HttpRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * A {@link CacheKey} that's derived from an {@link HttpRequest}.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class RequestCacheKey implements CacheKey
{
    private final static String UTF8 = "UTF-8";
    private final static String DIGEST = "SHA-256";

    private final URI mRequestUri;
    private final HttpRequest<?> mRequest;
    private String mStringRep;


    public RequestCacheKey(URI requestUri, HttpRequest<?> request)
    {
        mRequestUri = requestUri;
        mRequest = request;
    }


    @Override
    public String toString()
    {
        if (mStringRep == null)
        {
            try
            {
                MessageDigest md = MessageDigest.getInstance(DIGEST);
                md.digest(mRequest.method().verb().getBytes(UTF8));
                md.digest(mRequestUri.toASCIIString().getBytes(UTF8));
                mStringRep = Base64.encodeBytes(md.digest(), Base64.URL_SAFE);
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException(String.format("Runtime does not support %s digest algorithm.", DIGEST), e);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(String.format("Runtime does not support %s encoding.", UTF8), e);
            }
            catch (IOException e)
            {
                throw new RuntimeException(String.format("Can't base64 encode"), e);
            }
        }
        return mStringRep;
    }
}
