package org.smartforms.services;

import sun.misc.BASE64Decoder;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

/**
 * Throws unauthorized responses for all requests with no authorization header
 */
@Provider
public class BasicAuthFilter
        implements ContainerRequestFilter
{
    private static final Response unauthorizedResponse =
            Response.status(Response.Status.UNAUTHORIZED).header(HttpHeaders.WWW_AUTHENTICATE,
                    "Basic realm=\"realm\"").build();

    /**
     * the filter method
     * @param containerRequestContext
     * @throws java.io.IOException
     */
    @Override public void filter(ContainerRequestContext containerRequestContext)
            throws IOException {

        try {
            String user = getUserId(containerRequestContext);

            if (isProtectedEndpoint(containerRequestContext)) {
                if (user == null && containerRequestContext.getSecurityContext().getUserPrincipal() == null) {
                    containerRequestContext.abortWith(unauthorizedResponse);
                }else {
                    setSecurityContext(containerRequestContext, user);
                }
            }else {
                if (user != null) {
                    setSecurityContext(containerRequestContext, user);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean isProtectedEndpoint(ContainerRequestContext context) {
        boolean result = (context.getRequest().getMethod().equals("GET") && context.getUriInfo().getPath().equals("datadefs/views")) ||
                (context.getRequest().getMethod().equals("GET") && context.getUriInfo().getPath().startsWith("datadefs/views/")) ||
                (context.getRequest().getMethod().equals("GET") && context.getUriInfo().getPath().startsWith("datadefs"));
        return !result;
    }

    private String getUserId(ContainerRequestContext context) throws IOException {
        String user = null;
        String authString = context.getHeaderString("authorization");
        if(authString != null){
            authString = authString.replaceFirst("[Bb]asic ", "");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[]        userColonPassBytes = decoder.decodeBuffer(authString);
            String        userColonPass = new String(userColonPassBytes);

            final String userId = userColonPass.substring(0, userColonPass.lastIndexOf(':'));
            user = userId;
        }
        return user;
    }

    private void setSecurityContext(ContainerRequestContext containerRequestContext, final String user) {
        if (containerRequestContext.getSecurityContext().getUserPrincipal() != null) {
            return;
        }

        containerRequestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new Principal() {
                    @Override
                    public String getName() {
                        return user;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        });
    }
}
