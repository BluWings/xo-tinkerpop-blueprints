package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;

@Edge("OneToOne")
public interface TypedOneToOneRelation extends TypedRelation {

    @Outgoing
    A getA();

    @Incoming
    B getB();

}
