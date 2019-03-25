package com.atlassian.tutorial.rest;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.tutorial.Service.AttachmentsService;
import com.atlassian.tutorial.Service.AttachmentsServiceImpl;
import org.apache.commons.codec.digest.HmacUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Logger;

import static com.atlassian.confluence.user.AuthenticatedUserThreadLocal.get;
import static com.google.common.base.Preconditions.checkNotNull;


@Path("/attach")
public class AttachmentsRestResource {

    private static final Logger log = Logger.getLogger(AttachmentsServiceImpl.class.getName());



    private final AttachmentsService attachmentsService;

    public AttachmentsRestResource(AttachmentsService attachmentsService) {
        this.attachmentsService = checkNotNull(attachmentsService);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{pageID}")
    public final Response setAttachment(@PathParam("pageID") String pageId, @HeaderParam("path") String path) {
        ConfluenceUser confluenceUser = get();
        String userKey = confluenceUser.getKey().toString();
        try {
            attachmentsService.add(path, pageId, userKey);
            return Response.ok().build();
        } catch (SQLException e) {
            log.info(e.getMessage());
            return Response.status(500).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{pageID}")
    public final Response getAttachmentPath(@PathParam("pageID") String pageId) {
        ConfluenceUser confluenceUser = get();
        String userKey = confluenceUser.getKey().toString();
        try {
            return Response.ok(new AttachmentsRestResourceModel(attachmentsService.getUrl(pageId, userKey), "page", userKey)).build();
        } catch (SQLException e) {
            log.info(e.getMessage());
            return Response.status(500).build();
        }
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public final String generateUniqueFilename(@HeaderParam("filename") String filename) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[4];
        random.nextBytes(key);
        return HmacUtils.hmacMd5Hex(Arrays.toString(key), filename);
    }
}