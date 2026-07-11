package com.san_andres.backend.infrastructure.persistence.projection;

public interface PercentageStatisticProjection {

    String getLabel();

    Long getQuantity();

    Double getPercentage();

}