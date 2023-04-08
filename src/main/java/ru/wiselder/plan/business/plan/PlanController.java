package ru.wiselder.plan.business.plan;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherRequest;
import ru.wiselder.plan.response.DayPlan;
import ru.wiselder.plan.response.Plan;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @PostMapping(value = "/group/day")
    public DayPlan getPlan(@RequestBody @Valid GroupDayPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/group/week")
    public Plan getPlan(@RequestBody @Valid GroupRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/teacher/day")
    public DayPlan getPlan(@RequestBody @Valid TeacherDayPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/teacher/week")
    public Plan getPlan(@RequestBody @Valid TeacherRequest planRequest) {
        return planService.getPlan(planRequest);
    }
}
