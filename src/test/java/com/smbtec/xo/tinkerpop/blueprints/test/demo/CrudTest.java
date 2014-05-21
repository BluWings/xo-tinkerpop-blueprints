package com.smbtec.xo.tinkerpop.blueprints.test.demo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;

@RunWith(Parameterized.class)
public class CrudTest extends AbstractTinkerPopXOManagerTest {

    public CrudTest(final XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class);
    }

    @Test
    public void create() {
        final XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        A a = xoManager.create(A.class);
        a.setName("Foo");
        xoManager.currentTransaction().commit();
        xoManager.currentTransaction().begin();
        a = xoManager.find(A.class, "Foo").getSingleResult();
        assertThat(a.getName(), equalTo("Foo"));
        a.setName("Bar");
        xoManager.currentTransaction().commit();
        xoManager.currentTransaction().begin();

        A match = xoManager.createQuery("g.v(0)", A.class).execute().getSingleResult();
        assertThat(a, notNullValue());

        xoManager.currentTransaction().commit();
    }

    @Vertex("A")
    public interface A {

        @Indexed
        String getName();

        void setName(String name);

    }

}
