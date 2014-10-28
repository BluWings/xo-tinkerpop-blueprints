xo-tinkerpop-blueprints
=======================

[TinkerPop Blueprints](https://github.com/tinkerpop/blueprints/wiki) binding for [eXtendend Objects (XO)](https://github.com/buschmais/extended-objects).

Maven Users
-----------

Our artifacts are published to the Maven Central repository and can be found under the ``com.smb-tec.xo`` groupId.

You'll need to add the following dependency in your builds (and Maven will automatically include the additional transitive dependencies to TinkerPop Blueprints for you). It currently uses the TinkerPop Blueprints API 2.6.0 version:

```xml
    <dependency>
      <groupId>com.smb-tec.xo</groupId>
      <artifactId>xo-tinkerpop-blueprints</artifactId>
      <version>0.0.4-SNAPSHOT</version>  
    </dependency>
```

There are a lot of different Blueprints-enabled graph backends. See the [TinkerPop Blueprints Wiki](https://github.com/tinkerpop/blueprints/wiki) for a (more or less) complete list of implementations. Note that you have to add a dependency to the Blueprints-enabled graph backend of your choice to your applications ``pom.xml``.


|         | xo-tinkerpop-blueprints|extended-objects (XO)|Tinkerpop Blueprints|
|---------|------------------------|---------------------|--------------------|
| Version | 0.0.2                  | 0.4.0               | 2.5.0              |
| Version | 0.0.3                  | 0.4.5               | 2.6.0              |
| Version | 0.0.4-SNAPSHOT         | 0.5.0-SNAPSHOT      | 2.6.0              |

Quick Start
-----------

```java
@Vertex("Person")
public interface Person {
    @Property("name")
    String getName();
    void setName(String name);
}

@Vertex("Student")
public interface Student extends Person {
    @Property("")
    Long getRegNumber();
    void setRegNumber(Long regNumber);
}

// persist an entity
xoManager.currentTransaction().begin();
Student student = xoManager.create(Student.class);
student.setRegNumber(123456L);
student.setName("John Doe");
xoManager.currentTransaction().commit();

// find an entity and modify attributes
xoManager.currentTransaction().begin();
Person johnDoe = xoManager
    .find("John Doe", Person.class)
    .getSingleResult;
johnDoe.setName("John Doe Jr.");
xoManager.currentTransaction().commit();

// use Gremlin query
xoManager.currentTransaction().begin();
Student johnDoe = xoManager
    .createQuery("g.V('name','John Doe Jr.')", Student.class)
    .execute()
    .getSingleResult();
xoManager.currentTransaction().commit();
```

Getting Help
------------

More details on using ``xo-tinkerpop-blueprints`` can be found in the User Guide.

If you are new to eXtended Objects framework please visit the [eXtended Objects project site](https://github.com/buschmais/extended-objects).

License
-------

``xo-tinkerpop-blueprints`` is contributed under Apache License 2.0.

Build
-----

Start the Maven build on command line

    mvn clean package

Continuous Build
----------------

[![Build Status](https://secure.travis-ci.org/SMB-TEC/xo-tinkerpop-blueprints.png)](http://travis-ci.org/SMB-TEC/xo-tinkerpop-blueprints)