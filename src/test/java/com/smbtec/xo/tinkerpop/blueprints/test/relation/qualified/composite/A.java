package com.smbtec.xo.tinkerpop.blueprints.test.relation.qualified.composite;

import java.util.List;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface A {

    @Outgoing
    @QualifiedOneToOne
    B getOneToOne();

    void setOneToOne(B b);

    @Outgoing
    @QualifiedOneToMany
    List<B> getOneToMany();

    @Outgoing
    @QualifiedManyToMany
    List<B> getManyToMany();

}
