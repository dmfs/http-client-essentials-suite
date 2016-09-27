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

package org.dmfs.httpessentials.executors.logging.io;

import org.dmfs.httpessentials.executors.logging.logfacility.LogFacility;
import org.junit.Test;

import java.io.UnsupportedEncodingException;


/**
 * @author Gabor Keszthelyi
 */
public class CharacterLogSinkTest
{

    @Test
    public void test() throws UnsupportedEncodingException
    {
        final StringBuilder messages = new StringBuilder();
        messages.append("--START").append("\n");

        LogSink logSink = new CharacterLogSink("UTF-8", new LogFacility()
        {
            @Override
            public void log(String message)
            {
                messages.append("log: ").append(message).append("\n");
            }


            @Override
            public void logError(String message, Throwable throwable)
            {

            }
        }, 6);

        byte[] bytes = "hello\nthere\n\nhőgyálíűúőö".getBytes("UTF-8");

        for (byte b : bytes)
        {
            logSink.sink(b);
            messages.append(b).append("\n");
        }
        messages.append("--flushing:").append("\n");
        logSink.flush();
        messages.append("--END");

        System.out.println(messages.toString());
    }

}