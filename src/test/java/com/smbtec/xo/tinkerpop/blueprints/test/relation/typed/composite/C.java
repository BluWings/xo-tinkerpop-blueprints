package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("C")
public interface C {

    TypeA getTypeA();

    TypeB getTypeB();

}