package com.doit.univplanner.dto;
import com.doit.univplanner.entity.User;
import com.doit.univplanner.entity.Plan;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlanDto {
    @NotBlank(message = "사용자 ID는 필수입니다.")
    private String userId;

    @NotBlank(message = "계획 이름은 필수입니다.")
    private String name;
    public Plan toEntity(User user) {
        Plan plan = new Plan();
        plan.setName(this.name);
        plan.setUser(user);
        return plan;
    }
}