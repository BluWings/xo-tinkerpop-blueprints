package com.smbtec.xo.tinkerpop.blueprints.impl;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Dirk Mahler
 *
 */
public class DefaultTransaction implements DatastoreTransaction {

    private boolean active = false;

    @Override
    public void begin() {
        if (active) {
            throw new XOException("There is already an active transaction.");
        }
        active = true;
    }

    @Override
    public void commit() {
        active = false;
    }

    @Override
    public void rollback() {
        active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

}
