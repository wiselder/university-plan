package ru.wiselder.plan.business.auditorium;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.wiselder.plan.model.Auditorium;
import ru.wiselder.plan.request.CreateAuditoriumRequest;

@Service
@RequiredArgsConstructor
public class AuditoriumService {
    private final AuditoriumDao auditoriumDao;
    public List<Auditorium> getAuditoriumLikeName(String namePattern) {
        return auditoriumDao.findLikeName(namePattern);
    }

    @Transactional
    public Auditorium getOrCreate(CreateAuditoriumRequest request) {
        var auditorium = auditoriumDao.find(request);
        if (auditorium.isEmpty()) {
            auditoriumDao.create(request);
            auditorium = auditoriumDao.find(request);
        }
        return auditorium.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public Optional<Auditorium> getById(int id) {
        return auditoriumDao.findById(id);
    }
}
