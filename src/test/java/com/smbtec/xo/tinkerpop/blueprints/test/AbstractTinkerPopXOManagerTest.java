package com.smbtec.xo.tinkerpop.blueprints.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopDatastoreSession;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopXOProvider;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public abstract class AbstractTinkerPopXOManagerTest extends com.buschmais.xo.test.AbstractXOManagerTest {

	protected AbstractTinkerPopXOManagerTest(final XOUnit xoUnit) {
		super(xoUnit);
	}

	@Override
	protected void dropDatabase() {
		TinkerPopDatastoreSession session = getXoManager().getDatastoreSession(TinkerPopDatastoreSession.class);
		Iterable<Edge> edges = session.getGraph().getEdges();
		for (Edge edge : edges) {
			edge.remove();
		}
		Iterable<Vertex> vertices = session.getGraph().getVertices();
		for (Vertex vertex : vertices) {
			vertex.remove();
		}
	}

	protected static Collection<Object[]> xoUnits(final Class<?>... types) throws URISyntaxException {
		return xoUnits(Arrays.asList(types));
	}

	protected static Collection<Object[]> xoUnits(final List<? extends Class<?>> types) throws URISyntaxException {
		return xoUnits(types, Collections.<Class<?>> emptyList(), ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED,
				Transaction.TransactionAttribute.NONE);
	}

	protected static Collection<Object[]> xoUnits(final List<? extends Class<?>> types,
			final List<? extends Class<?>> instanceListenerTypes, final ValidationMode valiationMode,
			final ConcurrencyMode concurrencyMode, final Transaction.TransactionAttribute transactionAttribute)
			throws URISyntaxException {
		final Collection<Properties> blueprints = getBlueprints();
		final List<Object[]> xoUnits = new ArrayList<>(blueprints.size());
		for (final Properties blueprint : getBlueprints()) {
			final XOUnit unit = new XOUnit("default", "Default XO unit", new URI("blueprints:///"),
					TinkerPopXOProvider.class, new HashSet<>(types), instanceListenerTypes, valiationMode,
					concurrencyMode, transactionAttribute, blueprint);
			xoUnits.add(new Object[] { unit });
		}
		return xoUnits;
	}

	private static Collection<Properties> getBlueprints() {
		final Collection<Properties> blueprints = new HashSet<>();

		final Properties tinkergraph = new Properties();
		tinkergraph.setProperty("blueprints.graph", "com.tinkerpop.blueprints.impls.tg.TinkerGraph");
		blueprints.add(tinkergraph);

		final Properties orientdb = new Properties();
		orientdb.setProperty("blueprints.graph", "com.tinkerpop.blueprints.impls.orient.OrientGraph");
		orientdb.setProperty("blueprints.orientdb.url", "memory:tinkerpop");
		blueprints.add(orientdb);

		final Properties neo4j = new Properties();
		neo4j.setProperty("blueprints.graph", "com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph");
		neo4j.setProperty("blueprints.neo4j.directory", "target/neo4j");
		blueprints.add(neo4j);

		return blueprints;
	}
}
