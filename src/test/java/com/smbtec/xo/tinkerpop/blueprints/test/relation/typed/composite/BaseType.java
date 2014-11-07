package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;

public interface BaseType {

    @Outgoing
    C getC();

    @Incoming
    D getD();

    int getVersion();

    void setVersion(int version);

}