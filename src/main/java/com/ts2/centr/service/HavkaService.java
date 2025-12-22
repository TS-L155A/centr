package com.ts2.centr.service;

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
        return havkaRepository.save(havka);
    }

    public Havka getById (Long id) {
        return havkaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
    }

    public void delete(Long id) {
        havkaRepository.deleteById(id);
    }
}
