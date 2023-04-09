package ru.wiselder.plan.business.faculty;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.exception.ObjectNotFoundException;
import ru.wiselder.plan.model.Faculty;
import ru.wiselder.plan.model.Group;
import ru.wiselder.plan.request.CreateFacultyRequest;
import ru.wiselder.plan.request.GroupRequest;

@Service
@RequiredArgsConstructor
public class FacultyService {
    private final FacultyDao facultyDao;
    public List<Faculty> getAll() {
        return facultyDao.findAll();
    }

    @Transactional
    public List<Group> getGroups(int faculty) {
        facultyDao.findById(faculty)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return facultyDao.findGroupsByFaculty(faculty);
    }

    @Transactional
    public Faculty getOrCreate(CreateFacultyRequest request) {
        var faculty = facultyDao.findByName(request.name());
        if (faculty.isEmpty()) {
            facultyDao.create(request);
            faculty = facultyDao.findByName(request.name());
        }
        return faculty.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Transactional
    public Group getOrCreate(int facultyId, GroupRequest request) {
        var faculty = facultyDao.findById(facultyId);
        faculty.orElseThrow(() -> new ObjectNotFoundException("faculty", facultyId));
        var group = facultyDao.findGroup(facultyId, request);
        if (group.isEmpty()) {
            facultyDao.createGroup(facultyId, request);
            group = facultyDao.findGroup(facultyId, request);
        }

        return group.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
