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
package com.smbtec.xo.tinkerpop.blueprints.api;

import com.buschmais.xo.api.ResultIterable;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public interface TinkerPopRepository {

    /**
     * Find all instances of the given type.
     *
     * @param <T>
     *            The property type.
     * @param type
     *            The interface of the property type.
     * @return An {@link Iterable} returning the entity instances.
     */
    <T> ResultIterable<T> findAll(Class<T> type);

    /**
     * Return the number of instances of the given type.
     *
     * @param <T>
     *            The property type.
     * @param type
     *            The interface of the property type.
     * @return The number of entity instances.
     */
    <T> long count(Class<T> type);

    /**
     * Return an iterable to all entity instances in the graph.
     *
     * @return An {@link Iterable} returning all entity instances.
     */
    ResultIterable<?> vertices();

    /**
     * Return an iterable to all relationship instances in the graph.
     *
     * @return An {@link Iterable} returning relationship instances.
     */
    ResultIterable<?> edges();

}
