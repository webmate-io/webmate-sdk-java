package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * Global (properly configured) Jackson mapper singleton.
 */
public class JacksonMapper {

    static private ObjectMapper theInstance = null;

    private JacksonMapper() {}

    static public ObjectMapper getInstance() {
        if (JacksonMapper.theInstance == null) {
            JacksonMapper.theInstance = new ObjectMapper();
            JacksonMapper.theInstance.registerModule(new JodaModule());
            JacksonMapper.theInstance.registerModule(new GuavaModule());

        }
        return JacksonMapper.theInstance;
    }
}
