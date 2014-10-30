package com.smbtec.xo.tinkerpop.blueprints.test.relation.qualified.composite;

import java.util.List;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface B {

    @Incoming
    @QualifiedOneToOne
    A getOneToOne();

    void setOneToOne(A a);

    @Incoming
    @QualifiedOneToMany
    A getManyToOne();

    @Incoming
    @QualifiedManyToMany
    List<A> getManyToMany();

}
