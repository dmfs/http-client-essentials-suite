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

package org.dmfs.httpessentials.executors.following.strategies;

import org.dmfs.httpessentials.HttpStatus;
import org.junit.Test;

import java.net.URI;

import static org.dmfs.httpessentials.executors.following.strategies.matcher.RedirectStrategyFollowMatcher.follows;
import static org.dmfs.httpessentials.executors.following.strategies.matcher.RedirectStrategyFollowMatcher.mockRedirect;
import static org.dmfs.httpessentials.executors.following.strategies.matcher.RedirectStrategyFollowMatcher.notFollows;
import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link Limited}.
 *
 * @author Marten Gajda
 */
public class LimitedTest
{
    @Test
    public void testFollow()
    {
        assertThat(new Limited(3, new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        1,
                        URI.create("https://example.net")));
        assertThat(new Limited(3, new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        2,
                        URI.create("https://example.net")));
        assertThat(new Limited(3, new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        3,
                        URI.create("https://example.net")));
        assertThat(new Limited(new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        1,
                        URI.create("https://example.net")));
        assertThat(new Limited(new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        2,
                        URI.create("https://example.net")));
        assertThat(new Limited(new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        3,
                        URI.create("https://example.net")));
        assertThat(new Limited(new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        4,
                        URI.create("https://example.net")));
        assertThat(new Limited(new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        5,
                        URI.create("https://example.net")));
    }


    @Test
    public void testNoFollow()
    {
        assertThat(new Limited(3, new NeverFollowStrategy()),
                notFollows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        1));
        assertThat(new Limited(3, new FollowStrategy()),
                notFollows(
                        mockRedirect(
                                HttpStatus.OK,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        1));
        assertThat(new Limited(3, new FollowStrategy()),
                notFollows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        4));
        assertThat(new Limited(new FollowStrategy()),
                notFollows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        6));
    }
}