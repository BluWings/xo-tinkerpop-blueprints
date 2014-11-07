package com.smbtec.xo.tinkerpop.blueprints.test.findbyid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.findbyid.composite.A;
import com.smbtec.xo.tinkerpop.blueprints.test.findbyid.composite.A2B;
import com.smbtec.xo.tinkerpop.blueprints.test.findbyid.composite.B;

@RunWith(Parameterized.class)
public class FindByIdTest extends AbstractTinkerPopXOManagerTest {

    public FindByIdTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, A2B.class, B.class);
    }

    @Test
    public void entity() {
        XOManager xoManager = getXoManager();
        A a1 = xoManager.create(A.class);
        A a2 = xoManager.create(A.class);
        xoManager.flush();
        Object id1 = xoManager.getId(a1);
        Object id2 = xoManager.getId(a2);
        assertThat(xoManager.findById(A.class, id1), is(a1));
        assertThat(xoManager.findById(A.class, id2), is(a2));
    }

    @Test
    public void relation() {
        XOManager xoManager = getXoManager();
        A a1 = xoManager.create(A.class);
        B b1 = xoManager.create(B.class);
        A2B a2b1 = xoManager.create(a1, A2B.class, b1);
        A a2 = xoManager.create(A.class);
        B b2 = xoManager.create(B.class);
        A2B a2b2 = xoManager.create(a2, A2B.class, b2);
        Object id1 = xoManager.getId(a2b1);
        Object id2 = xoManager.getId(a2b2);
        xoManager.flush();
        assertThat(xoManager.findById(A2B.class, id1), is(a2b1));
        assertThat(xoManager.findById(A2B.class, id2), is(a2b2));
    }

}
