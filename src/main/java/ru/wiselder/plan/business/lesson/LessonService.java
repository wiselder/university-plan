package ru.wiselder.plan.business.lesson;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.business.auditorium.AuditoriumDao;
import ru.wiselder.plan.business.discipline.DisciplineDao;
import ru.wiselder.plan.business.faculty.FacultyDao;
import ru.wiselder.plan.business.plan.PlanDao;
import ru.wiselder.plan.business.teacher.TeacherDao;
import ru.wiselder.plan.exception.LessonIntersectionException;
import ru.wiselder.plan.exception.ObjectNotFoundException;
import ru.wiselder.plan.model.Group;
import ru.wiselder.plan.response.GroupLesson;
import ru.wiselder.plan.model.LessonInfo;
import ru.wiselder.plan.request.GroupLessonRequest;

import static ru.wiselder.plan.exception.LessonIntersectionException.Type.AUDITORIUM;
import static ru.wiselder.plan.exception.LessonIntersectionException.Type.GROUP;
import static ru.wiselder.plan.exception.LessonIntersectionException.Type.TEACHER;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonDao storage;
    private final FacultyDao facultyDao;
    private final PlanDao planDao;
    private final TeacherDao teacherDao;
    private final DisciplineDao disciplineDao;
    private final AuditoriumDao auditoriumDao;

    @Transactional
    public GroupLesson addLesson(GroupLessonRequest lesson) {
        checkCreateIntersections(lesson);
        var groups = checkExisting(lesson);
        storage.createLesson(new LessonInfo(
                lesson.disciplineId(),
                lesson.auditoriumId(),
                lesson.teacherId(),
                lesson.day(),
                lesson.bellOrdinal()
        ));
        var created = storage.getLessonByAuditorium(lesson.bellOrdinal(), lesson.day(), lesson.auditoriumId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        storage.resetGroups(created.id(), lesson.groupIds());
        return new GroupLesson(created, groups);
    }

    @Transactional
    public GroupLesson editLesson(int lessonId, GroupLessonRequest lesson) {
        storage.getLessonById(lessonId)
                .orElseThrow(() -> new ObjectNotFoundException("lesson", lessonId));
        checkEditIntersections(lesson, lessonId);
        var groups = checkExisting(lesson);
        storage.updateLesson(lessonId, new LessonInfo(
                lesson.disciplineId(),
                lesson.auditoriumId(),
                lesson.teacherId(),
                lesson.day(),
                lesson.bellOrdinal()
        ));
        storage.resetGroups(lessonId, lesson.groupIds());

        var edited = storage.getLessonById(lessonId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        return new GroupLesson(edited, groups);
    }

    @Transactional
    public void removeLesson(int lessonId) {
        if (storage.removeLesson(lessonId) == 0) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }
    }

    private void checkCreateIntersections(GroupLessonRequest lesson) {
        storage.getLessonByAuditorium(lesson.bellOrdinal(), lesson.day(),  lesson.auditoriumId())
                .ifPresent(l -> {
                    throw new LessonIntersectionException(AUDITORIUM, l);
                });
        storage.getLessonByTeacher(lesson.bellOrdinal(), lesson.day(), lesson.teacherId())
                .ifPresent(l -> {
                    throw new LessonIntersectionException(TEACHER, l);
                });
        var existingLessons = storage.getLessonByGroups(lesson.bellOrdinal(), lesson.day(), lesson.groupIds());
        if (!existingLessons.isEmpty()) {
            throw new LessonIntersectionException(GROUP, existingLessons.get(0));
        }
    }

    private void checkEditIntersections(GroupLessonRequest lesson, int lessonId) {
        storage.getLessonByAuditorium(lesson.bellOrdinal(), lesson.day(),  lesson.auditoriumId())
                .ifPresent(l -> {
                    if (l.id() != lessonId) {
                        throw new LessonIntersectionException(AUDITORIUM, l);
                    }
                });
        storage.getLessonByTeacher(lesson.bellOrdinal(), lesson.day(), lesson.teacherId())
                .ifPresent(l -> {
                    if (l.id() != lessonId) {
                        throw new LessonIntersectionException(TEACHER, l);
                    }
                });
        var existingLessons = storage.getLessonByGroups(lesson.bellOrdinal(), lesson.day(), lesson.groupIds());
        for (var l: existingLessons) {
            if (l.id() != lessonId) {
                throw new LessonIntersectionException(GROUP, l);
            }
        }
    }

    private List<Group> checkExisting(GroupLessonRequest lesson) {
        teacherDao.findById(lesson.teacherId())
                .orElseThrow(() -> new ObjectNotFoundException("teacher", lesson.teacherId()));
        auditoriumDao.findById(lesson.auditoriumId())
                .orElseThrow(() -> new ObjectNotFoundException("auditorium", lesson.auditoriumId()));
        disciplineDao.findById(lesson.disciplineId())
                .orElseThrow(() -> new ObjectNotFoundException("discipline", lesson.disciplineId()));

        return lesson.groupIds()
                .stream()
                .map(groupId ->
                        facultyDao.findGroupById(groupId)
                                .orElseThrow(() -> new ObjectNotFoundException("group", groupId)))
                .toList();
    }

    public GroupLesson getLesson(int id) {
        var lesson = storage.getLessonById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var groups = planDao.getGroupsByLesson(id);
        return new GroupLesson(lesson, groups);
    }
}
