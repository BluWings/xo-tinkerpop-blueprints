package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import java.util.List;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("B")
public interface B {

    TypedOneToOneRelation getOneToOne();

    TypedOneToManyRelation getManyToOne();

    List<TypedManyToManyRelation> getManyToMany();

}