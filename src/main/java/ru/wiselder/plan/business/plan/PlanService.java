package ru.wiselder.plan.business.plan;

import java.time.DayOfWeek;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MultiMapUtils;
import org.springframework.stereotype.Service;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherRequest;
import ru.wiselder.plan.response.DayPlan;
import ru.wiselder.plan.response.Plan;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanDao storage;

    public Plan getPlan(GroupRequest request) {
        return toPlan(storage.findLessonsByGroup(request));
    }

    public DayPlan getPlan(GroupDayPlanRequest request) {
        return new DayPlan(storage.findLessonsGroupAndDay(request));
    }

    public Plan getPlan(TeacherRequest request) {
        return toPlan(storage.findLessonsByTeacher(request));
    }

    public DayPlan getPlan(TeacherDayPlanRequest request) {
        return new DayPlan(storage.findLessonsByTeacherAndDay(request));
    }

    private Plan toPlan(List<Lesson> lessons) {
        var map = MultiMapUtils.<DayOfWeek, Lesson>newListValuedHashMap();
        for (var l : lessons) {
            map.put(l.day(), l);
        }
        var list = new DayPlan[7];
        for (var day: DayOfWeek.values()) {
            list[day.ordinal()] = new DayPlan(map.get(day));
        }
        return new Plan(List.of(list));
    }
}
