package com.thinktrail.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModerationResult {
    private boolean flagged;
    private String reason;
    private String error;
}
