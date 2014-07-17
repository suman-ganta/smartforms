package org.smartforms.services.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents dataInstance model.
 */
public class DataInstance {
    private Long dsId;
    private Long id;
    private String dataAsJson;

    public DataInstance() {

    }

    public Long getId() {
        return id;
    }

    public DataInstance setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getDsId() {
        return dsId;
    }

    public DataInstance setDsId(Long dsId) {
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
        this.id = Long.valueOf(diAsMap.get("id"));
        this.dsId = Long.valueOf(diAsMap.get("dsId"));
        this.dataAsJson = (String) diAsMap.get("dataAsJson");
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        map.put("dsId", dsId.toString());
        map.put("dataAsJson", dataAsJson);
        return map;
    }
}
