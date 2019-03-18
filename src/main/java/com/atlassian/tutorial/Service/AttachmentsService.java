package com.atlassian.tutorial.Service;
import com.atlassian.activeobjects.tx.Transactional;
import com.atlassian.tutorial.entity.AttachmentsEntity;

import java.sql.SQLException;
import java.util.List;

@Transactional
public interface AttachmentsService
{
    AttachmentsEntity add(String path, String pageId, String userId) throws SQLException;

    String getUrl(String pageId, String userId) throws SQLException;

    List<AttachmentsEntity> getAll();
}