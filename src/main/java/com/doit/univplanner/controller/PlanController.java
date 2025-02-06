package com.doit.univplanner.controller;

import com.doit.univplanner.dto.PlanDto;
import com.doit.univplanner.entity.Plan;
import com.doit.univplanner.entity.User;
import com.doit.univplanner.exception.CustomException;
import com.doit.univplanner.exception.ErrorResponse;
import com.doit.univplanner.service.PlanService;
import com.doit.univplanner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Controller
@RequestMapping("/plans")
public class PlanController {
    @Autowired
    private PlanService planService;

    @GetMapping("/list")
    public String planList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<PlanDto> plans = planService.getPlansByUsername(userDetails.getUsername());
        model.addAttribute("plans", plans);
        return "plan-list";
    }

    @GetMapping("/new")
    public String newPlanForm(Model model) {
        model.addAttribute("planDto", new PlanDto());
        return "plan-form";
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createPlan(@Valid @RequestBody PlanDto planDto,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            PlanDto createdPlan = planService.createPlan(planDto, userDetails.getUsername());
            return ResponseEntity.ok(createdPlan);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deletePlan(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        planService.deletePlan(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}