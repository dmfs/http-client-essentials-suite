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
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link Temporary}.
 */
public class PermanentTest
{
    @Test
    public void test()
    {
        assertThat(
                new Permanent(new FollowStrategy()),
                allOf(
                        follows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1,
                                URI.create("http:://example.com")),
                        follows(
                                mockRedirect(
                                        HttpStatus.MOVED_PERMANENTLY,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1,
                                URI.create("http:://example.com")),
                        notFollows(
                                mockRedirect(
                                        HttpStatus.TEMPORARY_REDIRECT,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1),
                        notFollows(
                                mockRedirect(
                                        HttpStatus.FOUND,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1)));

        assertThat(
                new Permanent(new NeverFollowStrategy()),
                allOf(
                        notFollows(
                                mockRedirect(
                                        HttpStatus.TEMPORARY_REDIRECT,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1),
                        notFollows(
                                mockRedirect(
                                        HttpStatus.FOUND,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1),
                        notFollows(
                                mockRedirect(
                                        HttpStatus.PERMANENT_REDIRECT,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1),
                        notFollows(
                                mockRedirect(
                                        HttpStatus.MOVED_PERMANENTLY,
                                        URI.create("http:://example.org"),
                                        URI.create("http:://example.com")),
                                1)));

    }
}