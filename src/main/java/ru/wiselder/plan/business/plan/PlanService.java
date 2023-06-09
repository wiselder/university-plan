package ru.wiselder.plan.business.plan;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.business.faculty.FacultyDao;
import ru.wiselder.plan.business.teacher.TeacherDao;
import ru.wiselder.plan.model.Bell;
import ru.wiselder.plan.model.Lesson;
import ru.wiselder.plan.request.GroupRequest;
import ru.wiselder.plan.response.GroupLesson;
import ru.wiselder.plan.request.GroupDayPlanRequest;
import ru.wiselder.plan.request.GroupWeekPlanRequest;
import ru.wiselder.plan.request.TeacherDayPlanRequest;
import ru.wiselder.plan.request.TeacherWeekPlanRequest;
import ru.wiselder.plan.response.DayPlan;
import ru.wiselder.plan.response.WeekPlan;

@Service
@RequiredArgsConstructor
public class PlanService {
    private final FacultyDao facultyDao;
    private final TeacherDao teacherDao;
    private final PlanDao planDao;
    private final PlanFormer planFormer;

    @Transactional
    public DayPlan getPlan(GroupDayPlanRequest request) {
        var group = request.group();
        facultyDao.findGroup(group.faculty(), new GroupRequest(group.course(), group.number(), group.subNumber()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var lessons = toGroupLessons(planDao.findLessons(request));
        return planFormer.form(request.day(), getAllBells(), lessons, request.group());
    }


    @Transactional
    public WeekPlan getPlan(GroupWeekPlanRequest group) {
        facultyDao.findGroup(group.faculty(), new GroupRequest(group.course(), group.number(), group.subNumber()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var lessons = toGroupLessons(planDao.findLessons(group));
        return planFormer.form(getAllBells(), lessons, group);
    }

    @Transactional
    public DayPlan getPlan(TeacherDayPlanRequest request) {
        teacherDao.findById(request.teacherId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var lessons = toGroupLessons(planDao.findLessons(request));
        return planFormer.form(request.day(), getAllBells(), lessons, request.teacherId());
    }

    @Transactional
    public WeekPlan getPlan(TeacherWeekPlanRequest request) {
        teacherDao.findById(request.teacherId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var lessons = toGroupLessons(planDao.findLessons(request));
        return planFormer.form(getAllBells(), lessons, request.teacherId());
    }

    public List<Bell> getAllBells() {
        return planDao.findAllBells();
    }

    private List<GroupLesson> toGroupLessons(List<Lesson> lessons) {
        return lessons.stream()
                .map(lesson -> new GroupLesson(lesson, planDao.getGroupsByLesson(lesson.id())))
                .toList();
    }
}
