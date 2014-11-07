package com.smbtec.xo.tinkerpop.blueprints.test.relation.implicit.composite;

import java.lang.annotation.Retention;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Edge
@Retention(RUNTIME)
public @interface ImplicitOneToOne {
}
