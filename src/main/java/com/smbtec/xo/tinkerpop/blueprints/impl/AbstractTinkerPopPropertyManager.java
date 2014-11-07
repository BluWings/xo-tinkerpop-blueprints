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

import java.util.Map;

import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.smbtec.xo.tinkerpop.blueprints.impl.metadata.PropertyMetadata;
import com.tinkerpop.blueprints.Element;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 * @param <E>
 */
public class AbstractTinkerPopPropertyManager<E extends Element> implements DatastorePropertyManager<E, PropertyMetadata> {

    @Override
    public void setProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata, Object value) {
        element.setProperty(metadata.getDatastoreMetadata().getName(), value);
    }

    @Override
    public boolean hasProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return element.getProperty(metadata.getDatastoreMetadata().getName()) != null;
    }

    @Override
    public void removeProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        element.removeProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public Object getProperty(E element, PrimitivePropertyMethodMetadata<PropertyMetadata> metadata) {
        return element.getProperty(metadata.getDatastoreMetadata().getName());
    }

    protected void setProperties(E element, Map<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> example) {
        for (Map.Entry<PrimitivePropertyMethodMetadata<PropertyMetadata>, Object> entry : example.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                setProperty(element, entry.getKey(), value);
            }
        }
    }

}
