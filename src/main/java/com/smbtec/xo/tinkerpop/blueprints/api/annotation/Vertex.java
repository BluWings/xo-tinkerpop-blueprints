package com.smbtec.xo.tinkerpop.blueprints.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.xo.spi.annotation.EntityDefinition;

/**
 * This annotation marks a vertex as an entity.
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
@EntityDefinition
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Vertex {

    String DEFAULT_VALUE = "";

    /**
     * @return Returns the name of the type as {@link String}.
     */
    String value() default "";

    /**
     * @return The (super) type containing an indexed property ({@link Indexed}
     *         ).
     *         <p>
     *         An index will be created for this label and the indexed property
     *         and used by {@link XOManager#find(Class, Object)}.
     *         </p>
     */
    Class<?> usingIndexedPropertyOf() default Object.class;

}
