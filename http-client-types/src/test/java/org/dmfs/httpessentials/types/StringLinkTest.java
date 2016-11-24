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

package org.dmfs.httpessentials.types;

import org.junit.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


/**
 * @author Marten Gajda
 */
public class StringLinkTest
{

    @Test
    public void target() throws Exception
    {
        assertEquals(URI.create("test"), new StringLink("<test>").target());
        assertEquals(URI.create("test"), new StringLink("<test>; rel=testrel").target());
    }


    @Test
    public void context() throws Exception
    {
        assertEquals(URI.create("testcontext"), new StringLink("<test>").context(URI.create("testcontext")));
        assertEquals(URI.create("testcontext"), new StringLink("<test>; rel=testrel; anchor=\"testcontext\"").context(URI.create("fallbackcontext")));
    }


    @Test
    public void languages() throws Exception
    {
        assertEquals(Collections.emptySet(), new StringLink("<test>").languages());
        assertEquals(new HashSet<>(Arrays.asList(Locale.GERMANY)), new StringLink("<test>; hreflang=de-DE").languages());
        assertEquals(new HashSet<>(Arrays.asList(Locale.GERMANY, Locale.FRANCE)), new StringLink("<test>; hreflang=de-DE; hreflang=fr-FR").languages());
    }


    @Test
    public void title() throws Exception
    {
        assertEquals("", new StringLink("<test>").title());
        assertEquals("TITLE", new StringLink("<test>; title=\"TITLE\"").title());
    }


    @Test
    public void mediaType() throws Exception
    {
        assertNull(new StringLink("<test>").mediaType());
        assertEquals(new StructuredMediaType("text", "plain"), new StringLink("<test>; type=text/plain").mediaType());
        assertEquals(new StructuredMediaType("text", "plain"), new StringLink("<test>; type=\"text/plain\"").mediaType());
    }


    @Test
    public void testRelationTypes() throws Exception
    {
        assertEquals(new HashSet<>(), new StringLink("<test>").relationTypes());
        assertEquals(new HashSet<>(), new StringLink("<test>; type=\"text/plain\"").relationTypes());
        assertEquals(new HashSet<>(Arrays.asList("test0")), new StringLink("<test>; rel=test0").relationTypes());
        assertEquals(new HashSet<>(Arrays.asList("test0", "test1")), new StringLink("<test>; rel=\"test0 test1\"").relationTypes());
        assertEquals(new HashSet<>(Arrays.asList("test0", "test1", "test2")), new StringLink("<test>; rel=\"test0  test1 test2\"").relationTypes());
        // all but the first rel param should be ignored
        assertEquals(new HashSet<>(Arrays.asList("test0", "test1", "test2")),
                new StringLink("<test>; rel=\"test0  test1 test2\"; rel=\"invalid\"").relationTypes());
    }


    @Test
    public void reverseRelationTypes() throws Exception
    {
        assertEquals(new HashSet<>(), new StringLink("<test>").reverseRelationTypes());
        assertEquals(new HashSet<>(), new StringLink("<test>; type=\"text/plain\"").reverseRelationTypes());
        assertEquals(new HashSet<>(Arrays.asList("test0")), new StringLink("<test>; rev=test0").reverseRelationTypes());
        assertEquals(new HashSet<>(Arrays.asList("test0", "test1")), new StringLink("<test>; rev=\"test0 test1\"").reverseRelationTypes());
        assertEquals(new HashSet<>(Arrays.asList("test0", "test1", "test2")), new StringLink("<test>; rev=\"test0  test1 test2\"").reverseRelationTypes());
    }

}