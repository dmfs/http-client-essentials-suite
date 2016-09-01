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

package org.dmfs.httpessentials.httpurlconnection.utils.decoration;

import org.dmfs.httpessentials.decoration.Decoration;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.httpurlconnection.utils.types.BottomBasicUserAgent;
import org.dmfs.httpessentials.types.Product;
import org.dmfs.httpessentials.types.SingletonUserAgent;
import org.dmfs.httpessentials.types.UserAgent;

import static org.dmfs.httpessentials.headers.HttpHeaders.USER_AGENT;


/**
 * A {@link Headers} decoration that appends the given {@link Product} to the end of an existing User-Agent header (or
 * creates it if it doesn't exist.)
 *
 * @author Gabor Keszthelyi
 */
public final class BottomUserAgentHeaderDecoration implements Decoration<Headers>
{
    private final Product mProduct;


    public BottomUserAgentHeaderDecoration(Product product)
    {
        mProduct = product;
    }


    @Override
    public Headers decorated(Headers originalHeaders)
    {
        UserAgent newUserAgent;

        if (originalHeaders.contains(USER_AGENT))
        {
            UserAgent originalUserAgent = originalHeaders.header(USER_AGENT).value();
            newUserAgent = new BottomBasicUserAgent(originalUserAgent, mProduct);
        }
        else
        {
            newUserAgent = new SingletonUserAgent(mProduct);
        }

        return originalHeaders.withHeader(USER_AGENT.entity(newUserAgent));
    }
}
