package com.smbtec.xo.tinkerpop.blueprints.test.repository.composite;

import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.annotation.ImplementedBy;
import com.buschmais.xo.api.annotation.Repository;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.api.proxy.ProxyMethod;
import com.smbtec.xo.tinkerpop.blueprints.api.TinkerPopRepository;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Gremlin;

@Repository
public interface DatastoreSpecificRepository extends TinkerPopRepository {

    @ResultOf
    @Gremlin("g.V.has('name', name)")
    A findByName(@Parameter("name") String name);

    @ImplementedBy(FindMethod.class)
    A find(String name);

    public class FindMethod implements ProxyMethod<XOManager> {

        @Override
        public Object invoke(XOManager xoManager, Object instance, Object[] args) throws Exception {
            Object arg = args[0];
            return xoManager.find(A.class, arg).getSingleResult();
        }
    }
}
