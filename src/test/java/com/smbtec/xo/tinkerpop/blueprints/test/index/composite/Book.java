package com.smbtec.xo.tinkerpop.blueprints.test.index.composite;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Indexed.Parameter;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Vertex;

@Vertex
public interface Book {

    @Indexed(parameters = { @Parameter(key = "type", value = "UNIQUE") })
    String getIsbn();

    void setIsbn(String isbn);

    @Indexed
    String getTitle();

    void setTitle(String title);

}
