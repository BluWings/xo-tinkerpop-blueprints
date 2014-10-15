/*
 * eXtended Objects - Tinkerpop Blueprints Binding
 *
 * Copyright (C) 2014 SMB GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.smbtec.xo.tinkerpop.blueprints.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.spi.annotation.EntityDefinition;

/**
 * This annotation marks a vertex as an entity.
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
@EntityDefinition
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Vertex {

    String DEFAULT_VALUE = "";

    /**
     * @return Returns the name of the type as {@link String}.
     */
    String value() default "";

    /**
     * @return The (super) type containing an indexed property ({@link Indexed}
     *         ).
     *         <p>
     *         An index will be created for this label and the indexed property
     *         and used by {@link XOManager#find(Class, Object)}.
     *         </p>
     */
    Class<?> usingIndexedPropertyOf() default Object.class;

}
