package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import com.buschmais.xo.spi.datastore.Datastore;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.util.GraphFactory;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class TinkerPopDatastore implements Datastore<TinkerPopDatastoreSession<Graph>, VertexMetadata, String, EdgeMetadata, String> {

    private Graph graph;

    public TinkerPopDatastore(final Configuration config) {
        graph = GraphFactory.open(config);
    }

    @Override
    public void init(Map<Class<?>, TypeMetadata> registeredMetadata) {
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
        // TODO: close()?
        // graph.shutdown();
        graph = null;
    }

}
