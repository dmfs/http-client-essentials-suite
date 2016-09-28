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

import java.io.UnsupportedEncodingException;


/**
 * @author Gabor Keszthelyi
 */

/*
TODO other implementations
"..we should handle binary entities differently. Options are:
1) don’t log
2) log base64 encoded
3) log quoted printable encoded
4) log hex encoded
5) other custom method"
 */
public interface LogSink
{
    void sink(byte b);

    void flush();
}
