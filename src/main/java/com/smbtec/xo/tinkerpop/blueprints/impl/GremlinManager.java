package com.smbtec.xo.tinkerpop.blueprints.impl;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreSession;
import com.buschmais.xo.spi.reflection.AnnotatedElement;
import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Gremlin;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class GremlinManager {

    /**
     * This is a helper method to extract the Gremlin expression.
     *
     * @param expression
     *            is the object which comes in from
     *            {@link DatastoreSession#executeQuery(Object, java.util.Map)}.
     * @return A {@link String} containing a Gremlin expression is returned.
     */
    public static <QL> GremlinExpression getGremlinExpression(final QL expression, final Map<String, Object> parameters) {
        GremlinExpression gremlinExpression = null;
        if (expression instanceof String) {
            gremlinExpression = new GremlinExpression("", (String) expression);
        } else if (AnnotatedElement.class.isAssignableFrom(expression.getClass())) {
            final AnnotatedElement<?> typeExpression = (AnnotatedElement<?>) expression;
            gremlinExpression = extractExpression(typeExpression);
        } else if (Class.class.isAssignableFrom(expression.getClass())) {
            final Class<?> clazz = (Class<?>) expression;
            gremlinExpression = extractExpression(clazz);
        } else if (Method.class.isAssignableFrom(expression.getClass())) {
            final Method method = (Method) expression;
            gremlinExpression = extractExpression(method);
        } else {
            throw new XOException("Unsupported query expression " + expression.toString() + "(class=" + expression.getClass() + ")");
        }
        return applyParameters(parameters, gremlinExpression);
    }

    private static GremlinExpression extractExpression(final AnnotatedElement<?> typeExpression) {
        final Gremlin gremlin = typeExpression.getAnnotation(Gremlin.class);
        if (gremlin == null) {
            throw new XOException(typeExpression + " must be annotated with " + Gremlin.class.getName());
        }
        return new GremlinExpression(gremlin);
    }

    private static <QL> GremlinExpression extractExpression(final Class<?> clazz) {
        final Gremlin gremlin = clazz.getAnnotation(Gremlin.class);
        if (gremlin == null) {
            throw new XOException(clazz.getName() + " must be annotated with " + Gremlin.class.getName());
        }
        return new GremlinExpression(gremlin);
    }

    private static <QL> GremlinExpression extractExpression(final Method method) {
        final Gremlin gremlin = method.getAnnotation(Gremlin.class);
        if (gremlin == null) {
            throw new XOException(method.getName() + " must be annotated with " + Gremlin.class.getName());
        }
        return new GremlinExpression(gremlin);
    }

    private static GremlinExpression applyParameters(final Map<String, Object> parameters, final GremlinExpression gremlinExpression) {
        final StringBuffer typeDefinitions = createTypeDefinitions(parameters);
        String expressionString = gremlinExpression.getExpression();
        for (final String type : parameters.keySet()) {
            final String placeholder = "\\{" + type + "\\}";
            if (!"this".equals(type)) {
                expressionString = expressionString.replaceAll(placeholder, type);
            }
        }
        final String enhancedExpressionString = typeDefinitions.toString() + expressionString;
        return new GremlinExpression(gremlinExpression.getResultName(), enhancedExpressionString);
    }

    private static StringBuffer createTypeDefinitions(final Map<String, Object> parameters) {
        final StringBuffer typeDefinitions = new StringBuffer();
        for (final Entry<String, Object> entry : parameters.entrySet()) {
            final String type = entry.getKey();
            if (!"this".equals(type)) {
                final Object value = entry.getValue();
                if (String.class.equals(value.getClass())) {
                    typeDefinitions.append(type);
                    typeDefinitions.append("=");
                    typeDefinitions.append("'");
                    typeDefinitions.append(value);
                    typeDefinitions.append("'");
                    typeDefinitions.append("\n");
                } else if (Edge.class.isAssignableFrom(value.getClass())) {
                    continue;
                } else if (Vertex.class.isAssignableFrom(value.getClass())) {
                    continue;
                } else {
                    typeDefinitions.append(type);
                    typeDefinitions.append("=");
                    typeDefinitions.append(value);
                    typeDefinitions.append("\n");
                }
            }
        }
        return typeDefinitions;
    }

}
