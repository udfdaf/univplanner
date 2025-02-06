package com.doit.univplanner.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Course {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false)
    private String dayOfWeek;

    @Column(nullable = false)
    private int startPeriod;

    @Column(nullable = false)
    private int endPeriod;

    @Column(nullable = false)
    private String type;

    private String professor;

    @Column(nullable = false)
    private int credits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;
}
