package com.smbtec.xo.tinkerpop.blueprints.test.repository.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("A")
public interface A {

    @Indexed
    String getName();

    void setName(String name);

}