/*
 * Copyright 2020 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following.strategies.matcher;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.following.RedirectStrategy;
import org.dmfs.httpessentials.headers.SingletonHeaders;
import org.dmfs.httpessentials.mockutils.entities.StaticMockResponseEntity;
import org.dmfs.httpessentials.mockutils.responses.CustomUrisMockResponse;
import org.dmfs.httpessentials.mockutils.responses.StaticMockResponse;
import org.hamcrest.Matcher;

import java.net.URI;

import static org.dmfs.httpessentials.headers.HttpHeaders.LOCATION;
import static org.dmfs.jems.hamcrest.matchers.LambdaMatcher.having;
import static org.dmfs.jems.hamcrest.matchers.optional.AbsentMatcher.absent;
import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.hamcrest.Matchers.is;


/**
 * @author Marten Gajda
 */
public final class RedirectStrategyFollowMatcher
{

    public static HttpResponse mockRedirect(HttpStatus status, URI source, URI location)
    {
        return new CustomUrisMockResponse(
                new StaticMockResponse(status,
                        new SingletonHeaders(LOCATION.entity(location)),
                        new StaticMockResponseEntity()),
                source, source);
    }


    public static Matcher<? super RedirectStrategy> follows(HttpResponse response, int attempt, URI destination)
    {
        return having(
                strategy -> strategy.location(response, attempt),
                is(present(destination)));
    }


    public static Matcher<? super RedirectStrategy> notFollows(HttpResponse response, int attempt)
    {
        return having(
                strategy -> strategy.location(response, attempt),
                absent());
    }

}
