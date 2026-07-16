package com.bhushan.cpitransport.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PackageMetadata {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("ShortText")
    private String shortText;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("SupportedPlatform")
    private String supportedPlatform;

    @JsonProperty("Products")
    private String products;

    @JsonProperty("Keywords")
    private String keywords;

    @JsonProperty("Countries")
    private String countries;

    @JsonProperty("Industries")
    private String industries;

    @JsonProperty("LineOfBusiness")
    private String lineOfBusiness;

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

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSupportedPlatform() {
        return supportedPlatform;
    }

    public void setSupportedPlatform(String supportedPlatform) {
        this.supportedPlatform = supportedPlatform;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
    }

    public String getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(String lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    @Override
    public String toString() {
        return "PackageMetadata{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", shortText='" + shortText + '\'' +
                '}';
    }
}