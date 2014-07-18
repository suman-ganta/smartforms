package org.smartforms.services;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.glassfish.jersey.jackson.JacksonFeature;

import java.io.IOException;
import java.net.URI;

/**
 * Embedded http server.
 *
 */
public class EmbeddedServer {
    // Base URI the Grizzly HTTP server will listen on
    public static final String REST_BASE_URI = "http://localhost:9085/rest/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in org.smartforms package
        //final ResourceConfig rc = new ResourceConfig().packages(Main.class.getPackage().getName());
        final ResourceConfig rc = new ResourceConfig().registerClasses(DataDefinitionResource.class,
                DataInstanceResource.class, BasicAuthFilter.class, JacksonFeature.class);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at REST_BASE_URI
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(REST_BASE_URI), rc);

        //create static file handler to serve html app
        StaticHttpHandler httpHandler = new StaticHttpHandler("../web/app");
        //disable cache to avoid server restarts during dev
        httpHandler.setFileCacheEnabled(false);
        httpServer.getServerConfiguration().addHttpHandler(httpHandler, "/sf");
        return httpServer;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", REST_BASE_URI));
        System.in.read();
        server.stop();
    }
}

