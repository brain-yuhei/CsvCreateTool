package com.example.csvcreate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "MST_HOLIDAY")
public class MstHolidayEntity {
    @Id
    private LocalDate holidayDate;

    private String holidayName;
}


