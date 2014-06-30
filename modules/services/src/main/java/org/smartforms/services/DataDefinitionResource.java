package org.smartforms.services;

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataDefintions() {
        List<DataSet> dataSets = new ArrayList<>();
        Set<String> datasetIds = jedis().smembers("user." + getUserId() + ".datasets");
        for (String datasetId : datasetIds) {
            Map<String, String> dsDetails = jedis().hgetAll("ds." + datasetId + ".details");
            dataSets.add(new DataSet(dsDetails));
        }
        if (!dataSets.isEmpty()) {
            return Response.ok(dataSets, MediaType.APPLICATION_JSON).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No dataset definitions found").build();
        }
    }

    @Path("{datasetId}/views")
    public Response getViewDefs(@PathParam("datasetId") String dataSetId) {
        List<ViewDef> viewDefs = new ArrayList<>();
        if (jedis().sismember("user." + getUserId() + ".datasets", dataSetId)) {
            Set<String> viewDefIds = jedis().smembers("ds." + dataSetId + ".viewDefs");
            for (String viewDefId : viewDefIds) {
                Map<String, String> viewDefDetails = jedis().hgetAll("viewDef." + viewDefId + ".details");
                viewDefs.add(new ViewDef(viewDefDetails));
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

    @Path("view/{viewId}")
    public Response getViewDef(@PathParam("viewId") String viewDefId) {
        Map<String, String> viewDefDetails = jedis().hgetAll("viewDef." + viewDefId + ".details");
        if (viewDefDetails != null) {
            ViewDef viewDef = new ViewDef(viewDefDetails);
            if (jedis().sismember("user." + getUserId() + ".datasets", viewDef.getDataSetId())) {
                return Response.ok(viewDef, MediaType.APPLICATION_JSON).build();
            }else {
                return Response.status(Response.Status.FORBIDDEN).entity("Given viewDefId is not accessible").build();
            }
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("No such view Definition exists").build();
        }
    }

    public Jedis jedis(){
        return new Jedis("localhost");
    }

    private String getUserId(){
        return securityContext.getUserPrincipal().getName();
    }
}
