package org.smartforms.services;

import org.smartforms.services.model.DataInstance;
import org.smartforms.services.model.DataSet;
import org.smartforms.services.model.ViewDef;
import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Dataset definitions lookup
 */
@Path("datadefs")
public class DataDefinitionResource {

    @Context
    protected SecurityContext securityContext;

    public DataDefinitionResource() {
        super();
    }

    /**
     * Lookup data sets for the user
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataDefintions() {
        List<DataSet> dataSets = new ArrayList<>();
        Set<String> datasetIds = jedis().smembers(PUtil.userDataSetsKey(getUserId()));
        for (String datasetId : datasetIds) {
            Map<String, String> dsDetails = jedis().hgetAll(PUtil.dataSetDetailsKey(datasetId));
            dataSets.add(new DataSet(dsDetails));
        }
        if (!dataSets.isEmpty()) {
            return Response.ok(dataSets, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No dataset definitions found").build();
        }
    }

    /**
     * Lookup view defintions for a given data set and user
     * @param dataSetId
     * @return
     */
    @GET
    @Path("{datasetId}/views")
    public Response getViewDefs(@PathParam("datasetId") String dataSetId) {
        List<ViewDef> viewDefs = new ArrayList<>();
        if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), dataSetId)) {
            Set<String> viewDefIds = jedis().smembers(PUtil.dataSetViewsKey(dataSetId));
            for (String viewDefId : viewDefIds) {
                Map<String, String> viewDefDetails = jedis().hgetAll(PUtil.viewDetailsKey(viewDefId));
                if(viewDefDetails != null && !viewDefDetails.isEmpty()) {
                    viewDefs.add(new ViewDef(viewDefDetails));
                }
            }
            if (!viewDefs.isEmpty()) {
                return Response.ok(viewDefs, MediaType.APPLICATION_JSON).build();
            }else{
                return Response.status(Response.Status.NOT_FOUND).entity("No view definitions found").build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Given dataSetId is not accessible").build();
        }
    }

    /**
     * Lookup all data instances of a given dataset
     * @param dataSetId
     * @return
     */
    @GET
    @Path("{datasetId}/instances")
    public Response getInstances(@PathParam("datasetId") String dataSetId) {
        List<DataInstance> dataInstances = new ArrayList<>();
        if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), dataSetId)) {
            Set<String> diIds = jedis().smembers(PUtil.dataSetInstancesKey(dataSetId));
            for (String diId : diIds) {
                Map<String, String> diDetails = jedis().hgetAll(PUtil.dataInstanceDetailsKey(diId));
                if(diDetails != null && !diDetails.isEmpty()) {
                    dataInstances.add(new DataInstance(diDetails));
                }
            }
            if (!dataInstances.isEmpty()) {
                return Response.ok(dataInstances, MediaType.APPLICATION_JSON).build();
            }else{
                return Response.status(Response.Status.NOT_FOUND).entity("No data instances found").build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Given dataSetId is not accessible").build();
        }
    }

    /**
     * Return view definition of a given viewId
     * @param viewDefId
     * @return
     */
    @Path("views/{viewId}")
    @GET
    public Response getViewDef(@PathParam("viewId") String viewDefId) {
        try {
            Map<String, String> viewDefDetails = jedis().hgetAll(PUtil.viewDetailsKey(viewDefId));
            if (viewDefDetails != null) {
                ViewDef viewDef = new ViewDef(viewDefDetails);
                if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), viewDef.getDsId())) {
                    return Response.ok(viewDef, MediaType.APPLICATION_JSON).build();
                }else {
                    return Response.status(Response.Status.FORBIDDEN).entity("Given viewDefId is not accessible").build();
                }
            }else{
                return Response.status(Response.Status.NOT_FOUND).entity("No such view Definition exists").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getLocalizedMessage()).build();
        }
    }

    public Jedis jedis(){
        return new Jedis("localhost");
    }

    private String getUserId(){
        return securityContext.getUserPrincipal().getName();
    }
}
