package com.testfabrik.webmate.javasdk;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
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
            JacksonMapper.theInstance.registerModule(new Jdk8Module().configureAbsentsAsNulls(true));
            JacksonMapper.theInstance.registerModule(new GuavaModule());
            JacksonMapper.theInstance.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        }
        return JacksonMapper.theInstance;
    }
}
