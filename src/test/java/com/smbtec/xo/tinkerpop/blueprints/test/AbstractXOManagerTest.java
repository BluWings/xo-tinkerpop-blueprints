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

import org.junit.After;
import org.junit.Before;

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XO;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopXOProvider;

public class AbstractXOManagerTest {

    private final XOUnit xoUnit;
    private XOManagerFactory xoManagerFactory;
    private XOManager xoManager = null;

    protected AbstractXOManagerTest(final XOUnit xoUnit) {
        this.xoUnit = xoUnit;
    }

    @Before
    public void createXOManagerFactory() throws URISyntaxException {
        xoManagerFactory = XO.createXOManagerFactory(xoUnit);
        dropDatabase();
    }

    @After
    public void closeXOManagerFactory() {
        closeXOmanager();
        if (xoManagerFactory != null) {
            xoManagerFactory.close();
        }
    }

    protected static Collection<Object[]> xoUnits(final Class<?>... types) {
        return xoUnits(Arrays.asList(types), Collections.<Class<?>> emptyList(), ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED,
                Transaction.TransactionAttribute.MANDATORY);
    }

    protected static Collection<Object[]> xoUnits(final List<? extends Class<?>> types) {
        return xoUnits(types, Collections.<Class<?>> emptyList(), ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED,
                Transaction.TransactionAttribute.MANDATORY);
    }

    protected static Collection<Object[]> xoUnits(final List<? extends Class<?>> types, final List<? extends Class<?>> instanceListenerTypes,
            final ValidationMode valiationMode, final ConcurrencyMode concurrencyMode, final Transaction.TransactionAttribute transactionAttribute) {
        final Collection<Properties> blueprints = getBlueprints();
        final List<Object[]> xoUnits = new ArrayList<>(blueprints.size());
        for (final Properties blueprint : getBlueprints()) {
            XOUnit unit;
            try {
                unit = new XOUnit("default", "Default XO unit", new URI("blueprints:///"), TinkerPopXOProvider.class, new HashSet<>(types),
                        instanceListenerTypes, valiationMode, concurrencyMode, transactionAttribute, blueprint);
                xoUnits.add(new Object[] { unit });
            } catch (final URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return xoUnits;
    }

    protected XOManagerFactory getXoManagerFactory() {
        return xoManagerFactory;
    }

    protected XOManager getXoManager() {
        if (xoManager == null) {
            xoManager = getXoManagerFactory().createXOManager();
        }
        return xoManager;
    }

    protected void closeXOmanager() {
        if (xoManager != null) {
            if (xoManager.currentTransaction().isActive()) {
                xoManager.currentTransaction().rollback();
            }
            xoManager.close();
            xoManager = null;
        }
    }

    private void dropDatabase() {
        final XOManager manager = getXoManager();
        manager.currentTransaction().begin();
        //
        manager.currentTransaction().commit();
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
        neo4j.setProperty("blueprints.neo4j.directory", "file:blueprints");
        blueprints.add(neo4j);

        return blueprints;
    }
}
