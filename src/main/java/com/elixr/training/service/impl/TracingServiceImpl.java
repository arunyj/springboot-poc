package com.elixr.training.service.impl;

import brave.Span;
import com.elixr.training.service.TracingService;
import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TracingServiceImpl implements TracingService {

    @Autowired
    private Tracer tracer;

    @Override
    public String getTraceId() {
        Span span = tracer.currentSpan();
        return span.context().traceIdString();
    }
}
