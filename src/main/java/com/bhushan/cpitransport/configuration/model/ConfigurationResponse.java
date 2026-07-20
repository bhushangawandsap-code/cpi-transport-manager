package com.bhushan.cpitransport.configuration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurationResponse {

    @JsonProperty("d")
    private ConfigurationData d;

    public ConfigurationData getD() {
        return d;
    }

    public void setD(ConfigurationData d) {
        this.d = d;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConfigurationData {

        @JsonProperty("results")
        private List<Configuration> results;

        public List<Configuration> getResults() {
            return results;
        }

        public void setResults(List<Configuration> results) {
            this.results = results;
        }

    }

}