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

package org.dmfs.httpessentials.executors.logging.elements;

import org.dmfs.httpessentials.client.HttpRequestExecutor;

import org.dmfs.httpessentials.executors.logging.Logging;
import org.dmfs.httpessentials.executors.logging.elements.sample.Headers;
import org.dmfs.httpessentials.executors.logging.elements.sample.MethodAndUri;
import org.dmfs.httpessentials.executors.logging.elements.sample.Status;

import static org.dmfs.httpessentials.executors.logging.elements.sample.Headers.ONLY_NON_PERSONAL;


/**
 * @author Gabor Keszthelyi
 */
public final class SampleUsage
{
    public void sample()
    {
        HttpLogElements httpLogElements = new HttpLogElementsBuilder()
                // Request elements in the order of printing:
                .add(new MethodAndUri())
                .add(new Headers(ONLY_NON_PERSONAL))

                // Response elements in the order of printing:
                .add(new Status())
                .add(new Headers())

                // LogSinkProvider for the body logging:
                .add(new TextBody())
                .build();

        HttpRequestExecutor originalExecutor = null;
        new Logging(originalExecutor, new BaseHttpLogger(httpLogElements));
    }

}
