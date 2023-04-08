package ru.wiselder.plan.business.discipline;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.model.Discipline;
import ru.wiselder.plan.request.CreateDisciplineRequest;

@Service
@RequiredArgsConstructor
public class DisciplineService {
    private final DisciplineDao disciplineDao;
    public List<Discipline> getDisciplineLikeName(String namePattern) {
        return disciplineDao.findLikeName(namePattern);
    }

    @Transactional
    public Discipline getOrCreate(CreateDisciplineRequest request) {
        var discipline = disciplineDao.findByName(request.name());
        if (discipline.isEmpty()) {
            disciplineDao.create(request);
            discipline = disciplineDao.findByName(request.name());
        }
        return discipline.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
