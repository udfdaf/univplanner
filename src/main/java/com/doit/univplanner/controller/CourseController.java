package com.doit.univplanner.controller;

import com.doit.univplanner.dto.CourseDto;
import com.doit.univplanner.entity.Course;
import com.doit.univplanner.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public Course addCourse(@Valid @RequestBody CourseDto courseDto) {
        return courseService.addCourse(courseDto);
    }
    @GetMapping("/list")
    public String courseList(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "course-list";
    }

    @GetMapping("/plan/{planId}")
    public List<Course> getCoursesByPlan(@PathVariable Long planId) {
        return courseService.getCoursesByPlanId(planId);
    }
}