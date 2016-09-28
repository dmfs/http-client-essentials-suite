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

package org.dmfs.httpessentials.executors.logging.elements.elements;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.logging.elements.ResponseLogElement;


/**
 * @author Gabor Keszthelyi
 */
public final class ResponseContentDesc implements ResponseLogElement
{
    @Override
    public void log(HttpResponse response, StringBuilder log)
    {
        // log content type and content length
    }
}
