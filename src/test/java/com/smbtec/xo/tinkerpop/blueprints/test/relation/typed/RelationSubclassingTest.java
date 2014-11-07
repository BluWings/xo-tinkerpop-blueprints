package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite.BaseType;
import com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite.C;
import com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite.D;
import com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite.TypeA;
import com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite.TypeB;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URISyntaxException;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class RelationSubclassingTest extends AbstractTinkerPopXOManagerTest {

    public RelationSubclassingTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(C.class, D.class, TypeA.class, TypeB.class);
    }

    @Test
    public void testRelationSubclassing() {
        XOManager xoManager = getXoManager();
        C c = xoManager.create(C.class);
        D d1 = xoManager.create(D.class);
        D d2 = xoManager.create(D.class);
        BaseType relation1 = xoManager.create(c, TypeA.class, d1);
        relation1.setVersion(1);
        BaseType relation2 = xoManager.create(c, TypeB.class, d2);
        relation2.setVersion(2);
        xoManager.flush();
        assertThat(c.getTypeA().getVersion(), equalTo(relation1.getVersion()));
        assertThat(c.getTypeB().getVersion(), equalTo(relation2.getVersion()));
        assertThat(relation1.getC(), equalTo(c));
        assertThat(relation1.getD(), equalTo(d1));
        assertThat(relation2.getC(), equalTo(c));
        assertThat(relation2.getD(), equalTo(d2));
    }

}
