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
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link SameAuthority}.
 */
public class SameAuthorityTest
{

    @Test
    public void testFollow()
    {
        assertThat(new SameAuthority(new FollowStrategy()),
                allOf(
                        follows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("https://example.com/test"),
                                        URI.create("https://example.com/new")),
                                1,
                                URI.create("https://example.com/new")),
                        follows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("https://example.com/test"),
                                        URI.create("//example.com/new")),
                                1,
                                URI.create("https://example.com/new")),
                        follows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("https://example.com/test"),
                                        URI.create("/new")),
                                1,
                                URI.create("https://example.com/new"))));
    }


    @Test
    public void testNoFollow()
    {
        assertThat(new SameAuthority(new NeverFollowStrategy()),
                notFollows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("/new")),
                        1));
        assertThat(new SameAuthority(new FollowStrategy()),
                allOf(
                        notFollows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("https://example.com/test"),
                                        URI.create("https://example.net")),
                                1),
                        notFollows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("https://example.com/test"),
                                        URI.create("http://example.com/new")),
                                1),
                        notFollows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("https://example.com/test"),
                                        URI.create("//example.net")),
                                1)));
    }
}