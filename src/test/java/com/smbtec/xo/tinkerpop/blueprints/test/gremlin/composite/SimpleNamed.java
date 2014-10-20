package com.smbtec.xo.tinkerpop.blueprints.test.gremlin.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Gremlin;

@Gremlin("g.v(0).out.map('firstname')")
public interface SimpleNamed {

    String getFirstname();
}
