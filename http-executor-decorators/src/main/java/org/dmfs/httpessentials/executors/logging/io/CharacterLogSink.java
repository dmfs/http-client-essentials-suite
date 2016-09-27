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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;


/**
 * @author Gabor Keszthelyi
 */
public final class CharacterLogSink implements LogSink
{

    /**
     * The size of the buffer the bytes are read into to decode each character.
     */
    private static final int BYTE_BUFFER_CAPACITY = 4;

    /**
     * The size of the character buffer that is filled up, when reaches capacity, it is flushed. (Or when a new line
     * characters is encountered.)
     */
    private static final int DEFAULT_CHAR_BUFFER_CAPACITY = 1024;

    private final CharsetDecoder mCharsetDecoder;
    private final LogFacility mLogFacility;
    private final ByteBuffer mByteBuffer;
    private final CharBuffer mCharBuffer;


    public CharacterLogSink(String charsetName, LogFacility logFacility, int charBufferCapacity)
    {
        mCharsetDecoder = Charset.forName(charsetName).newDecoder();
        mLogFacility = logFacility;
        mByteBuffer = ByteBuffer.allocate(BYTE_BUFFER_CAPACITY);
        mCharBuffer = CharBuffer.allocate(charBufferCapacity);
    }


    public CharacterLogSink(String charsetName, LogFacility logFacility)
    {
        this(charsetName, logFacility, DEFAULT_CHAR_BUFFER_CAPACITY);
    }


    @Override
    public void sink(byte b)
    {
        mByteBuffer.put(b);

        mByteBuffer.limit(mByteBuffer.position()); // set limit, so decoder only reads until last byte put
        mByteBuffer.position(0); // set position to 0, so decoder starts read from there

        int before = mCharBuffer.position();
        CoderResult coderResult = mCharsetDecoder.decode(mByteBuffer, mCharBuffer,
                true); // TODO result error handling?
        int after = mCharBuffer.position();

        if (before < after) // char has been added
        {
            mByteBuffer.clear();
            flushIfNeeded();
        }
        else // char hasn't been added
        {
            mByteBuffer.limit(mByteBuffer.position() + 1); // increase limit to accommodate next byte
        }
    }


    private void flushIfNeeded()
    {
        if (mCharBuffer.position() == mCharBuffer.limit())
        {
            flush();
        }

        if (lastChar() == '\n') // TODO other new lines chars?
        {
            mCharBuffer.position(mCharBuffer.position() - 1);
            flush();
        }
    }


    private int lastChar()
    {
        try
        {
            return mCharBuffer.get(mCharBuffer.position() - 1);
        }
        catch (IndexOutOfBoundsException e)
        {
            return -1;
        }
    }


    @Override
    public void flush()
    {
        mCharBuffer.limit(mCharBuffer.position());
        mCharBuffer.position(0);
        String charsInBuffer = mCharBuffer.toString();
        mCharBuffer.clear();

        mLogFacility.log(charsInBuffer);
    }

}
