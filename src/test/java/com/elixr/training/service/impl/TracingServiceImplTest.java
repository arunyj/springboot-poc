package com.elixr.training.service.impl;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TracingServiceImplTest {

    @Mock
    private Tracer tracer;

    @InjectMocks
    private TracingServiceImpl tracingService;

    @Test
    public void testGetTraceId() {
        Tracer braveTracer = Tracing.newBuilder().build().tracer();
        Span braveSpan = braveTracer.newTrace().name("encode").tag("lc", "codec").start(1L);
        Mockito.when(tracer.currentSpan()).thenReturn(braveSpan);
        assertEquals( braveSpan.context().traceIdString(), tracingService.getTraceId());
    }
}