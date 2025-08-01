package com.hub4.domain.model;

import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
public interface Savable {
    void save(OutputStream out) throws IOException;
}
