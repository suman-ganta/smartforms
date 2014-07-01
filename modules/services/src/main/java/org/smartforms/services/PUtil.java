package org.smartforms.services;

/**
 * Provides key names for the Redis Hashes and Sets
 */
public class PUtil {
    public static String userDataSetsKey(String userId) {
        return "user." + userId + ".datasets";
    }

    public static String dataSetDetailsKey(String datasetId) {
        return "ds." + datasetId + ".details";
    }

    public static String dataSetViewsKey(String datasetId) {
        return "ds." + datasetId + ".viewDefs";
    }

    public static String dataSetInstancesKey(String datasetId) {
        return "ds." + datasetId + ".instances";
    }

    public static String viewDetailsKey(String viewDefId) {
        return "viewDef." + viewDefId + ".details";
    }

    public static String dataInstanceDetailsKey(String diId){
        return "di." + diId + ".details";
    }
}
