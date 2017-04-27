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

package org.dmfs.httpessentials.executors.following.policies;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.following.RedirectPolicy;
import org.junit.Test;

import java.net.URI;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Marten Gajda
 */
public class TemporaryTest
{
    /**
     * This test verifies that the policy affects temporary redirects only
     *
     * @throws Exception
     */
    @Test
    public void testAffectsTemporary() throws Exception
    {
        HttpResponse mockResponse = mock(HttpResponse.class);

        RedirectPolicy mockPolicy = mock(RedirectPolicy.class);
        when(mockPolicy.affects(mockResponse)).thenReturn(true);

        RedirectPolicy testPolicy = new Temporary(mockPolicy);

        when(mockResponse.status()).thenReturn(HttpStatus.TEMPORARY_REDIRECT);
        assertTrue(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.FOUND);
        assertTrue(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.SEE_OTHER);
        assertTrue(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.PERMANENT_REDIRECT);
        assertFalse(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.MOVED_PERMANENTLY);
        assertFalse(testPolicy.affects(mockResponse));
    }


    /**
     * Test that the affects(...) of the decorated policy is honoured.
     *
     * @throws Exception
     */
    @Test
    public void testAffects2() throws Exception
    {
        HttpResponse mockResponse = mock(HttpResponse.class);

        RedirectPolicy mockPolicy = mock(RedirectPolicy.class);
        when(mockPolicy.affects(mockResponse)).thenReturn(false);

        RedirectPolicy testPolicy = new Temporary(mockPolicy);

        when(mockResponse.status()).thenReturn(HttpStatus.TEMPORARY_REDIRECT);
        assertFalse(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.FOUND);
        assertFalse(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.SEE_OTHER);
        assertFalse(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.PERMANENT_REDIRECT);
        assertFalse(testPolicy.affects(mockResponse));

        when(mockResponse.status()).thenReturn(HttpStatus.MOVED_PERMANENTLY);
        assertFalse(testPolicy.affects(mockResponse));
    }


    /**
     * Test that the new location is returned properly.
     *
     * @throws Exception
     */
    @Test
    public void testLocation() throws Exception
    {
        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.status()).thenReturn(HttpStatus.TEMPORARY_REDIRECT);

        RedirectPolicy mockPolicy = mock(RedirectPolicy.class);
        when(mockPolicy.affects(mockResponse)).thenReturn(true);
        when(mockPolicy.location(mockResponse, 123)).thenReturn(URI.create("http://testlocation"));

        RedirectPolicy testPolicy = new Temporary(mockPolicy);

        assertEquals(URI.create("http://testlocation"), testPolicy.location(mockResponse, 123));
    }
}