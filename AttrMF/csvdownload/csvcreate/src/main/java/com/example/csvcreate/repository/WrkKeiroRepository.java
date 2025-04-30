package com.example.csvcreate.repository;

import com.example.csvcreate.model.WrkKeiroEntity;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WrkKeiroRepository extends JpaRepository<WrkKeiroEntity, Long> {

    WrkKeiroEntity findByDateAndPayee(LocalDate date, String payee);

}

