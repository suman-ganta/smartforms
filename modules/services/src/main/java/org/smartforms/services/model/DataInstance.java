package org.smartforms.services.model;

import java.util.Map;

/**
 * Represents dataInstance model.
 */
public class DataInstance {
    private String datasetId;
    private String dataAsJson;

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public String getDataAsJson() {
        return dataAsJson;
    }

    public void setDataAsJson(String dataAsJson) {
        this.dataAsJson = dataAsJson;
    }

    public DataInstance(Map<String, String> diAsMap) {

    }
}
