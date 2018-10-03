package org.gosh.hololens.repository.api;


import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.gosh.hololens.repository.azure.AnnotationDelegate;


import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.servers.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@OpenAPIDefinition(
        servers = {
                @Server(
                        description = "development",
                        url = "https://goshmhif.azurewebsites.net/api/")
        }
)
@Path("/annotation")


public class AnnotationResource {

    AnnotationDelegate delegate = new AnnotationDelegate();

    @POST
    @Path("addfile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(description = "Creates an annotation inside the selected patient case.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New annotation created."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid"),
            @ApiResponse(responseCode = "404", description = "Patient Case not found")}
    )

    public Response AddAnnotation(@FormDataParam("caseId") String id,
                                  @FormDataParam("author") String author,
                                  @FormDataParam("text") String comment,
                                  @FormDataParam("file") InputStream uploadedInputStream,
                                  @FormDataParam("file") FormDataContentDisposition fileDetail) {


        try {
            if (uploadedInputStream != null) {
                delegate.addAnnotationFile(id, comment, author, uploadedInputStream, fileDetail);
            } else {
                delegate.addAnnotation(id, comment, author);
            }
            return Response.status(200)
                    .entity("Success").build();
        } catch (NullPointerException e) {
            return Response.status(404)
                    .entity("Patient case not found").build();
        }
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(description = "Creates an annotation inside the selected patient case.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New annotation created."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid"),
            @ApiResponse(responseCode = "404", description = "Patient Case not found")}
    )

    public Response AddAnnotation(@FormParam("caseId") String id,
                                  @FormParam("author") String author,
                                  @FormParam("text") String comment) {


        try {
            delegate.addAnnotation(id, comment, author);
        } catch (NullPointerException e) {
            return Response.status(404)
                    .entity("Patient Case not found").build();

        }
        return Response.status(200)
                .entity("Success").build();

    }


    @DELETE
    @Path("delete")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(description = "Deletes an annotation inside the selected patient case.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "404", description = "Annontation not found")}
    )
    public Response deleteAnnotation(@Parameter(description = "ID of the case to modify", required = true) @FormParam("caseId") String id,
                                     @Parameter(description = "UUID of the annotation to delete", required = true) @FormParam("annotationID") String author) {

        try {
            delegate.deleteAnnotation(id, author);
        } catch (NullPointerException e) {
            return Response.status(404)
                    .entity("Annotation not found").build();
        }

        return Response.status(200)
                .entity("Success").build();
    }


}