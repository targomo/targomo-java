package com.targomo.client.api.util;

import com.targomo.client.api.StatisticTravelOptions;
import com.targomo.client.api.TravelOptions;
import com.targomo.client.api.pojo.AggregationConfiguration;
import com.targomo.client.api.pojo.AggregationInputParameters;
import com.targomo.client.api.pojo.RoutingOptions;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class HashingUtilTest {
	@Test
	public void testTravelOptionsNameHashing() {
		// !INFO: These tests are designed to fail after updates to the configured classes. When these configurations
		// change please ensure relevant hashing, equals and comparator methods are appropriately updated.
		assertEquals(-367383571, HashingUtil.hashFieldNames(AggregationConfiguration.class));
		assertEquals(651477336, HashingUtil.hashFieldNames(AggregationInputParameters.class));
		assertEquals(924843144, HashingUtil.hashFieldNames(TravelOptions.class));
		assertEquals(-675091905, HashingUtil.hashFieldNames(StatisticTravelOptions.class));
		assertEquals(607894625, HashingUtil.hashFieldNames(RoutingOptions.class));
	}
}