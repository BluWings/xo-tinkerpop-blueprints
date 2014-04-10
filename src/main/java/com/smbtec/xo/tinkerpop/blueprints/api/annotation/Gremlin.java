package com.smbtec.xo.tinkerpop.blueprints.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.xo.spi.annotation.QueryDefinition;

@QueryDefinition
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Gremlin {

    /**
     * @return Returns the Gremlin expression as {@link String}.
     */
    String value();

    /**
     * @return A name for the result is returned which needs to reflect a
     *         property type.
     */
    String name() default "";
}