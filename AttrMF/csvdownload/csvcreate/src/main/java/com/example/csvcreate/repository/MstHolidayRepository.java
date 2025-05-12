package com.example.csvcreate.repository;

import java.time.LocalDate;

import com.example.csvcreate.model.MstHolidayEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MstHolidayRepository extends JpaRepository<MstHolidayEntity, Long> {

    // 指定された日付が祝日テーブルに存在するか
    boolean existsByHolidayDate(LocalDate holidayDate);
}

