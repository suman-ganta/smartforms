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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        List<Long> dsIds = new ArrayList<>();

        Long seq = jedis.incr(PUtil.dsKey());
        dsIds.add(seq);
        jedis.hmset(PUtil.dataSetDetailsKey(seq), new DataSet().setId(seq).setName("test" + seq).setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());

        seq = jedis.incr(PUtil.dsKey());
        dsIds.add(seq);
        jedis.hmset(PUtil.dataSetDetailsKey(seq), new DataSet().setId(seq).setName("test" + seq).setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());

        seq = jedis.incr(PUtil.dsKey());
        dsIds.add(seq);
        jedis.hmset(PUtil.dataSetDetailsKey(seq), new DataSet().setId(seq).setName("test" + seq).setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());

        seq = jedis.incr(PUtil.dsKey());
        dsIds.add(seq);
        jedis.hmset(PUtil.dataSetDetailsKey(seq), new DataSet().setId(seq).setName("test" + seq).setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());

        seq = jedis.incr(PUtil.dsKey());
        dsIds.add(seq);
        jedis.hmset(PUtil.dataSetDetailsKey(seq), new DataSet().setId(seq).setName("test" + seq).setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());

        seq = jedis.incr(PUtil.dsKey());
        dsIds.add(seq);
        jedis.hmset(PUtil.dataSetDetailsKey(seq), new DataSet().setId(seq).setName("test" + seq).setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}").toMap());

        //associate datasets to users
        jedis.sadd(PUtil.userDataSetsKey(VALID_USER), String.valueOf(dsIds.get(0)), String.valueOf(dsIds.get(1)), String.valueOf(dsIds.get(2)));

        //populate view definitions
        List<String> vdIds = new ArrayList<>();

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(0))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : true, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(0))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(0))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(1))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(1))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(1))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(2))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(2))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        seq = jedis.incr(PUtil.vdKey());
        vdIds.add(seq.toString());
        jedis.hmset(PUtil.viewDetailsKey(seq), new ViewDef().setDsId(Long.valueOf(dsIds.get(2))).setId(seq).setTitle("MyView" + seq).setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}").toMap());

        //associate views to datasets
        jedis.sadd(PUtil.dataSetViewsKey(dsIds.get(0)), vdIds.get(0), vdIds.get(1), vdIds.get(2));
        jedis.sadd(PUtil.dataSetViewsKey(dsIds.get(1)), vdIds.get(3), vdIds.get(4), vdIds.get(5));
        jedis.sadd(PUtil.dataSetViewsKey(dsIds.get(2)), vdIds.get(6), vdIds.get(7), vdIds.get(8));

        //populate data instances
        List<String> diIds = new ArrayList<>();

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 485}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 480}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 485}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 480}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 485}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 480}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 485}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Shanti\", \"lastName\" : \"Sree\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 480}").toMap());

        seq = jedis.incr(PUtil.diKey());
        diIds.add(seq.toString());
        jedis.hmset(PUtil.dataInstanceDetailsKey(seq), new DataInstance().setId(seq).setDsId(Long.valueOf(dsIds.get(0))).
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 485}").toMap());

        //associate data instances with dataSets
        jedis.sadd(PUtil.dataSetInstancesKey(dsIds.get(0)), diIds.get(0), diIds.get(1), diIds.get(2));
        jedis.sadd(PUtil.dataSetInstancesKey(dsIds.get(1)), diIds.get(3), diIds.get(4), diIds.get(5));
        jedis.sadd(PUtil.dataSetInstancesKey(dsIds.get(2)), diIds.get(6), diIds.get(7), diIds.get(8));
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
        assertTrue(datasets.length >= 3);
    }

    @Test
    public void viewDefLookup() {
        DataSet[] datasets = target.path(DATADEFS).request().get(DataSet[].class);
        assertNotNull(datasets);
        ViewDef[] viewDefs = target.path(DATADEFS + "/" + datasets[0].getId() + "/views").request().get(ViewDef[].class);
        assertTrue(viewDefs.length >= 3);
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

    @Test
    public void testDataSetCreation() {
        DataSet[] datasets1 = target.path(DATADEFS).request().get(DataSet[].class);

        DataSet ds = new DataSet().setName("testMe").setDescription("test desc").
                setDataFieldsJson("{\"firstName\" : \"text\", \"lastName\" : \"text\", \"address\":\"text\", \"totalMarks\" : \"number\"}");
        DataSet dataset = target.path(DATADEFS).request().post(Entity.entity(ds, MediaType.APPLICATION_JSON), DataSet.class);
        assertNotNull(dataset.getId());

        DataSet[] datasets2 = target.path(DATADEFS).request().get(DataSet[].class);

        assertEquals(datasets1.length + 1, datasets2.length);
    }

    @Test
    public void testViewDefCreation() {
        DataSet[] datasets = target.path(DATADEFS).request().get(DataSet[].class);
        assertNotNull(datasets);
        ViewDef[] viewDefs = target.path(DATADEFS + "/" + datasets[0].getId() + "/views").request().get(ViewDef[].class);

        ViewDef viewDef = new ViewDef().setDsId(datasets[0].getId()).setTitle("MyViewTest").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : true, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}");
        ViewDef viewDef1 = target.path(DATADEFS + "/views").request().post(Entity.entity(viewDef, MediaType.APPLICATION_JSON), ViewDef.class);
        assertNotNull(viewDef1.getId());

        ViewDef[] viewDefs2 = target.path(DATADEFS + "/" + datasets[0].getId() + "/views").request().get(ViewDef[].class);
        assertEquals(viewDefs.length + 1, viewDefs2.length);
    }

    @Test
    public void testDataInstanceCreation() {
        DataSet[] datasets = target.path(DATADEFS).request().get(DataSet[].class);
        assertNotNull(datasets);
        DataInstance[] dataInstances = target.path(DATADEFS + "/" + datasets[0].getId() + "/instances").request().get(DataInstance[].class);

        DataInstance dataInstance = new DataInstance().setDsId(datasets[0].getId()).
                setDataAsJson("{\"firstName\" : \"Suman\", \"lastName\" : \"Ganta\", \"address\":\"1200 E Hillsdale\", \"totalMarks\" : 485}");

        DataInstance dataInstance1 = target.path("instances").request().post(Entity.entity(dataInstance, MediaType.APPLICATION_JSON), DataInstance.class);
        assertNotNull(dataInstance1.getId());
        DataInstance[] dataInstances2 = target.path(DATADEFS + "/" + datasets[0].getId() + "/instances").request().get(DataInstance[].class);
        assertEquals(dataInstances.length + 1, dataInstances2.length);
    }
}
