/*
 * Copyright (c) 2008-2019, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.sql.impl.calcite.rules;

import com.hazelcast.sql.impl.calcite.rels.HazelcastRel;
import com.hazelcast.sql.impl.calcite.rels.HazelcastSortRel;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptRuleCall;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.RelFactories;
import org.apache.calcite.rel.core.Sort;
import org.apache.calcite.rel.logical.LogicalSort;

public class HazelcastSortRule extends RelOptRule {
    public static final RelOptRule INSTANCE = new HazelcastSortRule();

    private HazelcastSortRule() {
        super(
            // TODO: Why Convention.NONE is used in Drill?
            RelOptRule.operand(LogicalSort.class, RelOptRule.any()),
            RelFactories.LOGICAL_BUILDER,
            "HazelcastSortRule"
        );
    }

    @Override
    public boolean matches(RelOptRuleCall call) {
        // TODO: Sort with LIMIT/OFFSET will require different treatment>
        return super.matches(call);
    }

    @Override
    public void onMatch(RelOptRuleCall call) {
        final Sort sort = call.rel(0);

        final RelNode input = sort.getInput();
        final RelTraitSet traits = sort.getTraitSet().plus(HazelcastRel.LOGICAL);

        // TODO: Why this call is needed?
        RelNode convertedInput = convert(input, input.getTraitSet().plus(HazelcastRel.LOGICAL).simplify());

        call.transformTo(new HazelcastSortRel(
            sort.getCluster(),
            sort.getTraitSet().plus(HazelcastRel.LOGICAL),
            convertedInput,
            sort.getCollation(),
            sort.offset,
            sort.fetch
        ));
    }
}
