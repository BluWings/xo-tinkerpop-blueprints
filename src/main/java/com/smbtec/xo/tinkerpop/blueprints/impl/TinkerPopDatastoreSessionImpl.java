package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Graph.Features.GraphFeatures;
import com.tinkerpop.blueprints.Property;
import com.tinkerpop.blueprints.query.GraphQuery;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.process.Step;

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
        if (graph.getFeatures().supports(GraphFeatures.class, GraphFeatures.FEATURE_TRANSACTIONS)) {
            return new TinkerPopTransaction(graph);
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
                final Property<Object> property = entity.getProperty(key);
                discriminators.add((String) property.get());
            }
        }
        if (discriminators.size() == 0) {
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
    public <QL> ResultIterator<Map<String, Object>> executeQuery(final QL query, final Map<String, Object> parameters) {
        final GremlinExpression gremlinExpression = GremlinManager.getGremlinExpression(query, parameters);
        final String expression = gremlinExpression.getExpression();
        final Step step = com.tinkerpop.gremlin.groovy.GremlinLoader.compile(expression);
        if (parameters.containsKey("this")) {
            final Object setThis = parameters.get("this");
            if (Vertex.class.isAssignableFrom(setThis.getClass())) {
                final Vertex vertex = (Vertex) setThis;
                step.addStarts(Arrays.asList(vertex));
            } else if (Edge.class.isAssignableFrom(setThis.getClass())) {
                final Edge edge = (Edge) setThis;
                step.addStarts(Arrays.asList(edge.getVertex(Direction.IN), edge.getVertex(Direction.OUT)));
            } else {
                throw new XOException("Unsupported start point '" + String.valueOf(setThis) + "' (class=" + setThis.getClass() + ")");
            }
        } else {
            step.addStarts(graph.query().vertices());
        }
        return new ResultIterator<Map<String, Object>>() {

            @Override
            public boolean hasNext() {
                return step.hasNext();
            }

            @Override
            public Map<String, Object> next() {
                final Map<String, Object> results = new HashMap<>();
                final Object next = step.next();
                if (next instanceof Vertex) {
                    results.put(gremlinExpression.getResultName(), next);
                } else if (next instanceof Edge) {
                    results.put(gremlinExpression.getResultName(), next);
                } else if (next instanceof Map) {
                    @SuppressWarnings("unchecked")
                    final Map<String, Object> map = (Map<String, Object>) next;
                    results.putAll(map);
                } else {
                    results.put("unknown_type", next);
                }
                return results;
            }

            @Override
            public void remove() {
                step.remove();
            }

            @Override
            public void close() {
                // there is no close required in pipe
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
