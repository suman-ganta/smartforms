package org.smartforms.services.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents dataInstance model.
 */
public class DataInstance {
    private String dsId;
    private String id;
    private String dataAsJson;

    public DataInstance() {

    }

    public String getId() {
        return id;
    }

    public DataInstance setId(String id) {
        this.id = id;
        return this;
    }

    public String getDsId() {
        return dsId;
    }

    public DataInstance setDsId(String dsId) {
        this.dsId = dsId;
        return this;
    }

    public String getDataAsJson() {
        return dataAsJson;
    }

    public DataInstance setDataAsJson(String dataAsJson) {
        this.dataAsJson = dataAsJson;
        return this;
    }

    public DataInstance(Map<String, String> diAsMap) {
        this.id = (String) diAsMap.get("id");
        this.dsId = (String) diAsMap.get("dsId");
        this.dataAsJson = (String) diAsMap.get("dataAsJson");
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("dsId", dsId);
        map.put("dataAsJson", dataAsJson);
        return map;
    }
}
