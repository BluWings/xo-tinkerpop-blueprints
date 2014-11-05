package com.smbtec.xo.tinkerpop.blueprints.test.repository;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopRepository;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.repository.composite.A;
import com.smbtec.xo.tinkerpop.blueprints.test.repository.composite.B;
import com.smbtec.xo.tinkerpop.blueprints.test.repository.composite.DatastoreSpecificRepository;
import com.smbtec.xo.tinkerpop.blueprints.test.repository.composite.GenericRepository;

@RunWith(Parameterized.class)
public class RepositoryTest extends AbstractTinkerPopXOManagerTest {

    public RepositoryTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class, GenericRepository.class, DatastoreSpecificRepository.class);
    }

    @Test
    public void genericRepository() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        a.setName("A1");
        xoManager.flush();
        GenericRepository repository = xoManager.getRepository(GenericRepository.class);
        assertThat(repository.findByName("A1"), equalTo(a));
        assertThat(repository.find("A1"), equalTo(a));
    }

    @Test
    public void datastoreSpecificRepository() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        a.setName("A1");
        xoManager.flush();
        DatastoreSpecificRepository repository = xoManager.getRepository(DatastoreSpecificRepository.class);
        assertThat(repository.findAll(A.class).getSingleResult(), equalTo(a));
        assertThat(repository.findByName("A1"), equalTo(a));
        assertThat(repository.find("A1"), equalTo(a));
        assertThat(repository.count(A.class), equalTo(1L));
    }

    @Test
    public void allVertices() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        B b = xoManager.create(B.class);
        xoManager.flush();
        TinkerPopRepository repository = xoManager.getRepository(TinkerPopRepository.class);
        assertThat(repository.vertices(), containsInAnyOrder(a, b));
    }
}
