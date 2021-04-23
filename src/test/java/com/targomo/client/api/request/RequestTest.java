package com.targomo.client.api.request;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class RequestTest {

    @Mock
    protected Client mockClient;
    @Mock
    WebTarget mockWebTarget;
    @Mock
    Invocation.Builder mockBuilder;
    @Mock
    Invocation mockInvocation;
    @Mock
    protected Response sampleResponse;

    @Before
    public void setUp() throws Exception {
        when(mockClient.target(anyString())).thenReturn(mockWebTarget);

        when(mockWebTarget.queryParam(anyString(), anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.path(anyString())).thenReturn(mockWebTarget);
        when(mockWebTarget.request()).thenReturn(mockBuilder);

        when(mockBuilder.get()).thenReturn(sampleResponse);
        when(mockBuilder.post(anyObject())).thenReturn(sampleResponse);
        when(mockBuilder.buildGet()).thenReturn(mockInvocation);
        when(mockBuilder.headers(anyObject())).thenReturn(mockBuilder);

        when(mockInvocation.invoke()).thenReturn(sampleResponse);
    }
}
