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

import org.dmfs.httpessentials.executors.urlrewriting.RewritePolicy;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.encoding.XWwwFormUrlEncoded;
import org.dmfs.rfc3986.parameters.Parameter;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.adapters.XwfueParameterList;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;
import org.dmfs.rfc3986.parameters.parametersets.Replacing;
import org.dmfs.rfc3986.queries.SimpleQuery;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc3986.uris.RelativeUri;
import org.dmfs.rfc3986.uris.Resolved;
import org.dmfs.rfc3986.uris.Text;

import java.net.URI;


/**
 * A {@link RewritePolicy} which adds or replaces the given parameters in every URL.
 * <p>
 * Note: this assumes an x-www-form-urlencoded query.
 *
 * @author Marten Gajda
 */
public final class Parametrizing implements RewritePolicy
{
    private final RewritePolicy mDelegate;
    private final Parameter[] mParameters;


    public Parametrizing(RewritePolicy delegate, Parameter... parameters)
    {
        mDelegate = delegate;
        mParameters = parameters;
    }


    @Override
    public URI rewritten(URI location)
    {
        // Note: this will look simpler when we switched to uri-toolkit

        // parse the given URI
        Uri uri = new LazyUri(new Precoded(location.toString()));
        // create updated ParameterList
        ParameterList parameters = uri.query().isPresent() ? new Replacing(
                new XwfueParameterList(uri.query().value()), mParameters) : new BasicParameterList(mParameters);

        // Replace parameters of old Uri before forwarding the result to the delegate
        return mDelegate.rewritten(
                URI.create(new Text(new Resolved(uri, new RelativeUri(new SimpleQuery(new XWwwFormUrlEncoded(parameters))))).toString())
        );
    }
}
