package com.smbtec.xo.tinkerpop.blueprints.test.relation.typed.composite;

import java.util.List;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Incoming;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Edge.Outgoing;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface TreeNode {

    void setName(String name);

    String getName();

    @Incoming
    TreeNodeRelation getParent();

    @Outgoing
    List<TreeNodeRelation> getChildren();

}