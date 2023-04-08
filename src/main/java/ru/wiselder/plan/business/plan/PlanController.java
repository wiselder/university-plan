package ru.wiselder.plan.business.plan;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.response.TeacherDayPlan;
import ru.wiselder.plan.response.TeacherWeekPlan;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupWeekPlanRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherWeekPlanRequest;
import ru.wiselder.plan.response.GroupDayPlan;
import ru.wiselder.plan.response.GroupWeekPlan;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @PostMapping(value = "/group/day")
    public GroupDayPlan getPlan(@RequestBody @Valid GroupDayPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/group/week")
    public GroupWeekPlan getPlan(@RequestBody @Valid GroupWeekPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/teacher/day")
    public TeacherDayPlan getPlan(@RequestBody @Valid TeacherDayPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/teacher/week")
    public TeacherWeekPlan getPlan(@RequestBody @Valid TeacherWeekPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }
}
