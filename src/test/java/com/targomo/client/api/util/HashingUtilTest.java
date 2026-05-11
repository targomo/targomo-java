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
		assertEquals(-1286992621, HashingUtil.hashFieldNames(AggregationConfiguration.class));
		assertEquals(-752896982, HashingUtil.hashFieldNames(AggregationInputParameters.class));
		assertEquals(-1565773336, HashingUtil.hashFieldNames(TravelOptions.class));
		assertEquals(-645981251, HashingUtil.hashFieldNames(StatisticTravelOptions.class));
		assertEquals(-639453673, HashingUtil.hashFieldNames(RoutingOptions.class));
	}
}