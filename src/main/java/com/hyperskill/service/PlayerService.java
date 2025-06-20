package com.hyperskill.service;

import com.hyperskill.entity.Person;
import com.hyperskill.entity.Player;
import com.hyperskill.entity.Team;
import com.hyperskill.repository.PlayerRepository;
import com.hyperskill.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository){
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    public Player add(Player player){
        List<Team> teams = teamRepository.getAll();
        if(!teams.contains(player.getTeam())){
            //TODO add exception
            return null;
        }

        playerRepository.save(player);
        return player;
    }

    public void addAll(List<Player> players){
        playerRepository.saveAll(players);
    }

    public Player getById(long id){
        return playerRepository.getById(id);
    }

    public Player getByFullName(Person person){
        return playerRepository.getByFirstNameAndLastName(person);
    }

    public Collection<Player> getPlayers(){
        //TODO add pagination and sorting
        return playerRepository.getAll();
    }

    public Collection<Player> getPlayersByTeam(Team team){
        return team.getPlayers();
    }

    public Player deleteById(long id){
        if(!playerRepository.existsById(id)){
            //TODO add exception
            return null;
        }

        Player player = playerRepository.getById(id);
        playerRepository.deleteById(id);
        return player;
    }
}
