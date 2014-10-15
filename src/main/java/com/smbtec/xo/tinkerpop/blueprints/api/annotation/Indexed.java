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
import com.buschmais.xo.spi.annotation.IndexDefinition;

/**
 * Marks a property as to be indexed.
 * <p>
 * An indexed property is used to find instances using
 * {@link XOManager#find(Class, Object)}.
 * </p>
 */
@IndexDefinition
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Indexed {

    boolean unique() default false;

}