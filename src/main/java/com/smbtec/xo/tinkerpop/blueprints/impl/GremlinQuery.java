package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.util.HashMap;
import java.util.Iterator;
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
import com.tinkerpop.pipes.util.structures.Table;

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

            if (result instanceof Table) {
                throw new UnsupportedOperationException("Result of type 'Table' not yet supported");
            } else if (result instanceof Iterable) {
                return convertIterator(((Iterable<?>) result).iterator());
            } else if (result instanceof Iterator) {
                return convertIterator((Iterator<?>) result);
            } else if (result instanceof Map) {
                throw new UnsupportedOperationException("Result of type 'Map' not yet supported");
            }
            return convertSingleObject(result);

        } catch (Exception e) {
            throw new XOException(e.getMessage(), e);
        }
    }

    private ResultIterator<Map<String, Object>> convertSingleObject(final Object data) {
        return new ResultIterator<Map<String, Object>>() {

            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Map<String, Object> next() {
                Map<String, Object> result = new HashMap<>();
                if (data instanceof Vertex) {
                    result.put(NODE_COLUMN_NAME, data);
                } else if (data instanceof Edge) {
                    result.put(EDGE_COLUMN_NAME, data);
                } else if (data instanceof Graph) {
                    result.put(GRAPH_COLUMN_NAME, ((Graph) data).toString());
                }
                hasNext = false;
                return result;
            }

            @Override
            public void remove() {
                throw new XOException("Remove operation is not supported for query results.");
            }

            @Override
            public void close() {
            }
        };
    }

    private ResultIterator<Map<String, Object>> convertIterator(final Iterator<?> iterator) {
        return new ResultIterator<Map<String, Object>>() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Map<String, Object> next() {
                Object data = iterator.next();
                Map<String, Object> result = new HashMap<>();
                if (data instanceof Vertex) {
                    result.put(NODE_COLUMN_NAME, data);
                } else if (data instanceof Edge) {
                    result.put(EDGE_COLUMN_NAME, data);
                } else if (data instanceof Graph) {
                    result.put(GRAPH_COLUMN_NAME, ((Graph) data).toString());
                }
                return result;
            }

            @Override
            public void remove() {
                throw new XOException("Remove operation is not supported for query results.");
            }

            @Override
            public void close() {
            }
        };
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

}
