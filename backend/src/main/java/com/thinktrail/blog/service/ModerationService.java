package com.thinktrail.blog.service;

import com.thinktrail.blog.model.ModerationResult;

public interface ModerationService {
    public ModerationResult moderateText(String text);
}
