package com.atlassian.tutorial.rest;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.tutorial.Service.AttachmentsService;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;

import static com.atlassian.confluence.user.AuthenticatedUserThreadLocal.get;
import static com.google.common.base.Preconditions.checkNotNull;

@Path("/attach")
public class AttachmentsRestResource {
    private static final Logger log = Logger.getLogger(AttachmentsRestResource.class.getName());
    private final AttachmentsService attachmentsService;
    private final ConfluenceUser confluenceUser = get();
    private final String userKey = confluenceUser.getKey().toString();

    public AttachmentsRestResource(AttachmentsService attachmentsService) {
        this.attachmentsService = checkNotNull(attachmentsService);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{pageID}")
    public Response setAttachment(final @PathParam("pageID") String pageId, final @HeaderParam("path") String path, final @HeaderParam("attID") String attId) {
        try {
            attachmentsService.createOrUpload(path, pageId, userKey, attId);
            return Response.ok().build();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{pageID}")
    public Response getUserAttachment(final @PathParam("pageID") String pageId) {
        try {
            return Response.ok(new AttachmentsRestResourceModel(attachmentsService.getUrl(pageId, userKey),
                    attachmentsService.getAttId(pageId, userKey), pageId, userKey)).build();
        } catch (SQLException e) {
            log.error(e.getMessage());
            return Response.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String generateUniqueFilename(final @HeaderParam("filename") String filename) {
        final SecureRandom random = new SecureRandom();
        byte[] key = new byte[4];
        random.nextBytes(key);
        return HmacUtils.hmacMd5Hex(Arrays.toString(key), filename);
    }
}