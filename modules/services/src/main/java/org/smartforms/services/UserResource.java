package org.smartforms.services;

import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Users resource that provides login, logout and user details resources
 */
@Path("users")
public class UserResource {
    @Context
    protected SecurityContext securityContext;

    private Logger logger = Logger.getLogger(UserResource.class.getName());

   /* @GET
    @Path("${userid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails(@PathParam("userid") String userid) {
        System.out.println("invoked");
        return Response.ok(new User(), MediaType.APPLICATION_JSON).build();
    }*/

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest req){
        //String jsessionid = req.getSession().getId();
        String jsessionid = UUID.randomUUID().toString();
        jedis().hset(PUtil.userSessionsKey(), getUserId(), jsessionid);
        return Response.ok(jsessionid, MediaType.APPLICATION_JSON).cookie(new NewCookie("JSESSIONID", jsessionid)).build();
    }

    @GET
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(){
        if(jedis().hdel(PUtil.userSessionsKey(), getUserId()) == 1) {
            return Response.ok("Removed", MediaType.APPLICATION_JSON).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public Jedis jedis(){
        return new Jedis("localhost");
    }

    private String getUserId(){
        return securityContext.getUserPrincipal().getName();
    }

}
