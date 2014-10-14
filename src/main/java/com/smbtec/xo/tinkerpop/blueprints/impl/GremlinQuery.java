package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 *
 */
public class GremlinQuery implements DatastoreQuery<Gremlin> {

    private static final String NODE_COLUMN_NAME = "node";
    private static final String EDGE_COLUMN_NAME = "relationship";
    private static final String GRAPH_COLUMN_NAME = "graph";

    private static final String g = "g";
    private static final String GREMLIN_GROOVY = "gremlin-groovy";

    private Graph tinkerPopGraph;

    private ScriptEngine engine;

    public GremlinQuery(TinkerPopDatastoreSession<Graph> session) {
        tinkerPopGraph = session.getGraph();
        engine = new ScriptEngineManager().getEngineByName(GREMLIN_GROOVY);
    }

    @Override
    public ResultIterator<Map<String, Object>> execute(final String script, final Map<String, Object> parameters) {
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
    public ResultIterator<Map<String, Object>> execute(final Gremlin query, final Map<String, Object> parameters) {
        return execute(query.value(), parameters);
    }

    private Bindings createBindings(Map params, Graph graph) {
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

    protected static class IterableResultIterator implements ResultIterator<Map<String, Object>> {

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
            Map<String, Object> map = new HashMap<>();
            map.put("", entityRepresentation(iterator.next()));
            return map;
        }

        @Override
        public void close() {
        }

        @Override
        public void remove() {
        }

    }

    protected static class SimpleResultIterator implements ResultIterator<Map<String, Object>> {

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
            Map<String, Object> map = new HashMap<>();
            map.put("", entityRepresentation(entity));
            hasNext = false;
            return map;
        }

        @Override
        public void close() {
        }

        @Override
        public void remove() {
        }

    }

    public static Object entityRepresentation(Object entity) {
        if (entity instanceof Vertex) {
            return entity;
        } else if (entity instanceof Edge) {
            return entity;
        } else if (entity instanceof Graph) {
            return entity;
        } else if (entity instanceof Double || entity instanceof Float) {
            return ((Number) entity).doubleValue();
        } else if (entity instanceof Long || entity instanceof Integer) {
            return ((Number) entity).longValue();
        } else if (entity instanceof BigDecimal) {
            return ((BigDecimal) entity).doubleValue();
        } else if (entity == null) {
            return null;
        } else if (entity instanceof Iterable<?>) {
            List<Object> representation = new ArrayList<Object>();
            for (final Object r : (Iterable<?>) entity) {
                representation.add(entityRepresentation(r));
            }
            return representation;
        }
        return entity.toString();
    }
}
