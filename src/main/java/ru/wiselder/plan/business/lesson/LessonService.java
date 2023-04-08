package ru.wiselder.plan.business.lesson;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.business.auditorium.AuditoriumDao;
import ru.wiselder.plan.business.faculty.FacultyDao;
import ru.wiselder.plan.business.teacher.TeacherDao;
import ru.wiselder.plan.exception.LessonIntersectionException;
import ru.wiselder.plan.exception.ObjectNotFoundException;
import ru.wiselder.plan.model.Group;
import ru.wiselder.plan.response.GroupLesson;
import ru.wiselder.plan.model.Lesson;
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
    private final TeacherDao teacherDao;
    private final AuditoriumDao auditoriumDao;

    @Transactional
    public GroupLesson addLesson(GroupLessonRequest lesson) {
        checkIntersections(lesson);
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
        checkIntersections(lesson);
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

    public void removeLesson(int lessonId) {
       if (storage.removeLesson(lessonId) == 0) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }
    }

    private void checkIntersections(GroupLessonRequest lesson) {
        storage.getLessonByAuditorium(lesson.bellOrdinal(), lesson.day(),  lesson.auditoriumId())
                .ifPresent(l -> {
                    throw new LessonIntersectionException(AUDITORIUM, l);
                });
        storage.getLessonByTeacher(lesson.bellOrdinal(), lesson.day(), lesson.teacherId())
                .ifPresent(l -> {
                    throw new LessonIntersectionException(TEACHER, l);
                });
        storage.getLessonByGroups(lesson.bellOrdinal(), lesson.day(), lesson.groupIds())
                .ifPresent(l -> {
                    throw new LessonIntersectionException(GROUP, l);
                });

    }

    private List<Group> checkExisting(GroupLessonRequest lesson) {
        teacherDao.findById(lesson.teacherId())
                .orElseThrow(() -> new ObjectNotFoundException("teacher", lesson.teacherId()));
        auditoriumDao.findById(lesson.auditoriumId())
                .orElseThrow(() -> new ObjectNotFoundException("auditorium", lesson.auditoriumId()));

        return lesson.groupIds()
                .stream()
                .map(groupId ->
                        facultyDao.findGroupById(groupId)
                                .orElseThrow(() -> new ObjectNotFoundException("group", groupId)))
                .toList();
    }

    public Lesson getLesson(int id) {
        return storage.getLessonById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
