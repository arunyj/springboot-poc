package com.elixr.training.service.impl;

import com.elixr.training.service.TracingService;
import brave.Tracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TracingServiceImpl implements TracingService {

    @Autowired
    Tracer tracer;

    @Override
    public String getTraceId() {
        var span = tracer.currentSpan();
        return span.context().traceIdString();
    }
}
