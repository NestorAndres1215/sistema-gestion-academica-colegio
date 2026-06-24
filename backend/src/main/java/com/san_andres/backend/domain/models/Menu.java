package com.san_andres.backend.domain.models;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    private String id;
    private String code;
    private String name;
    private String icon;
    private String route;
    private String menuOrder;
    private String category;
    private Menu parent;
    private List<Menu> children;
    private List<Role> roles;
}