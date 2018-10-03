package org.gosh.hololens.repository.api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.gosh.hololens.repository.azure.CaseStoreDelegate;
import org.gosh.hololens.repository.azure.MeshDelegate;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

;


@Path("/case")
public class CaseResource {

    CaseStoreDelegate delegate = new CaseStoreDelegate();
    MeshDelegate meshdelegate = new MeshDelegate();

    private static Logger LOGGER = Logger.getLogger("InfoLogging");


    //TODO Use Authentication method that allows for storage of user details such as user-name.
    //Perhaps JSON webtokens.

    //TODO Change the method to take in a java object
    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(description = "updates a new Holographic Patient Case file, currently can only change patient Fhir identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Patient Case Created."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid")}
    )

    public Response updateNewHolographicCase(@Parameter(description = "ID of the case to create", required = true) @FormParam("patientId") String patientID,
                                             @Parameter(description = "JSON string that contains the new data", required = true) @FormParam("patientInfo") String object) {
        JSONObject test = new JSONObject(object);
        delegate.updateCase(patientID, test);


        return Response.status(200).entity("Success").build();
    }


    @POST
    @Path("create")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Creates a new Holographic Patient Case file in the Blob Storage. PatientInfo expects the following format{patientId:ID,createdBy:whoCreatedTheCase}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Patient Case Created."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid")}
    )
    //TODO Change the method to take in a java object
    public Response createHolographicCase(@Parameter(description = "ID of the case to create", required = true) @FormDataParam("patientId") String patientID,
                                          @Parameter(description = "JSON string that contains the creator's name and patient's Fhir compliant identifier", required = true) @FormDataParam("patientInfo") String object,
                                          @Parameter(description = "Mesh File") @FormDataParam("model") InputStream uploadedInputStream,
                                          @FormDataParam("model") FormDataContentDisposition fileDetail,
                                          @FormDataParam("tags") List<String> tags) {
        LOGGER.info(tags.toString());
        JSONObject test = new JSONObject(object);
        delegate.uploadCase(patientID, test, uploadedInputStream, fileDetail, tags);
        return Response.status(200)
                .entity(object).build();
    }


    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(description = "Creates a new Holographic Patient Case file in the Blob Storage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Patient Case Created."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid")}
    )
    public Response createHolographicCase(@Parameter(description = "ID of the case to create", required = true) @FormParam("patientId") String patientID,
                                          @Parameter(description = "JSON string that contains the doctor's name and patient's name", required = true) @FormParam("patientInfo") String object,
                                          @Parameter(description = "Optional tags", required = true) @FormParam("tags") List<String> tags) {
        LOGGER.info(object);
        JSONObject test = new JSONObject(object);
        delegate.uploadCase(patientID, test, null, null, tags);
        return Response.status(200)
                .entity(object).build();
    }


    @Path("/")
    @GET
    @Produces("application/json")
    public Response getAllCases(@QueryParam("tags") String tags, @QueryParam("name") String name) {
        JSONArray jsonArray1;

        if (tags == null && name == null) {
            LOGGER.info("All");
            jsonArray1 = delegate.getCases();
        } else {


            jsonArray1 = delegate.getCases(tags, name);
        }


        JSONObject test = new JSONObject();
        test.put("cases", jsonArray1);

        return Response.status(200)
                .entity(test.toString()).build();
    }


    @Path("/{patient_id}")
    @GET
    @Produces("application/xml")
    public Response getHolographicCase(@PathParam("patient_id") String patientID) {

        InputStream file = delegate.getCase(patientID);

        return Response.status(200)
                .entity(file).build();
    }


    @Path("/{patient_id}/mesh")
    @POST

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Adds a new Mesh to a Patient Case.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid")}
    )

    public Response addMesh(@Parameter(description = "ID of the case to modify", required = true) @PathParam("patient_id") String c,
                            @Parameter(description = "Mesh file", required = true) @FormDataParam("model") InputStream uploadedInputStream,
                            @FormDataParam("model") FormDataContentDisposition fileDetail) {

        meshdelegate.addMesh(c, uploadedInputStream, "TestUser", fileDetail);

        return Response.status(200)
                .entity("Success").build();
    }


    @Path("/{patient_id}/mesh/{mesh_id}")
    @DELETE

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Deletes a mesh from the given patient case")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "404", description = "Case not FOund")}
    )

    public Response deleteMesh(@Parameter(description = "ID of the case to modify", required = true) @PathParam("patient_id") String patientID,
                               @Parameter(description = "name of the mesh to delete", required = true) @PathParam("mesh_id") String meshId) {
        LOGGER.info(meshId);
        meshdelegate.deleteMesh(patientID, meshId);

        return Response.status(200)
                .entity("Success").build();
    }


    @Path("/{patient_id}/mesh/{mesh_id}")
    @PUT

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "replaces a mesh from the given patient case with a new one")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "404", description = "Case not FOund")}
    )

    public Response updateMesh(@Parameter(description = "ID of the case to modify", required = true) @PathParam("patient_id") String c,
                               @Parameter(description = "name of the mesh to delete", required = true) @PathParam("mesh_id") String meshId,
                               @FormDataParam("model") InputStream uploadedInputStream,
                               @FormDataParam("model") FormDataContentDisposition fileDetail
    ) {
        LOGGER.info(meshId);
        meshdelegate.updateMesh(c, meshId, "TestUser", uploadedInputStream, fileDetail);

        return Response.status(200)
                .entity("Success").build();
    }


    @Path("/{patient_id}")
    @DELETE
    @Produces("text/plain")

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(description = "Deletes a Patient case file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request.")}
    )

    public Response deleteHolographicCase(@Parameter(description = "ID of the case to delete", required = true) @PathParam("patient_id") String patientID) {

        boolean success;
        try {
            success = delegate.deleteCase(patientID);
        } catch (FileNotFoundException e) {
            return Response.status(404)
                    .entity("Patient case " + patientID + " does not exist").build();
        }

        if (success) {
            return Response.status(200)
                    .entity("Success").build();
        } else {
            return Response.status(500)
                    .entity("Could not delete case").build();
        }
    }


    @Path("model/{patient_id}/{mesh_id}")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getModelFromCase(@PathParam("patient_id") String patientID, @PathParam("mesh_id") String meshID) {

        byte[] mesh = delegate.getMesh(patientID, meshID);
        return Response.status(200)
                .entity(mesh).build();
    }


}