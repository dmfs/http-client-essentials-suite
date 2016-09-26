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

import org.dmfs.httpessentials.executors.logging.formatter.BodyLineFormatter;
import org.dmfs.httpessentials.executors.logging.io.LoggingInputStream;
import org.dmfs.httpessentials.executors.logging.logfacility.LogFacility;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author Gabor Keszthelyi
 */
public class LoggingInputStreamTest
{

    @Test
    public void test() throws IOException
    {
        InputStream inputStream = new ByteArrayInputStream("hello\nthere".getBytes());

        final StringBuilder messages = new StringBuilder();
        LogFacility logFacility = new LogFacility()
        {

            @Override
            public void log(String message)
            {
                messages.append(message);
            }


            @Override
            public void logError(String message, Throwable throwable)
            {

            }
        };

        BodyLineFormatter bodyLineFormatter = new BodyLineFormatter()
        {
            @Override
            public String charset()
            {
                return "UTF-8";
            }


            @Override
            public String bodyLineMsg(String aLineInTheBody)
            {
                return "Log: " + aLineInTheBody;
            }
        };
        InputStream underTest = new LoggingInputStream(inputStream, logFacility, bodyLineFormatter);

        int b;
        while ((b = underTest.read()) != -1)
        {
            messages.append(b).append("\n");
        }
        underTest.close();

        System.out.println("result:\n" + messages.toString());
    }

}