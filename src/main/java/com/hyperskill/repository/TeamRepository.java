package com.hyperskill.repository;

import com.hyperskill.entity.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TeamRepository extends CrudRepository<Team, UUID> {
    public List<Team> getAll();
}
