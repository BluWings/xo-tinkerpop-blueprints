package com.smbtec.xo.tinkerpop.blueprints.api;

import org.apache.commons.configuration.MapConfiguration;

import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.spi.bootstrap.XODatastoreProvider;
import com.buschmais.xo.spi.datastore.Datastore;
import com.smbtec.xo.tinkerpop.blueprints.impl.TinkerPopDatastore;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.EdgeMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.VertexMetadata;
import com.tinkerpop.blueprints.Graph;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class TinkerPopXOProvider implements XODatastoreProvider<VertexMetadata, String, EdgeMetadata, String> {

    @Override
    public Datastore<TinkerPopDatastoreSession<Graph>, VertexMetadata, String, EdgeMetadata, String> createDatastore(final XOUnit xoUnit) {
        if (xoUnit == null) {
            throw new IllegalArgumentException("XOUnit must not be null");
        }
        return new TinkerPopDatastore(new MapConfiguration(xoUnit.getProperties()));
    }

}
