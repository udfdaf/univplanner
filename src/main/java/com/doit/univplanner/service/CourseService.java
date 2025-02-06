package com.doit.univplanner.service;

import com.doit.univplanner.dto.CourseDto;
import com.doit.univplanner.entity.Course;
import com.doit.univplanner.entity.Plan;
import com.doit.univplanner.exception.CustomException;
import com.doit.univplanner.exception.ErrorCode;
import com.doit.univplanner.repository.CourseRepository;
import com.doit.univplanner.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PlanRepository planRepository;

    @Transactional
    public Course addCourse(CourseDto courseDto) {
        Plan plan = planRepository.findById(courseDto.getPlanId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PLAN));

        validateCourse(courseDto, plan.getId());

        Course course = courseDto.toEntity(plan);
        return courseRepository.save(course);
    }
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<Course> getCoursesByPlanId(Long planId) {
        return courseRepository.findByPlanId(planId);
    }

    private void validateCourse(CourseDto courseDto, Long planId) {
        // 시간표 중복 체크
        if (hasTimeConflict(courseDto, planId)) {
            throw new CustomException(ErrorCode.COURSE_TIME_CONFLICT);
        }

        // 종료 시간이 시작 시간보다 늦은지 확인
        if (courseDto.getEndPeriod() < courseDto.getStartPeriod()) {
            throw new CustomException(ErrorCode.INVALID_COURSE);
        }

        // 추가적인 유효성 검사 (예: 최대 학점 체크 등)
        validateCreditsLimit(planId, courseDto.getCredits());
    }

    private boolean hasTimeConflict(CourseDto newCourse, Long planId) {
        List<Course> existingCourses = courseRepository.findByPlanId(planId);

        return existingCourses.stream().anyMatch(existing ->
                existing.getDayOfWeek().equals(newCourse.getDayOfWeek()) &&
                        ((newCourse.getStartPeriod() >= existing.getStartPeriod() &&
                                newCourse.getStartPeriod() <= existing.getEndPeriod()) ||
                                (newCourse.getEndPeriod() >= existing.getStartPeriod() &&
                                        newCourse.getEndPeriod() <= existing.getEndPeriod()))
        );
    }

    private void validateCreditsLimit(Long planId, int newCredits) {
        List<Course> existingCourses = courseRepository.findByPlanId(planId);
        int totalCredits = existingCourses.stream()
                .mapToInt(Course::getCredits)
                .sum() + newCredits;

        // 예시: 최대 21학점
        if (totalCredits > 21) {
            throw new CustomException(ErrorCode.INVALID_COURSE);
        }
    }
}