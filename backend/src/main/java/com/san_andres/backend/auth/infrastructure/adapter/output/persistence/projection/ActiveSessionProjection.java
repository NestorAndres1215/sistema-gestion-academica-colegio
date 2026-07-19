package com.san_andres.backend.auth.infrastructure.adapter.output.persistence.projection;

import java.time.LocalDateTime;

public interface ActiveSessionProjection {

    Long getSessionId();

    Long getUserId();

    String getUsername();

    String getEmail();

    LocalDateTime getLoginAt();
}