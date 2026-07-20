package com.bhushan.cpitransport.configuration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationUpdateRequest {

    @JsonProperty("ParameterValue")
    private String parameterValue;

    @JsonProperty("DataType")
    private String dataType;

    @JsonProperty("Description")
    private String description;

    public ConfigurationUpdateRequest() {
    }

    public ConfigurationUpdateRequest(
            String parameterValue,
            String dataType,
            String description) {

        this.parameterValue = parameterValue;
        this.dataType = dataType;
        this.description = description;

    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}