package com.smbtec.xo.tinkerpop.blueprints.test.migration.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("A")
public interface A {

    String getValue();

    void setValue(String value);

}
