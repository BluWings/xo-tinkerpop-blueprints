package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
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
    public Vertex createEntity(TypeMetadataSet<EntityTypeMetadata<VertexMetadata>> types, Set<String> discriminators,
            Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> exampleEntity) {
        final Vertex vertex = graph.addVertex(null);
        for (final String discriminator : discriminators) {
            vertex.setProperty(XO_DISCRIMINATORS_PROPERTY + discriminator, discriminator);
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
        if (propertyMethodMetadata == null) {
            IndexedPropertyMethodMetadata<?> indexedProperty = entityTypeMetadata.getDatastoreMetadata().getIndexedProperty();
            if (indexedProperty == null) {
                throw new XOException("Type " + entityTypeMetadata.getAnnotatedType().getAnnotatedElement().getName() + " has no indexed property.");
            }
            propertyMethodMetadata = indexedProperty.getPropertyMethodMetadata();
        }
        PropertyMetadata propertyMetadata = propertyMethodMetadata.getDatastoreMetadata();
        Object value = entry.getValue();

        GraphQuery query = graph.query();
        query = query.has(XO_DISCRIMINATORS_PROPERTY + discriminator);

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
    public void flushEntity(Vertex entity) {
        // intentionally left empty
    }
}
