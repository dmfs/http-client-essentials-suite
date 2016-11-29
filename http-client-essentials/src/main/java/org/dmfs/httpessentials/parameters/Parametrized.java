/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.parameters;

import java.util.Iterator;


/**
 * Interface of an object that has typed parameters.
 *
 * @author Marten Gajda
 */
public interface Parametrized
{
    /**
     * Returns the first {@link Parameter} of the given type or a parameter with a default value if no such parameter exists.
     *
     * @param parameterType
     *         The {@link ParameterType}.
     * @param defaultValue
     *         The value to return in case no such parameter is present.
     *
     * @return The {@link Parameter}.
     */
    public <T> Parameter<T> firstParameter(ParameterType<T> parameterType, T defaultValue);

    /**
     * Returns an {@link Iterator} of all {@link Parameter}s of the given {@link ParameterType}. Returns an empty {@link Iterator} of no such parameter exists.
     *
     * @param parameterType
     *         The {@link ParameterType} of the parameters to return.
     *
     * @return An {@link Iterator} of {@link Parameter}s.
     */
    public <T> Iterator<Parameter<T>> parameters(ParameterType<T> parameterType);

    /**
     * Checks whether this object has a {@link Parameter} of the given {@link ParameterType}.
     *
     * @param parameterType
     *         The {@link ParameterType} to check.
     *
     * @return <code>true</code> if such a parameter is present, <code>false</code> otherwise.
     */
    public <T> boolean hasParameter(ParameterType<T> parameterType);
}
