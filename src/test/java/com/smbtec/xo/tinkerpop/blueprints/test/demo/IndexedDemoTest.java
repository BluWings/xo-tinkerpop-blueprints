package com.smbtec.xo.tinkerpop.blueprints.test.demo;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.demo.composite.Group;
import com.smbtec.xo.tinkerpop.blueprints.test.demo.composite.Person;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URISyntaxException;
import java.util.Collection;

@RunWith(Parameterized.class)
public class IndexedDemoTest extends AbstractTinkerPopXOManagerTest {

    public IndexedDemoTest(final XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(Group.class, Person.class);
    }

    @Test
    public void test() {
        final XOManager xoManager = getXoManager();
        final Person person1 = xoManager.create(Person.class);
        person1.setName("Peter");
        xoManager.flush();
        final Person person2 = xoManager.find(Person.class, "Peter").getSingleResult();
        Assert.assertThat(person2, Matchers.equalTo(person1));
    }
}