package com.smbtec.xo.tinkerpop.blueprints.test.bootstrap.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

/**
 * 
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Dirk Mahler
 * 
 */
@Vertex
public interface A {

	String getName();

	void setName(String name);
}
