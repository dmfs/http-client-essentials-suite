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

package org.dmfs.httpessentials.executors.logging;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.executors.logging.formatter.DefaultHttpLogFormatter;
import org.dmfs.httpessentials.executors.logging.formatter.DesignFormatter;
import org.dmfs.httpessentials.executors.logging.formatter.NoPersonalHeader;
import org.dmfs.httpessentials.executors.logging.httplogger.DefaultHttpLogger;
import org.dmfs.httpessentials.executors.logging.httplogger.HttpLogger;
import org.dmfs.httpessentials.executors.logging.httplogger.UrlFiltered;
import org.dmfs.httpessentials.executors.logging.logfacility.LogcatLogFacility;


/**
 * @author Gabor Keszthelyi
 */
public final class SampleUsage
{
    public static void main(String[] args)
    {
        HttpRequestExecutor originalExecutor = null;

        HttpLogger httpLogger =
                new UrlFiltered(
                        new DefaultHttpLogger(
                                new NoPersonalHeader(new DesignFormatter(new DefaultHttpLogFormatter())),
                                new LogcatLogFacility(2, "tag")),
                        "http://");

        HttpRequestExecutor wrappedExecutor1 = new Logging(originalExecutor, httpLogger);

        HttpRequestExecutor wrappedExecutor2 = new Logging(originalExecutor,
                new DesignFormatter(new DefaultHttpLogFormatter()), new LogcatLogFacility(2, "tag"));
    }

}
