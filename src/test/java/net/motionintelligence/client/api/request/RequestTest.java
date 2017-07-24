package net.motionintelligence.client.api.request;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class RequestTest {
	@Mock
	Client mockClient;
	@Mock
	WebTarget mockWebTarget;
	@Mock
	Invocation.Builder mockBuilder;
	@Mock
	Invocation mockInvocation;
	@Mock
	Response sampleResponse;

	@Before
	public void setUp() throws Exception {
		when(mockClient.target(anyString())).thenReturn(mockWebTarget);

		when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
		when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
		when(mockWebTarget.request()).thenReturn(mockBuilder);

		when(mockBuilder.get()).thenReturn(sampleResponse);
		when(mockBuilder.post(anyObject())).thenReturn(sampleResponse);
        when(mockBuilder.buildGet()).thenReturn(mockInvocation);

        when(mockInvocation.invoke()).thenReturn(sampleResponse);
	}
}
