package com.smbtec.xo.tinkerpop.blueprints.test.bootstrap;

import org.junit.Test;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XO;
import com.smbtec.xo.tinkerpop.blueprints.test.bootstrap.composite.A;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Dirk Mahler
 *
 */
public class TinkerPopBootstrapTest {

    @Test
    public void bootstrap() {
        XOManagerFactory xoManagerFactory = XO.createXOManagerFactory("TinkerGraph");
        XOManager xoManager = xoManagerFactory.createXOManager();
        A a = xoManager.create(A.class);
        a.setName("Test");
        xoManager.flush();
        xoManager.close();
        xoManagerFactory.close();
    }

}
