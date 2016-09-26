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

import org.dmfs.httpessentials.executors.logging.formatter.BodyLineFormatter;
import org.dmfs.httpessentials.executors.logging.logfacility.LogFacility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


/**
 * @author Gabor Keszthelyi
 */
public final class LoggingOutputStream extends OutputStream
{

    private final LogFacility mLogFacility;
    private final BodyLineFormatter mBodyLineFormatter;
    private final ByteArrayOutputStream mByteArrayOutputStream;


    public LoggingOutputStream(LogFacility logFacility, BodyLineFormatter bodyLineFormatter)
    {
        mLogFacility = logFacility;
        mBodyLineFormatter = bodyLineFormatter;
        mByteArrayOutputStream = new ByteArrayOutputStream();
    }


    @Override
    public void write(int b) throws IOException
    {
        if (b == '\n')
        {
            flushLine();
        }
        else if (b != -1)
        {
            mByteArrayOutputStream.write(b);
        }
    }


    @Override
    public void close() throws IOException
    {
        flushLine();
    }


    @Override
    public void flush() throws IOException
    {
        flushLine();
    }


    private void flushLine() throws UnsupportedEncodingException
    {
        if (mByteArrayOutputStream.size() != 0)
        {
            mLogFacility.log(createLogLine());
            mByteArrayOutputStream.reset();
        }
    }


    private String createLogLine() throws UnsupportedEncodingException
    {
        return mBodyLineFormatter.bodyLineMsg(mByteArrayOutputStream.toString(mBodyLineFormatter.charset()));
    }
}
