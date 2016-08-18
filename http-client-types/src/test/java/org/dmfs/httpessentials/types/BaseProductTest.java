/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.types;

import static org.junit.Assert.assertEquals;


/**
 * Base class for {@link Product} unit tests.
 *
 * @author Gabor Keszthelyi
 */
public abstract class BaseProductTest
{

    protected void assertNameVersionCommentAndToString(Product product, String name, String version, String comment, String toString)
    {
        assertEquals(name, product.name().toString());

        String actualVersion = product.version() == null ? null : product.version().toString();
        assertEquals(version, actualVersion);

        String actualComment = product.comment() == null ? null : product.comment().toString();
        assertEquals(comment, actualComment);

        assertEquals(toString, product.toString());

        StringBuilder sb = new StringBuilder();
        product.appendTo(sb);
        assertEquals(toString, sb.toString());
    }
}
