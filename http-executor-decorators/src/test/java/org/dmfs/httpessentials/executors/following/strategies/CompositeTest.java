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
import org.dmfs.jems.optional.elementary.Absent;
import org.dmfs.jems.optional.elementary.Present;
import org.junit.Test;

import java.net.URI;

import static org.dmfs.httpessentials.executors.following.strategies.matcher.RedirectStrategyFollowMatcher.follows;
import static org.dmfs.httpessentials.executors.following.strategies.matcher.RedirectStrategyFollowMatcher.mockRedirect;
import static org.dmfs.httpessentials.executors.following.strategies.matcher.RedirectStrategyFollowMatcher.notFollows;
import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link Composite}.
 *
 * @author marten
 */
public class CompositeTest
{
    @Test
    public void testFollows()
    {
        assertThat(
                new Composite(new FollowStrategy()),
                follows(
                        mockRedirect(
                                HttpStatus.FOUND,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        1,
                        URI.create("https://example.net")));
        assertThat(
                new Composite(
                        (response, redirectNumber) -> new Absent<>(),
                        (response, redirectNumber) -> new Present<>(URI.create("http://1.com"))),
                follows(
                        mockRedirect(
                                HttpStatus.FOUND,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        1,
                        URI.create("http://1.com")));
    }


    @Test
    public void testNotFollows()
    {
        assertThat(
                new Composite(new NeverFollowStrategy(), new NeverFollowStrategy(), new NeverFollowStrategy()),
                notFollows(
                        mockRedirect(
                                HttpStatus.OK,
                                URI.create("https://example.com/test"),
                                URI.create("https://example.net")),
                        1));
    }
}