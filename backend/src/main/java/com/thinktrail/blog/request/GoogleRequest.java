package com.thinktrail.blog.request;

public record GoogleRequest(String token) {
    public String getToken(){
        return token;
    }
}
