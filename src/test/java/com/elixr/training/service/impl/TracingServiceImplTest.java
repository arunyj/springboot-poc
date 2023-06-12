package com.elixr.training.service.impl;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import com.elixr.training.service.TracingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
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