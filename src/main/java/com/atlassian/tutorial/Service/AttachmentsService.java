package com.atlassian.tutorial.Service;

import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.tutorial.entity.AttachmentsEntity;

import java.sql.SQLException;

@Transactional
public interface AttachmentsService {
    AttachmentsEntity createOrUpload(String path, String pageId, String userId, String attachmentId) throws SQLException;

    AttachmentsEntity[] getEntity(String pageId, String userId) throws SQLException;
}