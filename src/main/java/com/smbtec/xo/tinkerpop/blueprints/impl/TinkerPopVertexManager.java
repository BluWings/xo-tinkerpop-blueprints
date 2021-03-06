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
package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.Vertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class TinkerPopVertexManager extends AbstractTinkerPopPropertyManager<Vertex> implements
        DatastoreEntityManager<Object, Vertex, VertexMetadata, String, PropertyMetadata> {

    /**
     * This constant contains the prefix for discriminator properties.
     */
    public static final String XO_DISCRIMINATORS_PROPERTY = "_xo_discriminator_";

    private final Graph graph;

    public TinkerPopVertexManager(Graph graph) {
        this.graph = graph;
    }

    @Override
    public boolean isEntity(Object o) {
        return Vertex.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Set<String> getEntityDiscriminators(Vertex entity) {
        final Set<String> discriminators = new HashSet<>();
        for (final String key : entity.getPropertyKeys()) {
            if (key.startsWith(XO_DISCRIMINATORS_PROPERTY)) {
                final String discriminator = entity.getProperty(key);
                discriminators.add(discriminator);
            }
        }
        if (discriminators.isEmpty()) {
            throw new XOException("A vertex was found without discriminators. Does another framework alter the database?");
        }
        return discriminators;
    }

    @Override
    public Object getEntityId(Vertex entity) {
        return entity.getId();
    }

    @Override
    public Vertex findEntityById(EntityTypeMetadata<VertexMetadata> metadata, String discriminator, Object id) {
        return graph.getVertex(id);
    }

    @Override
    public Vertex createEntity(TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types, Set<String> discriminators,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        final Vertex vertex = graph.addVertex(null);
        for (final String discriminator : discriminators) {
            vertex.setProperty(getDiscriminatorPropertyKey(discriminator), discriminator);
        }
        for (Map.Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> entry : exampleEntity.entrySet()) {
            setProperty(vertex, entry.getKey(), entry.getValue());
        }
        return vertex;
    }

    @Override
    public void deleteEntity(Vertex entity) {
        entity.remove();
    }

    @Override
    public ResultIterator<Vertex> findEntity(EntityTypeMetadata<VertexMetadata> entityTypeMetadata, String discriminator,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> values) {
        if (values.size() > 1) {
            throw new XOException("Only one property value is supported for find operation");
        }
        Map.Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> entry = values.entrySet().iterator().next();
        PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMethodMetadata = entry.getKey();
        PropertyMetadata propertyMetadata = propertyMethodMetadata.getDatastoreMetadata();
        Object value = entry.getValue();

        GraphQuery query = graph.query();
        query = query.has(getDiscriminatorPropertyKey(discriminator));
        query = query.has(propertyMethodMetadata.getDatastoreMetadata().getName(), value);
        final Iterable<Vertex> vertices = query.vertices();
        final Iterator<Vertex> iterator = vertices.iterator();

        return new ResultIterator<Vertex>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Vertex next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new XOException("Remove operation is not supported for find results.");
            }

            @Override
            public void close() {
                // intentionally left blank
            }
        };
    }

    @Override
    public void migrateEntity(Vertex entity, TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types, Set<String> discriminators,
            TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> targetTypes, Set<String> targetDiscriminators) {
        for (final String discriminator : discriminators) {
            if (!targetDiscriminators.contains(discriminator)) {
                entity.removeProperty(getDiscriminatorPropertyKey(discriminator));
            }
        }
        for (final String discriminator : targetDiscriminators) {
            if (!discriminators.contains(discriminator)) {
                entity.setProperty(getDiscriminatorPropertyKey(discriminator), discriminator);
            }
        }
    }

    @Override
    public void flushEntity(Vertex entity) {
        // intentionally left empty
    }

    /**
     * Returns the property key of the given entity discriminator.
     *
     * @param discriminator
     * @return the property key
     */
    public static String getDiscriminatorPropertyKey(String discriminator) {
        return XO_DISCRIMINATORS_PROPERTY + discriminator;
    }
}
