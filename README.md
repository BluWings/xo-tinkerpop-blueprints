xo-tinkerpop-blueprints
=======================

TinkerPop Blueprints binding for eXtendend Objects (XO).

Maven Users
-----------

Our artifacts are published to the Maven Central repository and can be found under the ``com.smb-tec.xo`` groupId.

You'll need to add the following dependency in your builds (and Maven will automatically include the additional transitive dependencies to TinkerPop Blueprints for you):

	<dependency>
	  	<groupId>com.smb-tec.xo</groupId>
  		<artifactId>xo-tinkerpop-blueprints</artifactId>
  		<version>0.0.1</version>
	</dependency>
	
There are a lot of different Blueprints-enabled graph backends. See [https://github.com/tinkerpop/blueprints/wiki](https://github.com/tinkerpop/blueprints/wiki) for a list of implementations.

Getting Started
---------------

Please visit the project wiki for the latest information : [https://github.com/BluWings/xo-tinkerpop-blueprints/wiki](https://github.com/BluWings/xo-tinkerpop-blueprints/wiki)

License
-------

xo-tinkerpop-blueprints is contributed under Apache License 2.0.

Build
-----

Start the Maven build on command line

	mvn clean verify

Continuous Build
----------------

[![Build Status](https://secure.travis-ci.org/BluWings/xo-tinkerpop-blueprints.png)](http://travis-ci.org/BluWings/xo-tinkerpop-blueprints)