package org.gosh.hololens.repository.api;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gosh.hololens.repository.azure.TagDelegate;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Path("/tag")
public class TagResource {

    TagDelegate delegate = new TagDelegate();

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(description = "Creates a meta tag inside the selected patient case.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New annotation created."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid"),
            @ApiResponse(responseCode = "404", description = "Patient Case not found")}
    )

    public Response addTag(@FormParam("caseId") String id,
                           @FormParam("tagText") String tag) {
        try {
            delegate.addTag(id, tag);
        } catch (NullPointerException e) {
            return Response.status(404)
                    .entity("Patient Case not found").build();

        }
        return Response.status(200)
                .entity("Success").build();

    }


    @PUT
    @Path("update")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Operation(description = "Updates the selected tag inside the selected patient case.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New annotation created."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "400", description = "Your request data is invalid"),
            @ApiResponse(responseCode = "404", description = "Patient Case not found")}
    )

    public Response updateTag(@FormParam("caseId") String id,
                              @FormParam("tagId") String tagId,
                              @FormParam("tagText") String tag) {
        try {
            delegate.updateTag(id, tagId, tag);
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
    @Operation(description = "Deletes a tag inside the selected patient case.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "401", description = "Client not authorised to complete the request."),
            @ApiResponse(responseCode = "404", description = "Annontation not found")}
    )
    public Response deleteTag(@Parameter(description = "ID of the case to modify", required = true) @FormParam("caseId") String id,
                              @Parameter(description = "UUID of the tag to delete", required = true) @FormParam("tagId") String tagID) {

        try {
            delegate.deleteTag(id, tagID);
        } catch (NullPointerException e) {
            return Response.status(404)
                    .entity("Tag not found").build();
        }

        return Response.status(200)
                .entity("Success").build();
    }


}