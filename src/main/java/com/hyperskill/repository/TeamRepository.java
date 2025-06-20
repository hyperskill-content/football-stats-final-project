package com.hyperskill.repository;

import com.hyperskill.entity.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {
    public List<Team> getAll();
}
