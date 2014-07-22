package org.smartforms.services;

import org.smartforms.services.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sumagant on 7/22/14.
 */
@Path("users")
public class UserResource {
    @GET
    @Path("${userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails(@PathParam("userid") String userid) {
        System.out.println("invoked");
        return Response.ok(new User(), MediaType.APPLICATION_JSON).build();
    }
}
