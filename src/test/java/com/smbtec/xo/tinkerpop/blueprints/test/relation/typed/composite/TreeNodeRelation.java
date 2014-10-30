package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;

@Edge
public interface TreeNodeRelation {

    int getVersion();

    void setVersion(int version);

    @Incoming
    TreeNode getChild();

    @Outgoing
    TreeNode getParent();
}
