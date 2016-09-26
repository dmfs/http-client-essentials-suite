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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


/**
 * @author Gabor Keszthelyi
 */
public final class LoggingInputStream extends InputStream
{
    private final InputStream mInputStream;
    private final LoggingFacility mLoggingFacility;
    private final BodyLineFormatter mBodyLineFormatter;
    private final ByteArrayOutputStream mByteArrayOutputStream;

    // TODO Alternative to ByteArrayOutputStream:
    // CharBuffer charBuffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes));


    public LoggingInputStream(InputStream inputStream, LoggingFacility loggingFacility, BodyLineFormatter bodyLineFormatter)
    {
        mInputStream = inputStream;
        mLoggingFacility = loggingFacility;
        mBodyLineFormatter = bodyLineFormatter;
        mByteArrayOutputStream = new ByteArrayOutputStream();
    }


    @Override
    public int read() throws IOException
    {
        int theByte = mInputStream.read();

        if (theByte != -1)
        {
            mByteArrayOutputStream.write(theByte);
        }

        if (theByte == '\n')
        {
            logLine();
            mByteArrayOutputStream.reset();
        }

        return theByte;
    }


    private void logLine() throws UnsupportedEncodingException
    {
        mLoggingFacility.log(createLogLine());
    }


    private String createLogLine() throws UnsupportedEncodingException
    {
        return mBodyLineFormatter.bodyLineMsg(mByteArrayOutputStream.toString(mBodyLineFormatter.charset()));
    }


    @Override
    public void close() throws IOException
    {
        mInputStream.close();
        if (mByteArrayOutputStream.size() != 0)
        {
            logLine();
            mByteArrayOutputStream.reset();
        }
    }
}
