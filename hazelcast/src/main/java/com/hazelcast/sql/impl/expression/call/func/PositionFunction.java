package com.hazelcast.sql.impl.expression.call.func;

import com.hazelcast.sql.HazelcastSqlException;
import com.hazelcast.sql.impl.QueryContext;
import com.hazelcast.sql.impl.expression.Expression;
import com.hazelcast.sql.impl.expression.call.CallOperator;
import com.hazelcast.sql.impl.expression.call.TriCallExpression;
import com.hazelcast.sql.impl.row.Row;
import com.hazelcast.sql.impl.type.DataType;

/**
 * POSITION(seek IN string FROM integer)}.
 */
public class PositionFunction extends TriCallExpression<Integer> {
    /** Seek type. */
    private transient DataType seekType;

    /** Source type. */
    private transient DataType sourceType;

    /** Position type. */
    private transient DataType posType;

    public PositionFunction() {
        // No-op.
    }

    public PositionFunction(Expression operand1, Expression operand2, Expression operand3) {
        super(operand1, operand2, operand3);
    }

    @Override
    public Integer eval(QueryContext ctx, Row row) {
        String seek;
        String source;
        int pos;

        // Get seek operand.
        Object seekValue = operand1.eval(ctx, row);

        if (seekValue == null)
            return null;

        if (seekType == null)
            seekType = operand1.getType();

        seek = seekType.getConverter().asVarchar(seekValue);

        // Get source operand.
        Object op2 = operand2.eval(ctx, row);

        if (op2 == null)
            return null;

        if (sourceType == null)
            sourceType = operand2.getType();

        source = sourceType.getConverter().asVarchar(op2);

        // Get "FROM"
        if (operand3 == null)
            pos = 0;
        else {
            Object op3 = operand3.eval(ctx, row);

            if (op3 == null)
                pos = 0;
            else {
                if (posType == null) {
                    DataType type = operand3.getType();

                    if (!type.isCanConvertToNumeric() || type.getScale() != 0)
                        throw new HazelcastSqlException(-1, "Unsupported data type: " + type);

                    posType = type;
                }

                pos = posType.getConverter().asInt(op3);
            }
        }

        // Process.
        if (pos == 0)
            return source.indexOf(seek) + 1;
        else {
            int pos0 = pos - 1;

            if (pos0 < 0 || pos0 > source.length())
                return 0;

            return source.indexOf(seek, pos0) + 1;
        }
    }

    @Override
    public DataType getType() {
        return DataType.VARCHAR;
    }

    @Override
    public int operator() {
        return CallOperator.POSITION;
    }
}
