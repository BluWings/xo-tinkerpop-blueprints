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
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.buschmais.xo.spi.metadata.type.RepositoryTypeMetadata;
import com.buschmais.xo.spi.session.XOSession;
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
        return null;
    }

    @Override
    public DatastoreEntityManager<Object, Vertex, VertexMetadata, String, PropertyMetadata> getDatastoreEntityManager() {
        return null;
    }

    @Override
    public DatastoreRelationManager<Vertex, Object, Edge, EdgeMetadata, String, PropertyMetadata> getDatastoreRelationManager() {
        return null;
    }

    @Override
    public Class<? extends Annotation> getDefaultQueryLanguage() {
        return null;
    }

    @Override
    public <QL extends Annotation> DatastoreQuery<QL> createQuery(Class<QL> queryLanguage) {
        return null;
    }

    @Override
    public <R> R createRepository(XOSession xoSession, RepositoryTypeMetadata repositoryTypeMetadata) {
        return null;
    }

    @Override
    public void close() {
    }

    @Override
    public Graph getGraph() {
        return this.graph;
    }

}
