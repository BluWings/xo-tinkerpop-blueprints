package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.util.Iterator;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.query.VertexQuery;
import com.tinkerpop.blueprints.util.ElementHelper;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class TinkerPopPropertyManager implements DatastorePropertyManager<Vertex, Edge, PropertyMetadata, EdgeMetadata> {

}
