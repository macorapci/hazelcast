package com.hazelcast.sql.impl.expression.call.func;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.sql.HazelcastSqlException;
import com.hazelcast.sql.impl.QueryContext;
import com.hazelcast.sql.impl.expression.Expression;
import com.hazelcast.sql.impl.expression.call.CallOperator;
import com.hazelcast.sql.impl.expression.call.UniCallExpression;
import com.hazelcast.sql.impl.row.Row;
import com.hazelcast.sql.impl.type.DataType;

import java.io.IOException;

/**
 * A function which accepts a string, and return an integer.
 */
public class StringRetIntFunction extends UniCallExpression<Integer> {
    /** Operator. */
    private int operator;

    /** Accessor. */
    private transient DataType operandType;

    public StringRetIntFunction() {
        // No-op.
    }

    public StringRetIntFunction(Expression operand, int operator) {
        super(operand);

        this.operator = operator;
    }

    @Override
    public Integer eval(QueryContext ctx, Row row) {
        Object operandValue = operand.eval(ctx, row);

        if (operandValue == null)
            return null;

        if (operandType == null)
            operandType = operand.getType();

        String operandValueString = operandType.getConverter().asVarchar(operandValue);

        switch (operator) {
            case CallOperator.CHAR_LENGTH:
                return operandValueString.length();

            case CallOperator.ASCII:
                return operandValueString.isEmpty() ? 0 : operandValueString.codePointAt(0);

            default:
                throw new HazelcastSqlException(-1, "Unsupported operator: " + operator);
        }
    }

    @Override
    public DataType getType() {
        return DataType.INT;
    }

    @Override
    public int operator() {
        return operator;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        super.writeData(out);

        out.writeInt(operator);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        super.readData(in);

        operator = in.readInt();
    }
}
