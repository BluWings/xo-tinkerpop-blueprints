package com.smbtec.xo.tinkerpop.blueprints.test.index;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.index.composite.Book;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

@RunWith(Parameterized.class)
public class IndexTest extends AbstractTinkerPopXOManagerTest {

    public IndexTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(Book.class);
    }

    @Test
    public void testKeyIndexCreation() {
        XOManager xoManager = getXoManager();

        TinkerPopDatastoreSession<?> datastoreSession = xoManager.getDatastoreSession(TinkerPopDatastoreSession.class);
        Graph graph = datastoreSession.getGraph();
        if (graph.getFeatures().supportsKeyIndices) {
            KeyIndexableGraph keyIndexableGraph = (KeyIndexableGraph) graph;
            Set<String> indexedKeys = keyIndexableGraph.getIndexedKeys(Vertex.class);
            assertThat(indexedKeys, notNullValue());
            assertThat(indexedKeys, hasSize(2));
        }

    }

}
