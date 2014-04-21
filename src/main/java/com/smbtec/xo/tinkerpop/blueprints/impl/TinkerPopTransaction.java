package com.smbtec.xo.tinkerpop.blueprints.impl;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.tinkerpop.blueprints.Graph;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class TinkerPopTransaction implements DatastoreTransaction {

    private boolean active = false;

    private final Graph graph;

    public TinkerPopTransaction(final Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph must not be null");
        }
        this.graph = graph;
    }

    @Override
    public void begin() {
        if (active) {
            throw new XOException("There is already an active transaction");
        }
        active = true;
    }

    @Override
    public void commit() {
        active = false;
        graph.tx().commit();
    }

    @Override
    public void rollback() {
        active = false;
        graph.tx().rollback();
    }

    @Override
    public boolean isActive() {
        return active;
    }

}
