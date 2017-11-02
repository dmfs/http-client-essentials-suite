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

package org.dmfs.jems.charsequence.elementary;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author Marten Gajda
 */
public class HexTest
{
    @Test
    public void testLength() throws Exception
    {
        assertThat(new Hex(new byte[0]).length(), is(0));
        assertThat(new Hex(new byte[] { 1 }).length(), is(2));
        assertThat(new Hex(new byte[] { 1, 2, 3 }).length(), is(6));
    }


    @Test
    public void testCharAt() throws Exception
    {
        assertThat(new Hex(new byte[] { 1 }).charAt(0), is('0'));
        assertThat(new Hex(new byte[] { 1 }).charAt(1), is('1'));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).charAt(0), is('f'));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).charAt(1), is('f'));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).charAt(2), is('1'));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).charAt(3), is('2'));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).charAt(4), is('a'));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).charAt(5), is('9'));
    }


    @Test
    public void testSubSequence() throws Exception
    {
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).subSequence(1, 5).toString(), is("f12a"));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).subSequence(1, 5).charAt(1), is('1'));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).subSequence(1, 5).length(), is(4));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).subSequence(1, 5).subSequence(1, 3).toString(), is("12"));
    }


    @Test
    public void testToString() throws Exception
    {
        assertThat(new Hex(new byte[] {}).toString(), is(""));
        assertThat(new Hex(new byte[] { 1 }).toString(), is("01"));
        assertThat(new Hex(new byte[] { (byte) 0xff, 0x12, (byte) 0xa9 }).toString(), is("ff12a9"));
    }
}
