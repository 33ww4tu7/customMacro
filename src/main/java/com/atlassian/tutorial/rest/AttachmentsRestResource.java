package com.atlassian.tutorial.rest;

import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.tutorial.Service.AttachmentsService;
import com.atlassian.tutorial.entity.AttachmentsEntity;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.SecureRandom;
import java.sql.SQLException;


import static com.atlassian.confluence.user.AuthenticatedUserThreadLocal.get;
import static com.google.common.base.Preconditions.checkNotNull;

@Path("/attach")
public class AttachmentsRestResource {
    private static final Logger log = Logger.getLogger(AttachmentsRestResource.class.getName());
    private final AttachmentsService attachmentsService;

    public AttachmentsRestResource(AttachmentsService attachmentsService) {
        this.attachmentsService = checkNotNull(attachmentsService);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("/{pageID}")
    public Response setAttachment(final @PathParam("pageID") String pageId, final @HeaderParam("path") String path, final @HeaderParam("AttachmentId") String attachmentId) {
        try {
            attachmentsService.createOrUpload(path, pageId, getUserKey(), attachmentId);
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
        final String userKey = getUserKey();
        try {
            final AttachmentsEntity[] attachmentsEntity = attachmentsService.getEntity(pageId, userKey);
            if (attachmentsEntity.length == 0) {
                return Response.status(HttpStatus.SC_NOT_FOUND).build();
            } else {
                return Response.ok(new AttachmentsRestResourceModel(attachmentsEntity[0].getPath(),
                        attachmentsEntity[0].getAttachmentId(), pageId, userKey)).build();
            }
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
        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_MD5, key);
        return hmacUtils.hmacHex(filename);
    }

    private String getUserKey() {
        final ConfluenceUser confluenceUser = get();
        return confluenceUser.getKey().toString();
    }
}