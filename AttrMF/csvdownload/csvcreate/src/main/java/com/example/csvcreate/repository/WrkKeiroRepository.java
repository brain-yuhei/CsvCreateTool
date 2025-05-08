package com.example.csvcreate.repository;

import com.example.csvcreate.model.WrkKeiroEntity;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface WrkKeiroRepository extends JpaRepository<WrkKeiroEntity, Long> {

    WrkKeiroEntity findByDateAndPayee(LocalDate date, String payee);

    void deleteByPayeeAndDateBetween(String payee, LocalDate startDate, LocalDate endDate);

    Optional<WrkKeiroEntity> findByPayeeAndDate(String payee, LocalDate date);

}



