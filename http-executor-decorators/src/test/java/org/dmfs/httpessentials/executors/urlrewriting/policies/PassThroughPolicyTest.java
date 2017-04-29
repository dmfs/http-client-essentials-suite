/*
 * Copyright 2017 dmfs GmbH
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

package org.dmfs.httpessentials.executors.urlrewriting.policies;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;


/**
 * @author Marten Gajda
 */
public class PassThroughPolicyTest
{
    @Test
    public void testRewritten() throws Exception
    {
        // we should always get in return what we put in
        assertEquals(URI.create("http://example.com"), new PassThroughPolicy().rewritten(URI.create("http://example.com")));
        assertEquals(URI.create("http:example.com"), new PassThroughPolicy().rewritten(URI.create("http:example.com")));
        assertEquals(URI.create("example.com"), new PassThroughPolicy().rewritten(URI.create("example.com")));
    }

}