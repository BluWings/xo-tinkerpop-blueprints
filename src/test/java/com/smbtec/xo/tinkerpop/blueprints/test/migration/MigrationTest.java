package com.smbtec.xo.tinkerpop.blueprints.test.migration;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.migration.composite.A;
import com.smbtec.xo.tinkerpop.blueprints.test.migration.composite.B;
import com.smbtec.xo.tinkerpop.blueprints.test.migration.composite.C;
import com.smbtec.xo.tinkerpop.blueprints.test.migration.composite.D;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URISyntaxException;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class MigrationTest extends AbstractTinkerPopXOManagerTest {

    public MigrationTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class, C.class, D.class);
    }

    @Test
    public void downCast() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        a.setValue("Value");
        B b = xoManager.migrate(a, B.class);
        xoManager.flush();
        assertThat(a == b, equalTo(false));
        assertThat(b.getValue(), equalTo("Value"));
    }

    @Test
    public void compositeObject() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        a.setValue("Value");
        B b = xoManager.migrate(a, B.class, D.class).as(B.class);
        xoManager.flush();
        assertThat(b.getValue(), equalTo("Value"));
    }

    @Test
    public void migrationHandler() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        a.setValue("Value");
        XOManager.MigrationStrategy<A, C> migrationStrategy = new XOManager.MigrationStrategy<A, C>() {
            @Override
            public void migrate(A instance, C target) {
                target.setName(instance.getValue());
            }
        };
        C c = xoManager.migrate(a, migrationStrategy, C.class);
        xoManager.flush();
        assertThat(c.getName(), equalTo("Value"));
    }

    @Test
    public void compositeObjectMigrationHandler() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        a.setValue("Value");
        XOManager.MigrationStrategy<A, C> migrationStrategy = new XOManager.MigrationStrategy<A, C>() {
            @Override
            public void migrate(A instance, C target) {
                target.setName(instance.getValue());
            }
        };
        C c = xoManager.migrate(a, migrationStrategy, C.class, D.class).as(C.class);
        xoManager.flush();
        assertThat(c.getName(), equalTo("Value"));
    }
}
