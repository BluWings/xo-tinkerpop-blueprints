package com.smbtec.xo.tinkerpop.blueprints.impl;

import com.smbtec.xo.tinkerpop.blueprints.api.annotation.Gremlin;

/**
 *
 * @author Lars Martin - lars.martin@smb-tec.com
 * @author Rick-Rainer Ludwig
 *
 */
public class GremlinExpression {

    private final String resultName;
    private final String expression;

    public GremlinExpression(final String expression) {
        this("", expression);
    }

    public GremlinExpression(final String resultName, final String expression) {
        super();
        this.resultName = resultName;
        this.expression = expression;
    }

    public GremlinExpression(final Gremlin gremlin) {
        this(gremlin.name(), gremlin.value());
    }

    public String getResultName() {
        return (resultName == null) || (resultName.isEmpty()) ? "unknown" : resultName;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return resultName + ":=" + expression;
    }
}
