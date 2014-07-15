package org.smartforms.services;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.smartforms.services.model.DataInstance;
import org.smartforms.services.model.DataSet;
import org.smartforms.services.model.ViewDef;
import redis.clients.jedis.Jedis;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataDefinitionResourceTest {

    public static final String DATADEFS = "datadefs";
    private HttpServer server;
    private WebTarget target;
    private static final String VALID_USER = "sganta";

    @Before
    public void setUp() throws Exception {
        // start the server
        server = EmbeddedServer.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        HttpAuthenticationFeature authFeature = HttpAuthenticationFeature.basic(VALID_USER, "superSecretPassword");
        c.register(authFeature);
        c.register(new JacksonFeature());

        target = c.target(EmbeddedServer.REST_BASE_URI);

        setupData();
    }

    /**
     * Populate test data to Redis instance
     */
    private void setupData() {
        Jedis jedis = new Jedis("localhost");
        //populate DataSets
        jedis.hmset(PUtil.dataSetDetailsKey("ds10"), new DataSet().setId("ds10").setName("test1").setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());
        jedis.hmset(PUtil.dataSetDetailsKey("ds11"), new DataSet().setId("ds11").setName("test2").setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());
        jedis.hmset(PUtil.dataSetDetailsKey("ds12"), new DataSet().setId("ds12").setName("test3").setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());
        jedis.hmset(PUtil.dataSetDetailsKey("ds13"), new DataSet().setId("ds13").setName("test4").setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());
        jedis.hmset(PUtil.dataSetDetailsKey("ds14"), new DataSet().setId("ds14").setName("test5").setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());
        jedis.hmset(PUtil.dataSetDetailsKey("ds15"), new DataSet().setId("ds15").setName("test6").setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());

        //associate datasets to users
        jedis.sadd(PUtil.userDataSetsKey(VALID_USER), "ds10", "ds11", "ds12");

        //populate view definitions
        jedis.hmset(PUtil.viewDetailsKey("vd10"), new ViewDef().setDsId("ds10").setId("vd10").setTitle("MyView10").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd11"), new ViewDef().setDsId("ds10").setId("vd11").setTitle("MyView11").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd12"), new ViewDef().setDsId("ds10").setId("vd12").setTitle("MyView12").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd13"), new ViewDef().setDsId("ds11").setId("vd13").setTitle("MyView13").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd14"), new ViewDef().setDsId("ds11").setId("vd14").setTitle("MyView14").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd15"), new ViewDef().setDsId("ds11").setId("vd15").setTitle("MyView15").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd16"), new ViewDef().setDsId("ds12").setId("vd16").setTitle("MyView16").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd17"), new ViewDef().setDsId("ds12").setId("vd17").setTitle("MyView17").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());
        jedis.hmset(PUtil.viewDetailsKey("vd18"), new ViewDef().setDsId("ds12").setId("vd18").setTitle("MyView18").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        //associate views to datasets
        jedis.sadd(PUtil.dataSetViewsKey("ds10"), "vd10", "vd11", "vd12");
        jedis.sadd(PUtil.dataSetViewsKey("ds11"), "vd13", "vd14", "vd15");
        jedis.sadd(PUtil.dataSetViewsKey("ds12"), "vd16", "vd17", "vd18");

        //populate data instances
        jedis.hmset(PUtil.dataInstanceDetailsKey("di1"), new DataInstance().setId("di1").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"marks\" : 485}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di2"), new DataInstance().setId("di2").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"marks\" : 480}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di3"), new DataInstance().setId("di3").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"marks\" : 485}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di4"), new DataInstance().setId("di4").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"marks\" : 480}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di5"), new DataInstance().setId("di4").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"marks\" : 485}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di6"), new DataInstance().setId("di4").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"marks\" : 480}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di7"), new DataInstance().setId("di4").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"marks\" : 485}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di8"), new DataInstance().setId("di4").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"marks\" : 480}").toMap());
        jedis.hmset(PUtil.dataInstanceDetailsKey("di9"), new DataInstance().setId("di4").setDsId("ds10").
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"marks\" : 485}").toMap());

        //associate data instances with dataSets
        jedis.sadd(PUtil.dataSetInstancesKey("ds10"), "di1", "di2", "di3");
        jedis.sadd(PUtil.dataSetInstancesKey("ds11"), "di4", "di5", "di6");
        jedis.sadd(PUtil.dataSetInstancesKey("ds12"), "di7", "di8", "di9");
    }

    private static Map<String, String> getAsMap(String... objects) {
        //check if count is even
        if((objects.length & objects.length - 1) != 0)
            throw new RuntimeException("parameters count need to be even");
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < objects.length; i += 2) {
            result.put(objects[i], objects[i + 1]);
        }
        return result;
    }

    @After
    public void tearDown() throws Exception {
        server.shutdownNow();
    }

    /**
     * Verify dataset lookup
     */
    @Test
    public void dataSetLookup() {
        DataSet[] datasets = target.path(DATADEFS).request().get(DataSet[].class);
        assertNotNull(datasets);
        assertEquals(3, datasets.length);
    }

    @Test
    public void viewDefLookup() {
        DataSet[] datasets = target.path(DATADEFS).request().get(DataSet[].class);
        assertNotNull(datasets);
        ViewDef[] viewDefs = target.path(DATADEFS + "/" + datasets[0].getId() + "/views").request().get(ViewDef[].class);
        assertEquals(3, viewDefs.length);
        ViewDef viewDef = target.path(DATADEFS + "/views/" + viewDefs[0].getId()).request().get(ViewDef.class);
        assertNotNull(viewDef);
    }

    @Test
    public void testDataInstanceLookup(){
        DataSet[] datasets = target.path(DATADEFS).request().get(DataSet[].class);
        assertNotNull(datasets);
        DataInstance[] dataInstances = target.path(DATADEFS + "/" + datasets[0].getId() + "/instances").request().get(DataInstance[].class);
        DataInstance dataInstance = target.path("instances/" + dataInstances[0].getId()).request().get(DataInstance.class);
        assertNotNull(dataInstance);
    }
}
