package com.atlassian.tutorial.entity;

import net.java.ao.Entity;

public interface AttachmentsEntity extends Entity {
    String getPath();

    void setPath(String path);

    String getPageId();

    void setPageId(String pageId);

    String getUserId();

    void setUserId(String UserId);
}
