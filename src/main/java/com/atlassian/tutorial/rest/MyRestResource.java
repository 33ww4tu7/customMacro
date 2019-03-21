package com.atlassian.tutorial.rest;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.tutorial.Service.AttachmentsService;
import org.apache.commons.codec.digest.HmacUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;

import static com.atlassian.confluence.user.AuthenticatedUserThreadLocal.get;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A resource of message.
 */
@Path("/message")
public class MyRestResource {

    private final AttachmentsService attachmentsService;

    public MyRestResource(AttachmentsService attachmentsService) {
        this.attachmentsService = checkNotNull(attachmentsService);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response getMessage(@HeaderParam("key") String key) {
        return Response.ok(new MyRestResourceModel("Hello World", key, "test")).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{pageID}")
    public Response setAttachment(@PathParam("pageID") String pageId, @HeaderParam("path") String path) {
        ConfluenceUser confluenceUser = get();
        String userKey = confluenceUser.getKey().toString();
        try {
            attachmentsService.add(path, pageId, userKey);
            return Response.ok().build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }

    }


    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{pageID}")
    public Response getMessageFromPath(@PathParam("pageID") String pageId) {
        ConfluenceUser confluenceUser = get();
        String userKey = confluenceUser.getKey().toString();

        try {
            return Response.ok(new MyRestResourceModel(attachmentsService.getUrl(pageId, userKey), "page", userKey)).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(500).build();
        }
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String generateHash(@HeaderParam("filename") String filename) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[4];
        random.nextBytes(key);
        return HmacUtils.hmacMd5Hex(Arrays.toString(key),filename);
    }
}