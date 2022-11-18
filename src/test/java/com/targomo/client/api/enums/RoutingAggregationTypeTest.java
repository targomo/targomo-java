package com.targomo.client.api.enums;

import com.targomo.client.api.pojo.CompetingRoutingOption;
import org.junit.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RoutingAggregationTypeTest {

    @Test
    public void testForEnums() {
        List<RoutingAggregationType> enumList =
                new ArrayList<>(EnumSet.allOf(RoutingAggregationType.class));

        assertThat(enumList.size()).isEqualTo(18);
        assertThat(enumList).containsExactly(RoutingAggregationType.MIN, RoutingAggregationType.MEAN, RoutingAggregationType.SUM);
    }
}
