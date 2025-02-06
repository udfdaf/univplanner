package com.doit.univplanner.controller;

import com.doit.univplanner.dto.PlanDto;
import com.doit.univplanner.entity.Plan;
import com.doit.univplanner.entity.User;
import com.doit.univplanner.service.PlanService;
import com.doit.univplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plans")
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private UserService userService;
    @GetMapping("/list")

    public String planList(Model model) {
        List<Plan> plans = planService.getAllPlans();
        model.addAttribute("plans", plans);
        return "plan-list";
    }

    @PostMapping("/create")
    public Plan createPlan(@Valid @RequestBody PlanDto planDto, @RequestParam String username) {
        User user = userService.getUserByUsername(username);
        return planService.createPlan(planDto, username);
    }

    @GetMapping("/{username}")
    public List<Plan> getPlansByUser(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return planService.getPlansByUserId(user.getId());
    }
}

