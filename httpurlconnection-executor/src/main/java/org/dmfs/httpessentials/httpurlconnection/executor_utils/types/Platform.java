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

package org.dmfs.httpessentials.httpurlconnection.executor_utils.types;

import org.dmfs.httpessentials.types.Product;


/**
 * A {@link Product} representing the platform/system (e.g.: "Dalvik/2.1.0 (Linux; U; Android 6.0.1; Nexus 5
 * Build/ABC123)").
 * <p>
 * Uses the value of System.getProperty("http.agent") or {@link #FALLBACK_COMMENT} if that is not present.
 *
 * @author Gabor Keszthelyi
 */
public final class Platform implements Product
{
    public static final Platform INSTANCE = new Platform();

    private static final String FALLBACK_COMMENT = "(no platform info)";

    private final String mHttpAgent;


    private Platform()
    {
        String httpAgent = System.getProperty("http.agent");
        mHttpAgent = httpAgent != null ? httpAgent : FALLBACK_COMMENT;
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
