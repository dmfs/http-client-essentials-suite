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

package org.dmfs.httpessentials.httpurlconnection.utils.types;

import org.dmfs.httpessentials.types.Product;


/**
 * A {@link Product} representing the platform/system (e.g.: "Dalvik/2.1.0 (Linux; U; Android 6.0.1; Nexus 5
 * Build/ABC123)").
 * <p>
 * Uses the value of <code>System.getProperty("http.agent")</code> (or if that's not present, it assembles a description
 * from <code>java</code> and <code>os</code> properties.
 *
 * @author Gabor Keszthelyi
 */
public final class Platform implements Product
{
    public static final Platform INSTANCE = new Platform();

    private final String mHttpAgent;


    private Platform()
    {
        String httpAgent = System.getProperty("http.agent");
        if (httpAgent != null)
        {
            mHttpAgent = httpAgent;
        }
        else
        {
            mHttpAgent = String.format("(%s/%s; %s/%s)",
                    System.getProperty("java.vendor", "unknown"),
                    System.getProperty("java.version", "unknown"),
                    System.getProperty("os.name", "unknown OS"),
                    System.getProperty("os.version", "unknown OS version"));
        }
    }


    @Override
    public void appendTo(StringBuilder sb)
    {
        sb.append(mHttpAgent);
    }


    @Override
    public String toString()
    {
        return mHttpAgent;
    }
}
