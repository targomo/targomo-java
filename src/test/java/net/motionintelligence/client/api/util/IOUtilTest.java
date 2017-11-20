package net.motionintelligence.client.api.util;

import net.motionintelligence.client.api.StatisticTravelOptions;
import net.motionintelligence.client.api.enums.EdgeWeightType;
import net.motionintelligence.client.api.enums.TravelType;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

public class IOUtilTest {
	@Test
	public void decode() throws Exception {
		assertEquals(
				"18923789127*&(!@&#*(^!*&@$^%&*%!",
				IOUtil.decode("18923789127*%26%28%21%40%26%23*%28%5E%21*%26%40%24%5E%25%26*%25%21")
		);
	}

	@Test
	public void encode() throws Exception {
		assertEquals(
				"18923789127*%26%28%21%40%26%23*%28%5E%21*%26%40%24%5E%25%26*%25%21",
				IOUtil.encode("18923789127*&(!@&#*(^!*&@$^%&*%!")
		);
	}

	@Test
	public void getResultString() throws Exception {
		Response response = mock(Response.class);
		when(response.readEntity(String.class)).thenReturn("callback({\"aaa\": \"bb\",\n\"cc\":\"bab\")");
		assertEquals("{\"aaa\": \"bb\",\n\"cc\":\"bab\"", IOUtil.getResultString(response));
	}

	@Test
	public void testTravelOptionsCloning() {
		//prepare
		StatisticTravelOptions original = new StatisticTravelOptions();
		original.setTravelType(TravelType.CAR);
		original.setElevationEnabled(true);
		original.setMaxEdgeWeight(7200);
		original.setEdgeWeightType(EdgeWeightType.TIME);
		original.setServiceUrl("TestURL");
		original.setServiceKey("TestKey");

		StatisticTravelOptions clone = IOUtil.cloneObjects( original, StatisticTravelOptions.class );

		assertThat(clone).isEqualToComparingFieldByFieldRecursively(original);
	}
}