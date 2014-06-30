package org.smartforms.services.model;

import java.util.Map;

/**
 * View definition JSON object
 */
public class ViewDef {
    private String dataSetId;
    private String title;
    private String viewDefJson;

    public String getDataSetId() {
        return dataSetId;
    }

    public void setDataSetId(String dataSetId) {
        this.dataSetId = dataSetId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getViewDefJson() {
        return viewDefJson;
    }

    public void setViewDefJson(String viewDefJson) {
        this.viewDefJson = viewDefJson;
    }

    public ViewDef(Map<String, String> vewDefAsMap){

    }
}
