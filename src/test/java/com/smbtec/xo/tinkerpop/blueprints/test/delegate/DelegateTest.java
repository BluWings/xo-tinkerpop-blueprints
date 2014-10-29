package com.smbtec.xo.tinkerpop.blueprints.test.delegate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.Query.Result;
import com.buschmais.xo.api.Query.Result.CompositeRowObject;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.impl.TinkerPopVertexManager;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.delegate.composite.A;
import com.smbtec.xo.tinkerpop.blueprints.test.delegate.composite.A2B;
import com.smbtec.xo.tinkerpop.blueprints.test.delegate.composite.B;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@RunWith(Parameterized.class)
public class DelegateTest extends AbstractTinkerPopXOManagerTest {

    public DelegateTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class, A2B.class);
    }

    @Test
    public void entity() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        Vertex vertex = ((CompositeObject) xoManager.create(A.class)).getDelegate();
        assertThat(vertex.getProperty(TinkerPopVertexManager.getDiscriminatorPropertyKey("A")), is(notNullValue()));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void relation() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        A a = xoManager.create(A.class);
        B b = xoManager.create(B.class);
        xoManager.create(a, A2B.class, b);
        List<A2B> r = executeQuery("g.V.has('_xo_discriminator_A').outE").getColumn("relationship");
        assertThat(r.size(), equalTo(1));
        Edge edge = ((CompositeObject) r.get(0)).getDelegate();
        assertThat(edge.getLabel(), equalTo("RELATION"));
        xoManager.currentTransaction().commit();
    }

    @Test
    public void row() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        A a = xoManager.create(A.class);
        Result<CompositeRowObject> row = xoManager.createQuery("g.V.has('_xo_discriminator_A')").execute();
        Map<String, Object> delegate = row.getSingleResult().getDelegate();
        assertThat(delegate, IsMapContaining.<String, Object> hasEntry("node", a));
        xoManager.currentTransaction().commit();
    }
}
