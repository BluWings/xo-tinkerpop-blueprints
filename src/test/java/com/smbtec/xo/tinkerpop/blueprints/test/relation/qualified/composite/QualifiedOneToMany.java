package com.smbtec.xo.tinkerpop.blueprints.test.relation.qualified.composite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;

@Edge("OneToMany")
@Retention(RetentionPolicy.RUNTIME)
public @interface QualifiedOneToMany {
}
