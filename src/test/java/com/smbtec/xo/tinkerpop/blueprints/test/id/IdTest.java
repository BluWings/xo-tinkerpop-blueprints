package com.smbtec.xo.tinkerpop.blueprints.test.id;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.id.composite.A;
import com.smbtec.xo.tinkerpop.blueprints.test.id.composite.A2B;
import com.smbtec.xo.tinkerpop.blueprints.test.id.composite.B;

@RunWith(Parameterized.class)
public class IdTest extends AbstractTinkerPopXOManagerTest {

    public IdTest(final XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class, A2B.class);
    }

    @Test
    public void id() {
        final XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        final A a = xoManager.create(A.class);
        final B b = xoManager.create(B.class);
        final A2B a2b = xoManager.create(a, A2B.class, b);
        final Object aId = xoManager.getId(a);
        assertThat(aId, notNullValue());
        assertThat(((CompositeObject) a).getId(), equalTo(aId));
        final Object bId = xoManager.getId(b);
        assertThat(bId, notNullValue());
        assertThat(((CompositeObject) b).getId(), equalTo(bId));
        final Object a2bId = xoManager.getId(a2b);
        assertThat(a2bId, notNullValue());
        assertThat(((CompositeObject) a2b).getId(), equalTo(a2bId));
    }
}
