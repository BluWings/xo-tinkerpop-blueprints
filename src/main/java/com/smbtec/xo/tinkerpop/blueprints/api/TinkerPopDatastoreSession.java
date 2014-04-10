package com.smbtec.xo.tinkerpop.blueprints.api;

import com.buschmais.xo.spi.datastore.DatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public interface TinkerPopDatastoreSession<G extends Graph> extends DatastoreSession<Object, Vertex, VertexMetadata, String, Object, Edge, EdgeMetadata, String> {

    G getGraph();

}
