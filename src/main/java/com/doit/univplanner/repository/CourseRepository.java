package com.doit.univplanner.repository;

import com.doit.univplanner.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByPlanId(Long planId);
    List<Course> findByPlanUserId(Long userId);
}
