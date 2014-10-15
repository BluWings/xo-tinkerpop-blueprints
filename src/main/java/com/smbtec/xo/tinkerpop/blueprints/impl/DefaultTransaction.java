/*
 * eXtended Objects - Tinkerpop Blueprints Binding
 *
 * Copyright (C) 2014 SMB GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
