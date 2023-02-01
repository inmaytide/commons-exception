package com.inmaytide.exception.web.domain;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * @author inmaytide
 * @since 2020/11/30
 */
public interface Response extends Serializable {

    default byte[] asBytes() {
        return toString().getBytes(StandardCharsets.UTF_8);
    }

    default DataBuffer asDataBuffer(DataBufferFactory bufferFactory) {
        return bufferFactory.wrap(asBytes());
    }

}
