package ru.wiselder.plan.business.plan;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiselder.plan.model.Bell;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupWeekPlanRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherWeekPlanRequest;
import ru.wiselder.plan.response.DayPlan;
import ru.wiselder.plan.response.WeekPlan;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping(value = "/bells")
    public List<Bell> getAllBells() {
        return planService.getAllBells();
    }

    @PostMapping(value = "/group/day")
    public DayPlan getPlan(@RequestBody @Valid GroupDayPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/group/week")
    public WeekPlan getPlan(@RequestBody @Valid GroupWeekPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/teacher/day")
    public DayPlan getPlan(@RequestBody @Valid TeacherDayPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }

    @PostMapping(value = "/teacher/week")
    public WeekPlan getPlan(@RequestBody @Valid TeacherWeekPlanRequest planRequest) {
        return planService.getPlan(planRequest);
    }
}
