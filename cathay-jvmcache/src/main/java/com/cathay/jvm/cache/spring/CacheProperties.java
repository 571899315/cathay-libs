package com.cathay.jvm.cache.spring;

import org.springframework.boot.autoconfigure.cache.CacheType;


public class CacheProperties {
    public CacheType type;
    public String spec;

    public void setType(CacheType type) {
        this.type = type;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }


    public CacheType getType() {
        return this.type;
    }

    public String getSpec() {
        return this.spec;
    }
}
