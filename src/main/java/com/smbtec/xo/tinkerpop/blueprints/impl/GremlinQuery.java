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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Gremlin;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.structures.Row;
import com.tinkerpop.pipes.util.structures.Table;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class GremlinQuery implements DatastoreQuery<Gremlin> {

    public static final String NODE_COLUMN_NAME = "node";
    public static final String EDGE_COLUMN_NAME = "relationship";
    public static final String GRAPH_COLUMN_NAME = "graph";

    private static final String g = "g";
    private static final String GREMLIN_GROOVY = "gremlin-groovy";

    private Graph tinkerPopGraph;

    private static ScriptEngine engine;

    public GremlinQuery(TinkerPopDatastoreSession<Graph> session) {
        this(session.getGraph());
    }

    public GremlinQuery(Graph graph) {
        tinkerPopGraph = graph;
        engine = new ScriptEngineManager().getEngineByName(GREMLIN_GROOVY);
    }

    @Override
    public ResultIterator<Map<String, Object>> execute(final String script,
            final Map<String, Object> parameters) {
        try {
            final Bindings bindings = createBindings(parameters, tinkerPopGraph);
            final Object result = engine.eval(script, bindings);

            if (result instanceof Iterable<?>) {
                return new IterableResultIterator((Iterable<?>) result);
            } else {
                return new SimpleResultIterator(result);
            }
        } catch (Exception e) {
            throw new XOException(e.getMessage(), e);
        }
    }

    @Override
    public ResultIterator<Map<String, Object>> execute(final Gremlin query,
            final Map<String, Object> parameters) {
        return execute(query.value(), parameters);
    }

    private Bindings createBindings(Map<String, Object> params, Graph graph) {
        final Bindings bindings = createInitialBinding(graph);
        if (params != null) {
            bindings.putAll(params);
        }
        return bindings;
    }

    private Bindings createInitialBinding(Graph graph) {
        final Bindings bindings = new SimpleBindings();
        bindings.put(g, graph);
        return bindings;
    }

    protected class IterableResultIterator implements
            ResultIterator<Map<String, Object>> {

        private Iterator<?> iterator;

        public IterableResultIterator(Iterable<?> iterable) {
            this.iterator = iterable.iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Map<String, Object> next() {
            return entityRepresentation(iterator.next());
        }

        @Override
        public void close() {
        }

        @Override
        public void remove() {
            throw new XOException(
                    "Remove operation is not supported for query results.");
        }

    }

    protected class SimpleResultIterator implements
            ResultIterator<Map<String, Object>> {

        private Object entity;
        private boolean hasNext = true;

        public SimpleResultIterator(Object entity) {
            this.entity = entity;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Map<String, Object> next() {
            hasNext = false;
            return entityRepresentation(entity);
        }

        @Override
        public void close() {
        }

        @Override
        public void remove() {
            throw new XOException(
                    "Remove operation is not supported for query results.");
        }

    }

    public Map<String, Object> entityRepresentation(Object entity) {
        Map<String, Object> map = new HashMap<>();
        if (entity instanceof Vertex) {
            map.put(NODE_COLUMN_NAME, entity);
        } else if (entity instanceof Edge) {
            map.put(EDGE_COLUMN_NAME, entity);
        } else if (entity instanceof Graph) {
            map.put(GRAPH_COLUMN_NAME, entity);
        } else if (entity instanceof Double || entity instanceof Float) {
            map.put("", ((Number) entity).doubleValue());
        } else if (entity instanceof Long || entity instanceof Integer) {
            map.put("", ((Number) entity).longValue());
        } else if (entity instanceof BigDecimal) {
            map.put("", ((BigDecimal) entity).doubleValue());
        } else if (entity == null) {
            map.put("", null);
        } else if (entity instanceof Table) {
            final Table table = (Table) entity;
            final Iterator<Row> rows = table.iterator();
            List<Object> resultRows = new LinkedList<>();
            while (rows.hasNext()) {
                resultRows.add(entityRepresentation(rows.next()));
            }
            map.put("", resultRows);
        } else if (entity instanceof Row) {
            final Row row = (Row) entity;
            final List<String> columnNames = row.getColumnNames();
            final Map<String, Object> resultRow = new HashMap<String, Object>();
            for (String columnName : columnNames) {
                resultRow.put(columnName,
                        entityRepresentation(row.getColumn(columnName)));
            }
            map.put("", resultRow);
        } else if (entity instanceof Map<?, ?>) {
            map.putAll((Map<? extends String, ? extends Object>) entity);
        } else if (entity instanceof Iterable<?>) {
            List<Object> representation = new ArrayList<Object>();
            for (final Object r : (Iterable<?>) entity) {
                representation.add(entityRepresentation(r));
            }
        } else {
            map.put("", entity.toString());
        }
        return map;
    }
}
