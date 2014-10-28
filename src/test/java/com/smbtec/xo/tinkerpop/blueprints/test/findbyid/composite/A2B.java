package com.smbtec.xo.tinkerpop.blueprints.test.findbyid.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;

@Edge
public interface A2B {

    @Outgoing
    A getA();

    @Incoming
    B getB();
}
