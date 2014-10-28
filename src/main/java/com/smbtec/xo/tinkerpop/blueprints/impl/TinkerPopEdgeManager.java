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

import java.util.Iterator;
import java.util.Map;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class TinkerPopEdgeManager extends AbstractTinkerPopPropertyManager<Edge> implements
        DatastoreRelationManager<Vertex, Object, Edge, EdgeMetadata, String, PropertyMetadata> {

    private final Graph graph;

    public TinkerPopEdgeManager(Graph graph) {
        this.graph = graph;
    }

    @Override
    public boolean isRelation(Object o) {
        return Edge.class.isAssignableFrom(o.getClass());
    }

    @Override
    public String getRelationDiscriminator(Edge relation) {
        return relation.getLabel();
    }

    @Override
    public Edge createRelation(Vertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            com.buschmais.xo.spi.metadata.type.RelationTypeMetadata.Direction direction, Vertex target,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        final String name = metadata.getDatastoreMetadata().getDiscriminator();
        Edge edge;
        switch (direction) {
        case FROM:
            edge = source.addEdge(name, target);
            break;
        case TO:
            edge = target.addEdge(name, source);
            break;
        default:
            throw new XOException("Unknown direction '" + direction.name() + "'.");
        }
        setProperties(edge, exampleEntity);
        return edge;
    }

    @Override
    public void deleteRelation(Edge relation) {
        relation.remove();
    }

    @Override
    public Object getRelationId(Edge relation) {
        return relation.getId();
    }

    @Override
    public Edge findRelationById(RelationTypeMetadata<EdgeMetadata> metadata, Object id) {
        return graph.getEdge(id);
    }

    @Override
    public void flushRelation(Edge relation) {
        // intentionally left empty
    }

    @Override
    public boolean hasSingleRelation(Vertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            RelationTypeMetadata.Direction direction) {
        final String label = metadata.getDatastoreMetadata().getDiscriminator();
        long count;
        switch (direction) {
        case FROM:
            count = source.query().direction(Direction.OUT).labels(label).count();
            break;
        case TO:
            count = source.query().direction(Direction.IN).labels(label).count();
            break;
        default:
            throw new XOException("Unkown direction '" + direction.name() + "'.");
        }
        if (count > 1) {
            throw new XOException("Multiple results are available.");
        }
        return count == 1;
    }

    @Override
    public Edge getSingleRelation(Vertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            RelationTypeMetadata.Direction direction) {
        final String label = metadata.getDatastoreMetadata().getDiscriminator();
        Iterable<Edge> edges;
        switch (direction) {
        case FROM:
            edges = source.getEdges(Direction.OUT, label);
            break;
        case TO:
            edges = source.getEdges(Direction.IN, label);
            break;
        default:
            throw new XOException("Unkown direction '" + direction.name() + "'.");
        }
        final Iterator<Edge> iterator = edges.iterator();
        if (!iterator.hasNext()) {
            throw new XOException("No result is available.");
        }
        final Edge result = iterator.next();
        if (iterator.hasNext()) {
            throw new XOException("Multiple results are available.");
        }
        return result;
    }

    @Override
    public Iterable<Edge> getRelations(Vertex source, RelationTypeMetadata<EdgeMetadata> metadata,
            RelationTypeMetadata.Direction direction) {
        final String label = metadata.getDatastoreMetadata().getDiscriminator();
        VertexQuery query = source.query();
        switch (direction) {
        case TO:
            query = query.direction(Direction.IN);
            break;
        case FROM:
            query = query.direction(Direction.OUT);
            break;
        default:
            throw new XOException("Unknown direction '" + direction.name() + "'.");
        }
        return query.labels(label).edges();
    }

    @Override
    public Vertex getFrom(final Edge relation) {
        return relation.getVertex(com.tinkerpop.blueprints.Direction.OUT);
    }

    @Override
    public Vertex getTo(final Edge relation) {
        return relation.getVertex(com.tinkerpop.blueprints.Direction.IN);
    }

}
