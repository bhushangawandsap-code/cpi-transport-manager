package com.bhushan.cpitransport.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IntegrationFlowMetadataResponse {
    @JsonProperty("d")
    private IntegrationFlowMetadata metadata;

    public IntegrationFlowMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(IntegrationFlowMetadata metadata) {
        this.metadata = metadata;
    }
}