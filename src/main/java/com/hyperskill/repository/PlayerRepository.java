package com.hyperskill.repository;

import com.hyperskill.entity.Person;
import com.hyperskill.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player getById(long id);
    Player getByFirstNameAndLastName(Person person);
    Collection<Player> getAll();
    Player deleteById(long id);
}
