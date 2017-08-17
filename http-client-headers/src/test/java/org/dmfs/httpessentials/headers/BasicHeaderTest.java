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

package org.dmfs.httpessentials.headers;

import org.dmfs.httpessentials.converters.IntegerConverter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class BasicHeaderTest
{

    @Test
    public void testType()
    {
        HeaderType<Integer> type = new BasicSingletonHeaderType<Integer>("TestHeader", IntegerConverter.INSTANCE);
        BasicHeader<Integer> simpleHeader = new BasicHeader<Integer>(type, 1234);
        assertEquals(type, simpleHeader.type());

        HeaderType<List<Integer>> listType = new BasicListHeaderType<Integer>("TestHeader", IntegerConverter.INSTANCE);
        BasicHeader<List<Integer>> listHeader = new BasicHeader<List<Integer>>(listType,
                Arrays.asList(new Integer[] { 1, 2, 3, 10 }));
        assertEquals(listType, listHeader.type());
    }


    @Test
    public void testValue()
    {
        HeaderType<Integer> type = new BasicSingletonHeaderType<Integer>("TestHeader", IntegerConverter.INSTANCE);
        BasicHeader<Integer> simpleHeader = new BasicHeader<Integer>(type, 1234);
        assertEquals((Integer) 1234, simpleHeader.value());

        HeaderType<List<Integer>> listType = new BasicListHeaderType<Integer>("TestHeader", IntegerConverter.INSTANCE);
        BasicHeader<List<Integer>> listHeader = new BasicHeader<List<Integer>>(listType,
                Arrays.asList(new Integer[] { 1, 2, 3, 10 }));
        assertEquals(Arrays.asList(new Integer[] { 1, 2, 3, 10 }), listHeader.value());

    }


    @Test
    public void testToString()
    {
        HeaderType<Integer> type = new BasicSingletonHeaderType<Integer>("TestHeader", IntegerConverter.INSTANCE);
        BasicHeader<Integer> simpleHeader = new BasicHeader<Integer>(type, 1234);
        assertEquals("1234", simpleHeader.toString());

        HeaderType<List<Integer>> listType = new BasicListHeaderType<Integer>("TestHeader", IntegerConverter.INSTANCE);
        BasicHeader<List<Integer>> listHeader = new BasicHeader<List<Integer>>(listType,
                Arrays.asList(new Integer[] { 1, 2, 3, 10 }));
        assertEquals("1,2,3,10", listHeader.toString());
    }

}
