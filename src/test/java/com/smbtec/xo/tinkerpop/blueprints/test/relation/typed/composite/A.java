package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import java.util.List;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("A")
public interface A {

    TypedOneToOneRelation getOneToOne();

    List<TypedOneToManyRelation> getOneToMany();

    List<TypedManyToManyRelation> getManyToMany();

}