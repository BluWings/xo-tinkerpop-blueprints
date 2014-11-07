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

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.xo.spi.datastore.Datastore;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.method.MethodMetadata;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphFactory;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Parameter;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class TinkerPopDatastore implements Datastore<TinkerPopDatastoreSession<Graph>, VertexMetadata, String, EdgeMetadata, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinkerPopDatastore.class);

    private Graph graph;

    public TinkerPopDatastore(final Configuration config) {
        graph = GraphFactory.open(config);
    }

    @Override
    public void init(Map<Class<?>, TypeMetadata> registeredMetadata) {
        for (TypeMetadata typeMetadata : registeredMetadata.values()) {
            if (typeMetadata instanceof EntityTypeMetadata) {
                if (graph.getFeatures().supportsKeyIndices) {
                    @SuppressWarnings("unchecked")
                    EntityTypeMetadata<VertexMetadata> entityTypeMetadata = (EntityTypeMetadata<VertexMetadata>) typeMetadata;
                    for (MethodMetadata<?, ?> methodMetadata : entityTypeMetadata.getProperties()) {
                        if (methodMetadata instanceof PrimitivePropertyMethodMetadata<?>) {
                            @SuppressWarnings("unchecked")
                            PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMetadata = (PrimitivePropertyMethodMetadata<PropertyMetadata>) methodMetadata;
                            ensureIndex(entityTypeMetadata.getDatastoreMetadata().getDiscriminator(), propertyMetadata);
                        }
                    }
                }
            }
        }
    }

    @Override
    public DatastoreMetadataFactory<VertexMetadata, String, EdgeMetadata, String> getMetadataFactory() {
        return new TinkerPopMetadataFactory();
    }

    @Override
    public TinkerPopDatastoreSession<Graph> createSession() {
        return new TinkerPopDatastoreSessionImpl(graph);
    }

    @Override
    public void close() {
        graph.shutdown();
        graph = null;
    }

    private void ensureIndex(String discriminator, PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMetadata) {
        PropertyMetadata datastoreMetadata = propertyMetadata.getDatastoreMetadata();
        Class<? extends Element> elementType = datastoreMetadata.getElementType();
        if (elementType != null) {
            String propertyName = datastoreMetadata.getName();
            Parameter<String, String>[] parameters = datastoreMetadata.getParameters();
            LOGGER.info("Creating key index for type {}({}) on property '{}' with parameters {}.", discriminator,
                    elementType.getSimpleName(), propertyName, parameters);
            ((KeyIndexableGraph) graph).createKeyIndex(propertyName, elementType, parameters);
        }
    }

}
