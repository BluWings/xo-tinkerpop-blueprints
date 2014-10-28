package com.smbtec.xo.tinkerpop.blueprints.test.delegate.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("B")
public interface B {

    A2B getA2B();

}
