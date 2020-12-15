package com.inmaytide.exception.web.domain;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author luomiao
 * @since 2020/11/30
 */
public interface Response {

    default byte[] asBytes() {
        return toString().getBytes(StandardCharsets.UTF_8);
    }

    default DataBuffer asDataBuffer(DataBufferFactory bufferFactory) {
        return bufferFactory.wrap(asBytes());
    }

}
