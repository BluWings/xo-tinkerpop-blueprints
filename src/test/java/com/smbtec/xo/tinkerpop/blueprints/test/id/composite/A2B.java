package com.smbtec.xo.tinkerpop.blueprints.test.id.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;


@Edge
public interface A2B {
    @Edge.Outgoing
    A getA();

    @Edge.Incoming
    B getB();
}
