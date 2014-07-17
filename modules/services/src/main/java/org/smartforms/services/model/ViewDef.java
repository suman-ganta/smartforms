package org.smartforms.services.model;

import java.util.HashMap;
import java.util.Map;

/**
 * View definition JSON object
 */
public class ViewDef {
    private Long id;
    private Long dsId;
    private String title;
    private String viewDefJson;

    public Long getId() {
        return id;
    }

    public ViewDef setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getDsId() {
        return dsId;
    }

    public ViewDef setDsId(Long dsId) {
        this.dsId = dsId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ViewDef setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getViewDefJson() {
        return viewDefJson;
    }

    public ViewDef setViewDefJson(String viewDefJson) {
        this.viewDefJson = viewDefJson;
        return this;
    }

    public ViewDef(Map<String, String> viewDefAsMap) {
        this.id = Long.valueOf(viewDefAsMap.get("id"));
        this.dsId = Long.valueOf(viewDefAsMap.get("dsId"));
        this.title = viewDefAsMap.get("title");
        this.viewDefJson = viewDefAsMap.get("viewDefJson");
    }

    public ViewDef(){

    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("dsId", dsId.toString());
        map.put("title", title);
        map.put("viewDefJson", viewDefJson);
        return map;
    }
}
