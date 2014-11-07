package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("D")
public interface D {

    TypeA getTypeA();

    TypeB getTypeB();

}
