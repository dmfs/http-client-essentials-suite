/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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

/**
 * A basic {@link Parameter} implementation.
 *
 * @param <ValueType>
 *         The parameter value type.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class BasicParameter<ValueType> implements Parameter<ValueType>
{
    private final ValueType mValue;
    private final ParameterType<ValueType> mType;


    /**
     * Creates a new {@link Parameter} of the given type and value.
     *
     * @param type
     *         The {@link ParameterType}.
     * @param value
     *         The parameter value.
     */
    public BasicParameter(ParameterType<ValueType> type, ValueType value)
    {
        mType = type;
        mValue = value;
    }


    @Override
    public ValueType value()
    {
        return mValue;
    }


    @Override
    public ParameterType<ValueType> type()
    {
        return mType;
    }


    @Override
    public String toString()
    {
        return mType.valueString(mValue);
    }

}
