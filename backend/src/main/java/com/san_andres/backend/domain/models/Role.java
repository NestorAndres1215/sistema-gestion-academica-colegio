package com.san_andres.backend.domain.models;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    private Long id;
    private String name;

}
