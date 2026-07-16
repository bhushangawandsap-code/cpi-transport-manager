package com.bhushan.cpitransport.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntegrationFlowRequest {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("PackageId")
    private String packageId;

    @JsonProperty("ArtifactContent")
    private String artifactContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getArtifactContent() {
        return artifactContent;
    }

    public void setArtifactContent(String artifactContent) {
        this.artifactContent = artifactContent;
    }
}