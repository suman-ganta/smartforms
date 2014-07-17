package org.smartforms.services.model;

import java.util.HashMap;
import java.util.Map;

/**
 * DataSet metadata representation
 */
public class DataSet {
    private Long id;
    private String name;
    private String description;
    private String dataFieldsJson;

    public String getDataFieldsJson() {
        return dataFieldsJson;
    }

    public DataSet setDataFieldsJson(String dataFieldsJson) {
        this.dataFieldsJson = dataFieldsJson;
        return this;
    }

    public Long getId() {
        return id;
    }

    public DataSet setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public DataSet setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DataSet setDescription(String description) {
        this.description = description;
        return this;
    }

    public DataSet(Map<String, String> dsAsMap) {
        this.id = Long.valueOf(dsAsMap.get("id"));
        this.name = dsAsMap.get("name");
        this.description = dsAsMap.get("description");
        this.dataFieldsJson = dsAsMap.get("dataFieldsJson");
    }

    public DataSet(){

    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap();
        map.put("id", id.toString());
        map.put("name", name);
        map.put("description", description);
        map.put("dataFieldsJson", dataFieldsJson);
        return map;
    }
}
