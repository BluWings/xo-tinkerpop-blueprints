xo-tinkerpop-blueprints
=======================

[TinkerPop Blueprints](https://github.com/tinkerpop/blueprints/wiki) binding for [eXtendend Objects (XO)](https://github.com/buschmais/extended-objects).

Maven Users
-----------

Our artifacts are published to the Maven Central repository and can be found under the ``com.smb-tec.xo`` groupId.

You'll need to add the following dependency in your builds (and Maven will automatically include the additional transitive dependencies to TinkerPop Blueprints for you). It currently uses the TinkerPop Blueprints API 2.6.0 version:

    <dependency>
        <groupId>com.smb-tec.xo</groupId>
        <artifactId>xo-tinkerpop-blueprints</artifactId>
        <version>0.0.3-SNAPSHOT</version>
    </dependency>


There are a lot of different Blueprints-enabled graph backends. See the [TinkerPop Blueprints Wiki](https://github.com/tinkerpop/blueprints/wiki) for a (more or less) complete list of implementations. Note that you have to add a dependency to the Blueprints-enabled graph backend of your choice to your applications ``pom.xml``.

Getting Started
---------------

Please visit the project wiki for the latest information : [https://github.com/BluWings/xo-tinkerpop-blueprints/wiki](https://github.com/BluWings/xo-tinkerpop-blueprints/wiki)

License
-------

``xo-tinkerpop-blueprints`` is contributed under Apache License 2.0.

Build
-----

Start the Maven build on command line

  mvn clean package

Continuous Build
----------------

[![Build Status](https://secure.travis-ci.org/BluWings/xo-tinkerpop-blueprints.png)](http://travis-ci.org/BluWings/xo-tinkerpop-blueprints)