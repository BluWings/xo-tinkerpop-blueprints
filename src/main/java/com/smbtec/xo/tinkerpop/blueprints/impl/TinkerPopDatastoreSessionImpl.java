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

import java.lang.annotation.Annotation;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Gremlin;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class TinkerPopDatastoreSessionImpl implements TinkerPopDatastoreSession<Graph> {

    private final Graph graph;
    private final TinkerPopVertexManager vertexManager;
    private final TinkerPopEdgeManager edgeManager;

    public TinkerPopDatastoreSessionImpl(final Graph graph) {
        this.graph = graph;
        this.vertexManager = new TinkerPopVertexManager(graph);
        this.edgeManager = new TinkerPopEdgeManager();

    }

    @Override
    public DatastoreTransaction getDatastoreTransaction() {
        if (graph.getFeatures().supportsTransactions) {
            return new TinkerPopTransaction((TransactionalGraph) graph);
        } else {
            return new DefaultTransaction();
        }
    }

    @Override
    public Class<? extends Annotation> getDefaultQueryLanguage() {
        return Gremlin.class;
    }

    @Override
    public <QL extends Annotation> DatastoreQuery<QL> createQuery(final Class<QL> queryLanguage) {
        if (Gremlin.class.equals(queryLanguage)) {
            return (DatastoreQuery<QL>) new GremlinQuery(this);
        }
        throw new XOException("Unsupported query language: " + queryLanguage.getName());
    }

    @Override
    public void close() {
    }

    @Override
    public Graph getGraph() {
        return this.graph;
    }

    @Override
    public DatastoreEntityManager<Object, Vertex, VertexMetadata, String, PropertyMetadata> getDatastoreEntityManager() {
        return vertexManager;
    }

    @Override
    public DatastoreRelationManager<Vertex, Object, Edge, EdgeMetadata, String, PropertyMetadata> getDatastoreRelationManager() {
        return edgeManager;
    }

}
