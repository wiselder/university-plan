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

    public List<Group> getGroups(int faculty) {
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
    public Group getOrCreate(GroupRequest request) {
        var faculty = facultyDao.findById(request.faculty());
        faculty.orElseThrow(() -> new ObjectNotFoundException("faculty", request.faculty()));
        var group = facultyDao.findGroup(request);
        if (group.isEmpty()) {
            facultyDao.createGroup(request);
            group = facultyDao.findGroup(request);
        }

        return group.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
