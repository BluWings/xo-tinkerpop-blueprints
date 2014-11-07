package com.smbtec.xo.tinkerpop.blueprints.test.delegate.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("A")
public interface A {

    A2B getA2B();

}
