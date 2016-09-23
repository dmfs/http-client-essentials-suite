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

/**
 * LoggingFacility abstraction bearing compatibility with LogCat and slf4j in mind.
 * https://developer.android.com/reference/android/util/Log.html
 * http://www.slf4j.org/api/org/slf4j/Logger.html
 *
 * @author Gabor Keszthelyi
 */
public interface LoggingFacility
{
    void log(LogLevel logLevel, String tag, String message);

    void log(LogLevel logLevel, String tag, String message, Throwable throwable);

}
