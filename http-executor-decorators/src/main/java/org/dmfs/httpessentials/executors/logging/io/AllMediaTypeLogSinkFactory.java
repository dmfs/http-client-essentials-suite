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
import org.dmfs.httpessentials.types.MediaType;


/**
 * @author Gabor Keszthelyi
 */
public final class AllMediaTypeLogSinkFactory implements LogSinkFactory
{
    private final LogFacility mLogFacility;


    public AllMediaTypeLogSinkFactory(LogFacility logFacility)
    {
        mLogFacility = logFacility;
    }


    @Override
    public LogSink logSink(MediaType mediaType)
    {
        if (mediaType.mainType().equalsIgnoreCase("text"))
        {
            return new CharacterLogSink(mediaType.charset("UTF-8"), mLogFacility, 1024);
        }

        // TODO other types

        return NullLogSink.INSTANCE;
    }
}
