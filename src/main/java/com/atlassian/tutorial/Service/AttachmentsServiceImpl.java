package com.atlassian.tutorial.Service;


import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.tutorial.entity.AttachmentsEntity;
import net.java.ao.Query;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

@Scanned
@Named
public class AttachmentsServiceImpl implements AttachmentsService {
    @ComponentImport
    private final ActiveObjects ao;

    @Inject
    public AttachmentsServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public AttachmentsEntity add(String path, String pageId, String userId) {
        final AttachmentsEntity attachmentsEntity = ao.create(AttachmentsEntity.class);
        attachmentsEntity.setPath(path);
        attachmentsEntity.setPageId(pageId);
        attachmentsEntity.setUserId(userId);
        attachmentsEntity.save();
        return attachmentsEntity;
    }

    @Override
    public String getUrl(String pageId, String userId) {
        String path = "";
        AttachmentsEntity[] attachmentsEntity = ao.find(AttachmentsEntity.class, Query.select().where("PAGE_ID = ? AND USER_ID = ?", pageId, userId));
        for (AttachmentsEntity ae : attachmentsEntity) {
            path = ae.getPath();
        }
        return path;
    }
}
