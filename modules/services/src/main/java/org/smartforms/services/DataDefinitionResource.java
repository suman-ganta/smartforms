package org.smartforms.services;

import org.smartforms.services.model.DataInstance;
import org.smartforms.services.model.DataSet;
import org.smartforms.services.model.ViewDef;
import redis.clients.jedis.Jedis;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.*;

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
            Map<String, String> dsDetails = jedis().hgetAll(PUtil.dataSetDetailsKey(Long.valueOf(datasetId)));
            dataSets.add(new DataSet(dsDetails));
        }
        if (!dataSets.isEmpty()) {
            return Response.ok(dataSets, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No dataset definitions found").build();
        }
    }

    /**
     * Create new dataset
     * @param dataSet
     * @return
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataDefinition(DataSet dataSet) {
        Long seq = jedis().incr(PUtil.dsKey());
        dataSet.setId(seq);
        jedis().hmset(PUtil.dataSetDetailsKey(seq), dataSet.toMap());
        //associate the new one to the user
        jedis().sadd(PUtil.userDataSetsKey(securityContext.getUserPrincipal().getName()), String.valueOf(seq));
        //Add public datasets to public set
        if (dataSet.is_public()) {
            jedis().sadd(PUtil.userDataSetsKey(PUtil.PUBUSER), String.valueOf(seq));
        }
        return Response.ok(dataSet, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Lookup data sets for the user
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("view/{viewId}")
    public Response getDataDefintionByViewId(@PathParam("viewId") String viewId) {
        Set<String> datasetIds = jedis().smembers(PUtil.userDataSetsKey(getUserId()));
        for (String datasetId : datasetIds) {
            Set<String> viewDefIds = jedis().smembers(PUtil.dataSetViewsKey(Long.valueOf(datasetId)));
            if (viewDefIds.contains(viewId)) {
                Map<String, String> dsDetails = jedis().hgetAll(PUtil.dataSetDetailsKey(Long.valueOf(datasetId)));
                return Response.ok(new DataSet(dsDetails), MediaType.APPLICATION_JSON).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("No dataset definitions found for given viewId").build();
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
            Set<String> viewDefIds = jedis().smembers(PUtil.dataSetViewsKey(Long.valueOf(dataSetId)));
            for (String viewDefId : viewDefIds) {
                Map<String, String> viewDefDetails = jedis().hgetAll(PUtil.viewDetailsKey(Long.valueOf(viewDefId)));
                if (viewDefDetails != null && !viewDefDetails.isEmpty()) {
                    viewDefs.add(new ViewDef(viewDefDetails));
                }
            }
            if (!viewDefs.isEmpty()) {
                return Response.ok(viewDefs, MediaType.APPLICATION_JSON).build();
            } else {
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
    public Response getInstances(@PathParam("datasetId") Long dataSetId) {
        List<DataInstance> dataInstances = new ArrayList<>();
        if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), String.valueOf(dataSetId))) {
            Set<String> diIds = jedis().smembers(PUtil.dataSetInstancesKey(dataSetId));
            for (String diId : diIds) {
                Map<String, String> diDetails = jedis().hgetAll(PUtil.dataInstanceDetailsKey(Long.valueOf(diId)));
                if (diDetails != null && !diDetails.isEmpty()) {
                    dataInstances.add(new DataInstance(diDetails));
                }
            }
            if (!dataInstances.isEmpty()) {
                return Response.ok(dataInstances, MediaType.APPLICATION_JSON).build();
            } else {
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
    public Response getViewDef(@PathParam("viewId") Long viewDefId) {
        try {
            Map<String, String> viewDefDetails = jedis().hgetAll(PUtil.viewDetailsKey(viewDefId));
            if (viewDefDetails != null) {
                ViewDef viewDef = new ViewDef(viewDefDetails);
                if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), String.valueOf(viewDef.getDsId()))) {
                    return Response.ok(viewDef, MediaType.APPLICATION_JSON).build();
                } else {
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

    /**
     * Search view definitions with given query string
     * TODO: need to do offline indexing work for this
     * @param query
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("views")
    public Response findViewDefs(@QueryParam("query") String query) {
        List<ViewDef> viewDefs = new ArrayList<>();
        Set<String> datasetIds = jedis().smembers(PUtil.userDataSetsKey(getUserId()));
        Set<String> pubDatasetIds = jedis().smembers(PUtil.userDataSetsKey(PUtil.PUBUSER));
        Set<String> dsIds = new HashSet<>(datasetIds);
        dsIds.addAll(pubDatasetIds);

        for (String dsId : dsIds) {
            Set<String> viewDefIds = jedis().smembers(PUtil.dataSetViewsKey(Long.valueOf(dsId)));
            for (String viewDefId : viewDefIds) {
                Map<String, String> viewDetails = jedis().hgetAll(PUtil.viewDetailsKey(Long.valueOf(viewDefId)));
                ViewDef vd = new ViewDef(viewDetails);
                if(query != null && !query.isEmpty()) {
                    if (vd.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        viewDefs.add(vd);
                    }
                }else{
                    viewDefs.add(vd);
                }
            }
        }

        if (!viewDefs.isEmpty()) {
            return Response.ok(viewDefs, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No view definitions found").build();
        }
    }

    /**
     * Add new view definition. This will be used by form designer in UI
     * @param viewDef
     * @return
     */
    @Path("views")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addViewDef(ViewDef viewDef) {
        Long seq = jedis().incr(PUtil.vdKey());
        viewDef.setId(seq);
        jedis().hmset(PUtil.viewDetailsKey(seq), viewDef.toMap());

        jedis().sadd(PUtil.dataSetViewsKey(viewDef.getDsId()), String.valueOf(seq));
        return Response.ok(viewDef, MediaType.APPLICATION_JSON).build();
    }

    /**
     * TODO use connection pool
     * @return
     */
    public Jedis jedis(){
        return new Jedis("localhost");
    }

    /**
     * Returns user id of logged in user
     * @return
     */
    private String getUserId(){
        return securityContext.getUserPrincipal().getName();
    }
}
