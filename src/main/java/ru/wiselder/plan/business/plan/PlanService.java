package ru.wiselder.plan.business.plan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wiselder.plan.response.GroupLesson;
import ru.wiselder.plan.response.TeacherDayPlan;
import ru.wiselder.plan.response.TeacherWeekPlan;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupWeekPlanRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherWeekPlanRequest;
import ru.wiselder.plan.response.GroupDayPlan;
import ru.wiselder.plan.response.GroupWeekPlan;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanDao planDao;

    public GroupDayPlan getPlan(GroupDayPlanRequest request) {
        return GroupDayPlan.of(request.day(), planDao.findLessons(request));
    }

    public GroupWeekPlan getPlan(GroupWeekPlanRequest request) {
        return GroupWeekPlan.of(planDao.findLessons(request));
    }

    @Transactional
    public TeacherDayPlan getPlan(TeacherDayPlanRequest request) {
        var lessons = planDao.findLessons(request)
                .stream()
                .map(lesson -> new GroupLesson(lesson, planDao.getGroupsByLesson(lesson.id())))
                .toList();
        return TeacherDayPlan.of(request.day(), lessons);
    }

    @Transactional
    public TeacherWeekPlan getPlan(TeacherWeekPlanRequest request) {
        var lessons = planDao.findLessons(request)
                .stream()
                .map(lesson -> new GroupLesson(lesson, planDao.getGroupsByLesson(lesson.id())))
                .toList();
        return TeacherWeekPlan.of(lessons);
    }

}
