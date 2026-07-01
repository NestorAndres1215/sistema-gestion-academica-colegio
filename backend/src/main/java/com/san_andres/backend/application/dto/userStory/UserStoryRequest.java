package com.san_andres.backend.application.dto.userStory;

import lombok.Data;

@Data
public class UserStoryRequest {

    private String action;
    private String detail;
    private String email;
}