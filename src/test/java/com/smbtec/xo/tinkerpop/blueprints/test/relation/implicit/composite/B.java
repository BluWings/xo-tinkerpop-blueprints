package com.smbtec.xo.tinkerpop.blueprints.test.relation.implicit.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface B {

    @Incoming
    @ImplicitOneToOne
    A getOneToOne();

    void setOneToOne(A a);
}
