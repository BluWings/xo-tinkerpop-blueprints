package com.smbtec.xo.tinkerpop.blueprints.test.relation.qualified;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.xo.api.Query.Result.CompositeRowObject;
import com.buschmais.xo.api.ResultIterable;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.smbtec.xo.tinkerpop.blueprints.test.AbstractTinkerPopXOManagerTest;
import com.smbtec.xo.tinkerpop.blueprints.test.relation.qualified.composite.A;
import com.smbtec.xo.tinkerpop.blueprints.test.relation.qualified.composite.B;

@RunWith(Parameterized.class)
public class QualifiedRelationTest extends AbstractTinkerPopXOManagerTest {

    public QualifiedRelationTest(XOUnit xoUnit) {
        super(xoUnit);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getXOUnits() throws URISyntaxException {
        return xoUnits(A.class, B.class);
    }

    @Test
    public void oneToOne() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        B b1 = xoManager.create(B.class);
        a.setOneToOne(b1);
        xoManager.flush();
        assertThat(a.getOneToOne(), equalTo(b1));
        assertThat(b1.getOneToOne(), equalTo(a));
        assertThat(
                executeQuery("g.V.has('_xo_discriminator_A').out('OneToOne').has('_xo_discriminator_B')").getColumn(
                        "node"), hasItem(b1));
        B b2 = xoManager.create(B.class);
        a.setOneToOne(b2);
        xoManager.flush();
        assertThat(a.getOneToOne(), equalTo(b2));
        assertThat(b2.getOneToOne(), equalTo(a));
        assertThat(b1.getOneToOne(), equalTo(null));
        assertThat(
                executeQuery("g.V.has('_xo_discriminator_A').out('OneToOne').has('_xo_discriminator_B')").getColumn(
                        "node"), hasItem(b2));
        a.setOneToOne(null);
        xoManager.flush();
        assertThat(a.getOneToOne(), equalTo(null));
        assertThat(b1.getOneToOne(), equalTo(null));
        assertThat(b2.getOneToOne(), equalTo(null));
    }

    @Test
    public void oneToMany() {
        XOManager xoManager = getXoManager();
        A a = xoManager.create(A.class);
        B b1 = xoManager.create(B.class);
        B b2 = xoManager.create(B.class);
        a.getOneToMany().add(b1);
        a.getOneToMany().add(b2);
        xoManager.flush();
        assertThat(a.getOneToMany(), hasItems(b1, b2));
        assertThat(b1.getManyToOne(), equalTo(a));
        assertThat(b2.getManyToOne(), equalTo(a));
        assertThat(executeQuery("g.V.has('_xo_discriminator_A').out('OneToMany').has('_xo_discriminator_B')")
                .<B> getColumn("node"), hasItems(b1, b2));
        a.getOneToMany().remove(b1);
        a.getOneToMany().remove(b2);
        B b3 = xoManager.create(B.class);
        B b4 = xoManager.create(B.class);
        a.getOneToMany().add(b3);
        a.getOneToMany().add(b4);
        xoManager.flush();
        assertThat(a.getOneToMany(), hasItems(b3, b4));
        assertThat(b1.getManyToOne(), equalTo(null));
        assertThat(b2.getManyToOne(), equalTo(null));
        assertThat(b3.getManyToOne(), equalTo(a));
        assertThat(b4.getManyToOne(), equalTo(a));
        assertThat(executeQuery("g.V.has('_xo_discriminator_A').out('OneToMany').has('_xo_discriminator_B')")
                .<B> getColumn("node"), hasItems(b3, b4));
    }

    @Test
    @Ignore
    public void manyToMany() {
        XOManager xoManager = getXoManager();
        xoManager.currentTransaction().begin();
        A a1 = xoManager.create(A.class);
        A a2 = xoManager.create(A.class);
        B b1 = xoManager.create(B.class);
        B b2 = xoManager.create(B.class);
        a1.getManyToMany().add(b1);
        a1.getManyToMany().add(b2);
        a2.getManyToMany().add(b1);
        a2.getManyToMany().add(b2);
        xoManager.currentTransaction().commit();
        xoManager.currentTransaction().begin();
        assertThat(a1.getManyToMany(), hasItems(b1, b2));
        assertThat(a2.getManyToMany(), hasItems(b1, b2));
        assertThat(b1.getManyToMany(), hasItems(a1, a2));
        assertThat(b2.getManyToMany(), hasItems(a1, a2));

        ResultIterable<CompositeRowObject> result = xoManager.createQuery(
                "g.V.has('_xo_discriminator_A').out('ManyToMany').has('_xo_discriminator_B').dedup.table()").execute();
        // assertThat(executeQuery("MATCH (a:A)-[:ManyToMany]->(b:B) RETURN a, collect(b) as listOfB ORDER BY ID(a)")
        // .<A> getColumn("a"), hasItems(a1, a2));
        // assertThat(executeQuery("MATCH (a:A)-[:ManyToMany]->(b:B) RETURN a, collect(b) as listOfB ORDER BY ID(a)")
        // .<Iterable<B>> getColumn("listOfB"), hasItems(hasItems(b1, b2),
        // hasItems(b1, b2)));
        a1.getManyToMany().remove(b1);
        a2.getManyToMany().remove(b1);
        xoManager.currentTransaction().commit();
        xoManager.currentTransaction().begin();
        assertThat(a1.getManyToMany(), hasItems(b2));
        assertThat(a2.getManyToMany(), hasItems(b2));
        assertThat(b1.getManyToMany().isEmpty(), equalTo(true));
        assertThat(b2.getManyToMany(), hasItems(a1, a2));
        xoManager.currentTransaction().commit();
    }
}
