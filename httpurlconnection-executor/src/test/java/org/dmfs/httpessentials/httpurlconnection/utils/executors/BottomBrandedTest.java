/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.httpurlconnection.utils.executors;

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.mockutils.executors.CapturingExecutor;
import org.dmfs.httpessentials.mockutils.requests.EmptyRequest;
import org.dmfs.httpessentials.types.SimpleProduct;
import org.dmfs.httpessentials.types.SingletonUserAgent;
import org.dmfs.httpessentials.types.UserAgent;
import org.junit.Test;

import java.io.IOException;

import static org.dmfs.httpessentials.headers.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;


/**
 * Functional test for {@link BottomBranded}.
 *
 * @author Gabor Keszthelyi
 */
public class BottomBrandedTest
{

    @Test
    public void test_whenNoUserAgentIsInRequest_shouldAddIt() throws ProtocolException, ProtocolError, IOException
    {
        // ARRANGE
        CapturingExecutor capturingExecutor = new CapturingExecutor();
        BottomBranded wrappedExecutor = new BottomBranded(capturingExecutor, new SimpleProduct("bottom-product"));

        // ACT
        wrappedExecutor.execute(null, new EmptyRequest<String>());

        // ASSERT
        String headerString = capturingExecutor.mCapturedRequest.headers().header(USER_AGENT).toString();
        assertEquals("bottom-product", headerString);
    }


    @Test
    public void test_whenThereIsUserAgentHeader_shouldAppendBottomOneAtTheEnd() throws ProtocolException, ProtocolError, IOException
    {
        // ARRANGE
        CapturingExecutor capturingExecutor = new CapturingExecutor();
        HttpRequestExecutor wrappedExecutor = new BottomBranded(capturingExecutor, new SimpleProduct("bottom-product"));

        UserAgent userAgent = new SingletonUserAgent(new SimpleProduct("product1")).withProduct(
                new SimpleProduct("product2"));
        Header originalHeader = USER_AGENT.entity(userAgent);

        // ACT
        wrappedExecutor.execute(null, new RequestWithHeader(originalHeader));

        // ASSERT
        String modifiedHeaderString = capturingExecutor.mCapturedRequest.headers().header(USER_AGENT).toString();
        assertEquals("product2 product1 bottom-product", modifiedHeaderString);
    }


    @Test
    public void test_multipleBottomDecorations_shouldAppendAllAtTheEnd() throws ProtocolException, ProtocolError, IOException
    {
        // ARRANGE
        CapturingExecutor capturingExecutor = new CapturingExecutor();
        HttpRequestExecutor platformWrapped = new BottomBranded(capturingExecutor, new SimpleProduct("platform"));
        HttpRequestExecutor urlConnectionWrapped = new BottomBranded(platformWrapped,
                new SimpleProduct("url-connection"));

        UserAgent userAgent = new SingletonUserAgent(new SimpleProduct("product1")).withProduct(
                new SimpleProduct("product2"));
        Header originalHeader = USER_AGENT.entity(userAgent);

        // ACT
        urlConnectionWrapped.execute(null, new RequestWithHeader(originalHeader));

        // ASSERT
        String modifiedHeaderString = capturingExecutor.mCapturedRequest.headers().header(USER_AGENT).toString();
        assertEquals("product2 product1 url-connection platform", modifiedHeaderString);
    }

}