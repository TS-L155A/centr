package com.ts2.centr.repo;

import com.ts2.centr.models.Havka;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HavkaRepository extends CrudRepository<Havka, Long> {
}
