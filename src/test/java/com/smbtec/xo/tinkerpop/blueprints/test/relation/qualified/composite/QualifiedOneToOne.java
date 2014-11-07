package com.smbtec.xo.tinkerpop.blueprints.test.relation.qualified.composite;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;

@Edge("OneToOne")
@Retention(RUNTIME)
public @interface QualifiedOneToOne {
}
