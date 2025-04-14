package com.example.keirocreate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.keirocreate.model.UserKeiroEntity;

public interface KeiroRepository extends JpaRepository<UserKeiroEntity, Long> {

}

