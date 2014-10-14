package com.smbtec.xo.tinkerpop.blueprints.test.demo.composite;

import java.util.List;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex("Group")
public interface Group {

    List<Person> getMembers();

    // @ResultOf
    // MemberByName getMemberByName(@Parameter("name") String name);

    // @Cypher("match (g:Group)-[:Members]->(p:Person) where id(g)={this} and p.name={name} return p as member")
    // public interface MemberByName {
    // Person getMember();
    // }

}