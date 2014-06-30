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
        throws IOException
    {
        // Get the authentication passed in HTTP headers parameters
        String          authString = containerRequestContext.getHeaderString("authorization");
        SecurityContext securityContext = containerRequestContext.getSecurityContext();

        if (authString == null && securityContext.getUserPrincipal() == null) {
            containerRequestContext.abortWith(unauthorizedResponse);
        }
        else if (securityContext.getUserPrincipal() == null) {
            authString = authString.replaceFirst("[Bb]asic ", "");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[]        userColonPassBytes = decoder.decodeBuffer(authString);
            String        userColonPass = new String(userColonPassBytes);

            final String userId = userColonPass.substring(0, userColonPass.lastIndexOf(':'));
            containerRequestContext.setSecurityContext(new SecurityContext() {
                    @Override public Principal getUserPrincipal()
                    {
                        return new Principal() {
                            @Override public String getName()
                            {
                                return userId;
                            }
                        };
                    }

                    @Override public boolean isUserInRole(String role)
                    {
                        return false;
                    }

                    @Override public boolean isSecure()
                    {
                        return false;
                    }

                    @Override public String getAuthenticationScheme()
                    {
                        return null;
                    }
                });
        }
    }
}
