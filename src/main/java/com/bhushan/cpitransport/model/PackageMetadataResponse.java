package com.bhushan.cpitransport.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)

public class PackageMetadataResponse {

    @JsonProperty("d")
    private PackageMetadata metadata;

    public PackageMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(PackageMetadata metadata) {
        this.metadata = metadata;
    }

}