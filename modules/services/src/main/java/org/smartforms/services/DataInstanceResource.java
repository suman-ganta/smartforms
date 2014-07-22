package org.smartforms.services;

import org.smartforms.services.model.DataInstance;
import org.smartforms.services.model.ViewDef;
import redis.clients.jedis.Jedis;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * REST endpoint for data instance lookup
 */
@Path("instances")
public class DataInstanceResource {

    @Context
    protected SecurityContext securityContext;

    /**
     * Return data instance details of a given instanceId
     * @param diId
     * @return
     */
    @Path("{instanceId}")
    @GET
    public Response getDataInstance(@PathParam("instanceId") Long diId){
        Map<String, String> dataInstance = jedis().hgetAll(PUtil.dataInstanceDetailsKey(diId));
        if(dataInstance != null) {
            DataInstance di = new DataInstance(dataInstance);
            if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), String.valueOf(di.getDsId()))) {
                return Response.ok(di, MediaType.APPLICATION_JSON).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN).entity("Given dataInstance is not accessible").build();
            }
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("No such datainstance exists").build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDataInstance(DataInstance dataInstance) {
        Long seq = jedis().incr(PUtil.diKey());
        dataInstance.setId(seq);
        jedis().hmset(PUtil.dataInstanceDetailsKey(seq), dataInstance.toMap());

        //associate with dataset
        jedis().sadd(PUtil.dataSetInstancesKey(dataInstance.getDsId()), String.valueOf(dataInstance.getId()));
        return Response.ok(dataInstance, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDataInstance(DataInstance dataInstance) {
        jedis().hmset(PUtil.dataInstanceDetailsKey(dataInstance.getId()), dataInstance.toMap());
        return Response.ok(dataInstance, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Searches data instances associated to a view definition
     * @param viewDefId
     * @param query
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findDataInstances(@QueryParam("viewDefId") Long viewDefId, @QueryParam("query") String query){
        List<DataInstance> dataInstances = new ArrayList<>();

        Map<String, String> viewDefDetails = jedis().hgetAll(PUtil.viewDetailsKey(viewDefId));
        if (viewDefDetails != null) {
            ViewDef viewDef = new ViewDef(viewDefDetails);
            if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), String.valueOf(viewDef.getDsId()))) {
                Set<String> diIds = jedis().smembers(PUtil.dataSetInstancesKey(viewDef.getDsId()));
                for (String diId : diIds) {
                    Map<String, String> diDetails = jedis().hgetAll(PUtil.dataInstanceDetailsKey(Long.valueOf(diId)));
                    DataInstance di = new DataInstance(diDetails);
                    if (di.getDataAsJson().contains(query)) {
                        dataInstances.add(di);
                    }
                }
                if (!dataInstances.isEmpty()) {
                    return Response.ok(dataInstances, MediaType.APPLICATION_JSON).build();
                }
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity("No data instances found").build();
    }

    public Jedis jedis(){
        return new Jedis("localhost");
    }

    private String getUserId(){
        return securityContext.getUserPrincipal().getName();
    }
}
