package org.smartforms.services;

import org.smartforms.services.model.DataInstance;
import redis.clients.jedis.Jedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Map;

/**
 * REST endpoint for data instance lookup
 */
@Path("/instances")
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
    public Response getDataInstance(@PathParam("instanceId") String diId){
        Map<String, String> dataInstance = jedis().hgetAll(PUtil.dataInstanceDetailsKey(diId));
        if(dataInstance != null) {
            DataInstance di = new DataInstance(dataInstance);
            if (jedis().sismember(PUtil.userDataSetsKey(getUserId()), di.getDsId())) {
                return Response.ok(di, MediaType.APPLICATION_JSON).build();
            }else {
                return Response.status(Response.Status.FORBIDDEN).entity("Given dataInstance is not accessible").build();
            }
        }else{
            return Response.status(Response.Status.NOT_FOUND).entity("No such datainstance exists").build();
        }
    }

    public Jedis jedis(){
        return new Jedis("localhost");
    }

    private String getUserId(){
        return securityContext.getUserPrincipal().getName();
    }
}
