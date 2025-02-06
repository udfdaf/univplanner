package com.doit.univplanner.repository;

import com.doit.univplanner.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findByUserId(Long userId);
}
