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

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Plan createPlan(PlanDto planDto, String username) {
        User user = userService.getUserByUsername(username);

        // userId와 실제 사용자의 id가 일치하는지 검증
        if (!user.getId().toString().equals(planDto.getUserId())) {
            throw new CustomException(ErrorCode.INVALID_PLAN);
        }

        Plan plan = planDto.toEntity(user);
        return planRepository.save(plan);
    }
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Plan> getPlansByUserId(Long userId) {
        return planRepository.findByUserId(userId);
    }
}