package com.bhushan.cpitransport.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PackageRequest {


    private String Id;

    private String Name;

    private String ShortText;

    @JsonProperty("Id")
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @JsonProperty("Name")
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setShortText(String shortText) {
        ShortText = shortText;
    }

    @JsonProperty("ShortText")
    public String getShortText() {return ShortText;}

}
