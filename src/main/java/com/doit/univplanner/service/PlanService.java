package com.doit.univplanner.service;

import com.doit.univplanner.dto.PlanDto;
import com.doit.univplanner.entity.Plan;
import com.doit.univplanner.entity.User;
import com.doit.univplanner.exception.CustomException;
import com.doit.univplanner.exception.ErrorCode;
import com.doit.univplanner.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public PlanDto createPlan(PlanDto planDto, String username) {
        User user = userService.getUserByUsername(username);
        Plan plan = planDto.toEntity(user);
        Plan savedPlan = planRepository.save(plan);
        return PlanDto.from(savedPlan);
    }

    @Transactional(readOnly = true)
    public List<PlanDto> getPlansByUsername(String username) {
        User user = userService.getUserByUsername(username);
        return planRepository.findByUserId(user.getId())
                .stream()
                .map(PlanDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePlan(Long planId, String username) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PLAN));

        // 현재 사용자의 Plan인지 확인
        if (!plan.getUser().getUsername().equals(username)) {
            throw new CustomException(ErrorCode.INVALID_PLAN);
        }

        planRepository.delete(plan);
    }

}