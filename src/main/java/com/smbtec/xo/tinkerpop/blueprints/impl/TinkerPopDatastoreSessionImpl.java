package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Gremlin;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.GraphQuery;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.Pipe;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class TinkerPopDatastoreSessionImpl implements TinkerPopDatastoreSession<Graph> {

    /**
     * This constant contains the prefix for discriminator properties.
     */
    public static final String XO_DISCRIMINATORS_PROPERTY = "_xo_discriminator_";

    private final Graph graph;

    public TinkerPopDatastoreSessionImpl(final Graph graph) {
        this.graph = graph;
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
    public boolean isEntity(final Object o) {
        return Vertex.class.isAssignableFrom(o.getClass());
    }

    @Override
    public boolean isRelation(final Object o) {
        return Edge.class.isAssignableFrom(o.getClass());
    }

    @Override
    public Set<String> getEntityDiscriminators(final Vertex entity) {
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
    public String getRelationDiscriminator(final Edge relation) {
        return relation.getLabel();
    }

    @Override
    public Object getEntityId(final Vertex entity) {
        return entity.getId();
    }

    @Override
    public Object getRelationId(final Edge relation) {
        return relation.getId();
    }

    @Override
    public Vertex createEntity(final TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types, final Set<String> discriminators) {
        final Vertex vertex = graph.addVertex(null);
        for (final String discriminator : discriminators) {
            vertex.setProperty(XO_DISCRIMINATORS_PROPERTY + discriminator, discriminator);
        }
        return vertex;
    }

    @Override
    public void deleteEntity(final Vertex entity) {
        entity.remove();
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
    public ResultIterator<Vertex> findEntity(final EntityTypeMetadata<VertexMetadata> type, final String discriminator, final Object value) {
        GraphQuery query = graph.query();
        query = query.has(XO_DISCRIMINATORS_PROPERTY + discriminator);

        IndexedPropertyMethodMetadata<?> indexedProperty = type.getDatastoreMetadata().getIndexedProperty();
        if (indexedProperty == null) {
            indexedProperty = type.getIndexedProperty();
        }
        if (indexedProperty == null) {
            throw new XOException("Type " + type.getAnnotatedType().getAnnotatedElement().getName() + " has no indexed property.");
        }
        final PrimitivePropertyMethodMetadata<PropertyMetadata> propertyMethodMetadata = indexedProperty.getPropertyMethodMetadata();
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
                iterator.remove();
            }

            @Override
            public void close() {
                // intentionally left empty
            }
        };
    }

    @Override
    public void migrateEntity(final Vertex entity, final TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types, final Set<String> discriminators,
            final TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> targetTypes, final Set<String> targetDiscriminators) {
        for (final String discriminator : discriminators) {
            if (!targetDiscriminators.contains(discriminator)) {
                entity.removeProperty(XO_DISCRIMINATORS_PROPERTY + discriminator);
            }
        }
        for (final String discriminator : targetDiscriminators) {
            if (!discriminators.contains(discriminator)) {
                entity.setProperty(XO_DISCRIMINATORS_PROPERTY + discriminator, discriminator);
            }
        }
    }

    @Override
    public void flushEntity(final Vertex entity) {
    }

    @Override
    public void flushRelation(final Edge relation) {
    }

    @Override
    public DatastorePropertyManager<Vertex, Edge, PropertyMetadata, EdgeMetadata> getDatastorePropertyManager() {
        return new TinkerPopPropertyManager();
    }

    @Override
    public void close() {
    }

    @Override
    public Graph getGraph() {
        return this.graph;
    }

}
