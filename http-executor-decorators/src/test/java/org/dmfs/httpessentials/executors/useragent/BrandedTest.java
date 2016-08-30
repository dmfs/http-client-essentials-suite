/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.executors.useragent;

import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.mockutils.executors.CapturingExecutor;
import org.dmfs.httpessentials.mockutils.requests.EmptyRequest;
import org.dmfs.httpessentials.types.CommentedProduct;
import org.dmfs.httpessentials.types.VersionedProduct;
import org.junit.Test;

import java.io.IOException;

import static org.dmfs.httpessentials.headers.HttpHeaders.USER_AGENT_HEADER;
import static org.junit.Assert.assertEquals;


/**
 * Functional test for {@link Branded}.
 *
 * @author Gabor Keszthelyi
 */
public class BrandedTest
{

    @Test
    public void test_userAgentHeaderGetsAdded() throws ProtocolException, ProtocolError, IOException
    {
        // ARRANGE
        CapturingExecutor capturingExecutor = new CapturingExecutor();
        Branded wrappedExecutor = new Branded(capturingExecutor, new CommentedProduct("name", "version", "comment"));

        // ACT
        wrappedExecutor.execute(null, new EmptyRequest<String>());

        // ASSERT
        String headerString = capturingExecutor.mCapturedRequest.headers()
                .header(USER_AGENT_HEADER)
                .toString();
        assertEquals("name/version (comment)", headerString);
    }


    @Test
    public void test_multipleAgentDecorators() throws ProtocolException, ProtocolError, IOException
    {
        // ARRANGE
        CapturingExecutor capturingExecutor = new CapturingExecutor();
        Branded smoothSyncExecutor = new Branded(capturingExecutor,
                new CommentedProduct("SmoothSync", "1.0", "debug"));
        Branded smoothSycnApiExecutor = new Branded(smoothSyncExecutor,
                new VersionedProduct("smoothsync-api-client", "0.4"));
        Branded oath2Executor = new Branded(smoothSycnApiExecutor,
                new VersionedProduct("oauth2-essentials", "0.3"));

        // ACT
        oath2Executor.execute(null, new EmptyRequest<String>());

        // ASSERT
        String headerString = capturingExecutor.mCapturedRequest.headers()
                .header(USER_AGENT_HEADER)
                .toString();
        assertEquals("SmoothSync/1.0 (debug) smoothsync-api-client/0.4 oauth2-essentials/0.3", headerString);
    }

}