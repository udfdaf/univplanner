package com.doit.univplanner.dto;

import com.doit.univplanner.entity.Course;
import com.doit.univplanner.entity.Plan;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDto {
    @NotBlank(message = "강의명은 필수입니다.")
    private String name;

    @NotNull(message = "마일리지는 필수입니다.")
    @Min(value = 0, message = "마일리지는 0 이상이어야 합니다.")
    private int mileage;

    @NotBlank(message = "요일은 필수입니다.")
    private String dayOfWeek;

    @NotNull(message = "시작 교시는 필수입니다.")
    @Min(value = 1, message = "시작 교시는 1 이상이어야 합니다.")
    @Max(value = 9, message = "시작 교시는 9 이하여야 합니다.")
    private int startPeriod;

    @NotNull(message = "종료 교시는 필수입니다.")
    @Min(value = 1, message = "종료 교시는 1 이상이어야 합니다.")
    @Max(value = 9, message = "종료 교시는 9 이하여야 합니다.")
    private int endPeriod;

    @NotBlank(message = "강의 유형은 필수입니다.")
    private String type;

    private String professor;

    @NotNull(message = "학점은 필수입니다.")
    @Min(value = 1, message = "학점은 1 이상이어야 합니다.")
    @Max(value = 4, message = "학점은 4 이하여야 합니다.")
    private int credits;

    @NotNull(message = "계획 ID는 필수입니다.")
    private Long planId;

    public Course toEntity(Plan plan) {
        Course course = new Course();
        course.setName(this.name);
        course.setMileage(this.mileage);
        course.setDayOfWeek(this.dayOfWeek);
        course.setStartPeriod(this.startPeriod);
        course.setEndPeriod(this.endPeriod);
        course.setType(this.type);
        course.setProfessor(this.professor);
        course.setCredits(this.credits);
        course.setPlan(plan);
        return course;
    }
}