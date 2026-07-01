package com.san_andres.backend.infrastructure.persistence.repositories;

import com.san_andres.backend.infrastructure.persistence.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface JpaCompanyRepository extends JpaRepository<CompanyEntity,String> {

    Optional<CompanyEntity> findByName(String name);

    @Query("SELECT MAX(c.id) FROM CompanyEntity c")
    String findLastCode();

}