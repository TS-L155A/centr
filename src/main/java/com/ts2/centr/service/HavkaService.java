package com.ts2.centr.service;

import com.ts2.centr.exceptions.HavkaValidationException;
import com.ts2.centr.exceptions.NotFoundException;
import com.ts2.centr.models.Havka;
import com.ts2.centr.repo.HavkaRepository;
import org.springframework.stereotype.Service;

@Service
public class HavkaService {

    private final HavkaRepository havkaRepository;

    public HavkaService (HavkaRepository havkaRepository){
        this.havkaRepository = havkaRepository;
    }

    public Iterable<Havka> getAll() {
        return havkaRepository.findAll();
    }

    public Havka save(Havka havka) {

        if (havka.getQuantity() < 0 || havka.getQuantity() > Integer.MAX_VALUE) {
            throw new HavkaValidationException("Количество должно быть целым числом от 0 до " + Integer.MAX_VALUE);
        }

        return havkaRepository.save(havka);
    }

    public Havka getById (Long id) {
        return havkaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар не найден: " + id));
    }

    public void delete(Long id) {
        havkaRepository.deleteById(id);
    }
}
