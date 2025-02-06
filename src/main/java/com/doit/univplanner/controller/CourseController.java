package com.doit.univplanner.controller;

import com.doit.univplanner.dto.CourseDto;
import com.doit.univplanner.entity.Course;
import com.doit.univplanner.exception.CustomException;
import com.doit.univplanner.exception.ErrorResponse;
import com.doit.univplanner.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/plan/{planId}")
    public String viewCourses(@PathVariable Long planId, Model model) {
        List<CourseDto> courses = courseService.getCoursesByPlanId(planId);

        // 총 학점과 마일리지 계산
        int totalCredits = 0;
        int totalMileage = 0;
        for (CourseDto course : courses) {
            totalCredits += course.getCredits();
            totalMileage += course.getMileage();
        }

        model.addAttribute("courses", courses);
        model.addAttribute("planId", planId);
        model.addAttribute("totalCredits", totalCredits);
        model.addAttribute("totalMileage", totalMileage);
        return "course-list";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addCourse(@Valid @RequestBody CourseDto courseDto) {
        try {
            CourseDto savedCourse = courseService.addCourse(courseDto);
            return ResponseEntity.ok(savedCourse);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().build();
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}