package com.smbtec.xo.tinkerpop.blueprints.test.relation.implicit.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface A {

    @Outgoing
    @ImplicitOneToOne
    B getOneToOne();

    void setOneToOne(B b);

}
