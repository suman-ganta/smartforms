package org.smartforms.services.model;

import java.util.HashMap;
import java.util.Map;

/**
 * DataSet metadata representation
 */
public class DataSet {
    private String id;
    private String name;
    private String description;

    public String getId() {
        return id;
    }

    public DataSet setId(String id) {
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

    public DataSet(Map dsAsMap) {
        this.id = (String)dsAsMap.get("id");
        this.name = (String)dsAsMap.get("name");
        this.description = (String)dsAsMap.get("description");
    }

    public DataSet(){

    }

    public Map toMap() {
        Map map = new HashMap();
        map.put("id", id);
        map.put("name", name);
        map.put("description", description);
        return map;
    }
}
