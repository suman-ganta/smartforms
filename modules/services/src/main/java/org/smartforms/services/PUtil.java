package org.smartforms.services;

/**
 * Provides key names for the Redis Hashes and Sets
 */
public class PUtil {
    public static String userDataSetsKey(String userId) {
        return "user." + userId + ".datasets";
    }

    public static String dataSetDetailsKey(Long datasetId) {
        return "ds." + datasetId + ".details";
    }

    public static String dataSetViewsKey(Long datasetId) {
        return "ds." + datasetId + ".viewDefs";
    }

    public static String dataSetInstancesKey(Long datasetId) {
        return "ds." + datasetId + ".instances";
    }

    public static String viewDetailsKey(Long viewDefId) {
        return "viewDef." + viewDefId + ".details";
    }

    public static String dataInstanceDetailsKey(Long diId){
        return "di." + diId + ".details";
    }

    public static String diKey() {
        return "diSeq";
    }

    public static String dsKey() {
        return "dsSeq";
    }

    public static String vdKey() {
        return "vdSeq";
    }
}
