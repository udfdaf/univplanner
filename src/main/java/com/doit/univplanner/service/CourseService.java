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
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PlanRepository planRepository;

    @Transactional
    public CourseDto addCourse(CourseDto courseDto) {
        // 1. Plan 존재 여부 확인
        Plan plan = planRepository.findById(courseDto.getPlanId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PLAN));

        // 2. 유효성 검사
        validateCourse(courseDto, plan.getId());

        try {
            // 3. Entity 변환 및 저장
            Course course = courseDto.toEntity(plan);
            Course savedCourse = courseRepository.save(course);
            return CourseDto.toDto(savedCourse);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_COURSE);
        }
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getCoursesByPlanId(Long planId) {
        // Plan 존재 여부 먼저 확인
        if (!planRepository.existsById(planId)) {
            throw new CustomException(ErrorCode.INVALID_PLAN);
        }

        return courseRepository.findByPlanId(planId)
                .stream()
                .map(CourseDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_COURSE));
        courseRepository.delete(course);
    }

    private void validateCourse(CourseDto courseDto, Long planId) {
        // 1. 종료 시간이 시작 시간보다 늦은지 확인
        if (courseDto.getEndPeriod() < courseDto.getStartPeriod()) {
            throw new CustomException(ErrorCode.INVALID_COURSE);
        }

        // 2. 시간표 중복 체크
        if (hasTimeConflict(courseDto, planId)) {
            throw new CustomException(ErrorCode.COURSE_TIME_CONFLICT);
        }

        // 3. 학점 제한 체크
        validateCreditsLimit(planId, courseDto.getCredits());
    }

    private boolean hasTimeConflict(CourseDto newCourse, Long planId) {
        List<Course> existingCourses = courseRepository.findByPlanId(planId);

        return existingCourses.stream().anyMatch(existing ->
                existing.getDayOfWeek().equals(newCourse.getDayOfWeek()) &&
                        // 시간 중복 검사 로직 수정
                        // 한 교시가 겹치는 경우는 허용 (예: 1-3교시와 3-5교시)
                        (newCourse.getStartPeriod() < existing.getEndPeriod() &&
                                newCourse.getEndPeriod() > existing.getStartPeriod())
        );
    }
    private void validateCreditsLimit(Long planId, int newCredits) {
        List<Course> existingCourses = courseRepository.findByPlanId(planId);
        int totalCredits = existingCourses.stream()
                .mapToInt(Course::getCredits)
                .sum() + newCredits;

        if (totalCredits > 21) {
            throw new CustomException(ErrorCode.CREDITS_EXCEEDED);  // 새로운 에러 코드 필요
        }
    }
}
