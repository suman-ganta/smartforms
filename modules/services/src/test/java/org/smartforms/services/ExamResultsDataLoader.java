package org.smartforms.services;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.smartforms.services.model.DataInstance;
import org.smartforms.services.model.DataSet;
import org.smartforms.services.model.ViewDef;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Sample data generator for exam results usecase.
 */
public class ExamResultsDataLoader {

    private static final String VALID_USER = "sganta";
    private WebTarget target;
    private DataSet ds;

    private static final String[][] marks = {
            {"Suman", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Sriram", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Prashant", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Samba", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Sai", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Shanti", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Vinod", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Venkatrao", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Sirisha", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Krishna Kishore", "Siddareddy", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Nanda Kishore", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman2", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman3", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman4", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman5", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman6", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman7", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman8", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman9", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman10", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman11", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman12", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman13", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman14", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman15", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman16", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman17", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman18", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman19", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman20", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman21", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman22", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
            {"Suman23", "Ganta", "images/suman.jpg", "1200 E Hillsdale Blvd, Apt 231, Foster City 94404", "80", "84", "77", "90", "90", "67", "487"},
    };


    public void uploadDataSet(){
        DataSet dataSet = new DataSet().setName("SSC Results - 2014").setDescription("Full exam results of SSC 2014").
                setDataFieldsJson("{\"firstName\" : \"text\", " +
                        "\"lastName\" : \"text\", " +
                        "\"picture\" : \"image\", " +
                        "\"address\":\"text\", " +
                        "\"telugu\" : \"number\", " +
                        "\"english\" : \"number\", " +
                        "\"hindi\" : \"number\", " +
                        "\"mathematics\" : \"number\", " +
                        "\"science\" : \"number\", " +
                        "\"social\" : \"number\", " +
                        "\"totalMarks\" : \"number\"}").setPublic(Boolean.TRUE);
        ds = target.path("datadefs").request().post(Entity.entity(dataSet, MediaType.APPLICATION_JSON), DataSet.class);
    }

    public void uploadDataInstances() {
        for (String[] student : marks) {
            DataInstance dataInstance = new DataInstance().setDsId(ds.getId()).
                    setDataAsJson(String.format("{\"firstName\" : \"%s\", \"lastName\" : \"%s\", \"picture\" : \"%s\", " +
                                    "\"address\":\"%s\", \"telugu\" : %s, \"english\" : %s, \"hindi\" : %s, \"mathematics\" : %s, " +
                                    "\"science\" : %s, \"social\" : %s, \"totalMarks\" : %s}",
                            student[0], student[1], student[2], student[3], student[4], student[5], student[6],
                            student[7], student[8], student[9], student[10]));
            target.path("instances").request().post(Entity.entity(dataInstance, MediaType.APPLICATION_JSON), DataInstance.class);
        }
    }

    public void uploadView() {
        ViewDef viewDef = new ViewDef().setDsId(ds.getId()).setTitle("Marks Sheet").setThumbnail("images/ssc.png").setViewDefJson("{\n" +
                "\"firstName\" : {\"label\" : \"First Name\", \"optionality\" : \"required\", \"mutable\" : true, \"visible\" : true},\n" +
                "\"lastName\" : {\"label\" : \"Last Name\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"picture\" : {\"label\" : \"Photo\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"address\" : {\"label\" : \"Address\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"telugu\" : {\"label\" : \"Telugu\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"english\" : {\"label\" : \"Telugu\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"hindi\" : {\"label\" : \"Hindi\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"mathematics\" : {\"label\" : \"Mathematics\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"science\" : {\"label\" : \"Science\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"social\" : {\"label\" : \"Social Studies\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true},\n" +
                "\"totalMarks\" : {\"label\" : \"Marks\", \"optionality\" : \"required\", \"mutable\" : false, \"visible\" : true}\n" +
                "}");
        ViewDef viewDef1 = target.path("datadefs").path("views").request().post(Entity.entity(viewDef, MediaType.APPLICATION_JSON), ViewDef.class);
    }

    public void upload(){
        this.uploadDataSet();
        this.uploadView();
        this.uploadDataInstances();
    }

    public ExamResultsDataLoader() {
        // create the client
        Client c = ClientBuilder.newClient();

        HttpAuthenticationFeature authFeature = HttpAuthenticationFeature.basic(VALID_USER, "superSecretPassword");
        c.register(authFeature);
        c.register(new JacksonFeature());

        target = c.target(EmbeddedServer.REST_BASE_URI);
    }
}
