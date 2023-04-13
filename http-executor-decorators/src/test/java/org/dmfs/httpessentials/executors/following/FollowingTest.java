/*
 * Copyright 2023 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.*;
import org.dmfs.httpessentials.entities.EmptyHttpRequestEntity;
import org.dmfs.httpessentials.executors.following.policies.FollowPolicy;
import org.dmfs.httpessentials.executors.following.policies.Relative;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.mockutils.executors.StaticMockServerExecutor;
import org.dmfs.httpessentials.mockutils.responses.StaticMockResponse;
import org.dmfs.jems.optional.elementary.Absent;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.dmfs.jems2.mockito.Mock.*;
import static org.saynotobugs.confidence.Assertion.assertThat;
import static org.saynotobugs.confidence.quality.Core.*;


class FollowingTest
{
    private URI uri = URI.create("https://example.com");


    @Test
    void test()
    {
        Headers mockHeaders = mock("Headers", Headers.class);
        HttpResponseEntity mockEntity = mock(HttpResponseEntity.class, with(HttpResponseEntity::contentType, returning(new Absent<>())));
        HttpResponse mockResponse = new StaticMockResponse(HttpStatus.UNAUTHORIZED, mockHeaders, mockEntity);
        HttpRequestExecutor delegate = new StaticMockServerExecutor(mockResponse);
        assertThat(new Following(delegate, new Relative(new FollowPolicy())),
            has("response", e -> e.execute(uri, new HttpRequest<HttpResponse>()
                {
                    @Override
                    public HttpMethod method()
                    {
                        return HttpMethod.GET;
                    }


                    @Override
                    public Headers headers()
                    {
                        return new EmptyHeaders();
                    }


                    @Override
                    public HttpRequestEntity requestEntity()
                    {
                        return new EmptyHttpRequestEntity();
                    }


                    @Override
                    public HttpResponseHandler<HttpResponse> responseHandler(HttpResponse response)
                    {
                        return x -> x;
                    }
                }),
                allOf(
                    has("entity", HttpResponse::responseEntity, is(mockEntity)),
                    has("headers", HttpResponse::headers, is(mockHeaders)),
                    has("status", HttpResponse::status, is(HttpStatus.UNAUTHORIZED)),
                    has("request URI", HttpResponse::requestUri, is(uri)),
                    has("response URI", HttpResponse::responseUri, is(uri))
                )
            ));
    }

}