package com.smbtec.xo.tinkerpop.blueprints.test.migration.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("C")
public interface C {

    String getName();

    void setName(String name);

}
