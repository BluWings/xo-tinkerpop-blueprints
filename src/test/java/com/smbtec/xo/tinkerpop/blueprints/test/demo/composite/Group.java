package com.smbtec.xo.tinkerpop.blueprints.test.demo.composite;

import com.buschmais.xo.api.annotation.ResultOf;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

import java.util.List;

import static com.buschmais.xo.api.annotation.ResultOf.Parameter;

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