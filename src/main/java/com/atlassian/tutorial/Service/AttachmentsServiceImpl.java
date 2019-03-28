package com.atlassian.tutorial.Service;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.tutorial.entity.AttachmentsEntity;
import net.java.ao.Query;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

@Named
public class AttachmentsServiceImpl implements AttachmentsService {

    private static final String SQL_QUERY = "PAGE_ID = ? AND USER_ID = ? ORDER BY ID";

    @ComponentImport
    private final ActiveObjects ao;

    @Inject
    public AttachmentsServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public AttachmentsEntity createOrUpload(final String path, final String pageId, final String userId) {
        final AttachmentsEntity[] ae = ao.find(AttachmentsEntity.class, Query.select().where(SQL_QUERY, pageId, userId));
        if (ae.length == 0) {
            return create(path, pageId, userId);
        } else {
            return upload(ae[0], path);
        }
    }

    private AttachmentsEntity create(final String path, final String pageId, final String userId) {
        final AttachmentsEntity attachmentsEntity = ao.create(AttachmentsEntity.class);
        attachmentsEntity.setPath(path);
        attachmentsEntity.setPageId(pageId);
        attachmentsEntity.setUserId(userId);
        attachmentsEntity.save();
        return attachmentsEntity;
    }

    private AttachmentsEntity upload(AttachmentsEntity attachmentsEntity, final String path) {
        attachmentsEntity.setPath(path);
        attachmentsEntity.save();
        return attachmentsEntity;
    }

    @Override
    public String getUrl(final String pageId, final String userId) {
        final AttachmentsEntity[] attachmentsEntity = ao.find(AttachmentsEntity.class, Query.select().where(SQL_QUERY, pageId, userId));
        if (attachmentsEntity.length != 0) {
            return attachmentsEntity[0].getPath();
        }
        return "none";
    }
}