package com.atlassian.tutorial.Service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.tutorial.entity.AttachmentsEntity;
import net.java.ao.Query;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;

import static com.google.common.base.Preconditions.checkNotNull;

@Named
public class AttachmentsServiceImpl implements AttachmentsService {

    private static final String SQL_QUERY = "PAGE_ID = ? AND USER_ID = ?";

    @ComponentImport
    private final ActiveObjects ao;

    @Inject
    public AttachmentsServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public AttachmentsEntity createOrUpload(final String path, final String pageId, final String userId, final String attId) {
        final AttachmentsEntity[] ae = ao.find(AttachmentsEntity.class, Query.select().where(SQL_QUERY, pageId, userId));
        if (ae.length == 0) {
            return create(path, pageId, userId, attId);
        } else {
            return upload(ae[0], path, attId);
        }
    }

    private AttachmentsEntity create(final String path, final String pageId, final String userId, final String attId) {
        final AttachmentsEntity attachmentsEntity = ao.create(AttachmentsEntity.class);
        attachmentsEntity.setPath(path);
        attachmentsEntity.setPageId(pageId);
        attachmentsEntity.setUserId(userId);
        attachmentsEntity.setAttId(attId);
        attachmentsEntity.save();
        return attachmentsEntity;
    }

    private AttachmentsEntity upload(AttachmentsEntity attachmentsEntity, final String path, final String attId) {
        attachmentsEntity.setPath(path);
        attachmentsEntity.setAttId(attId);
        attachmentsEntity.save();
        return attachmentsEntity;
    }

    @Override
    public AttachmentsEntity[] getEntity(final String pageId, final String userId) {
        return ao.find(AttachmentsEntity.class, Query.select().where(SQL_QUERY, pageId, userId));
    }
}