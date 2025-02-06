package com.doit.univplanner.dto;
import com.doit.univplanner.entity.User;
import com.doit.univplanner.entity.Plan;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlanDto {
    private Long id;  // 응답 시 필요한 id 필드 추가

    @NotBlank(message = "계획 이름은 필수입니다.")
    private String name;

    public Plan toEntity(User user) {
        Plan plan = new Plan();
        plan.setName(this.name);
        plan.setUser(user);
        return plan;
    }

    // Entity를 DTO로 변환하는 정적 메서드
    public static PlanDto from(Plan plan) {
        PlanDto dto = new PlanDto();
        dto.setId(plan.getId());
        dto.setName(plan.getName());
        return dto;
    }
}