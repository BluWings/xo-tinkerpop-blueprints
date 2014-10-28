package com.smbtec.xo.tinkerpop.blueprints.test.delegate.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;

@Edge("RELATION")
public interface A2B {

    @Outgoing
    A getA();

    @Incoming
    B getB();
}
