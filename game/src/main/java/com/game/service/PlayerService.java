package com.game.service;

import com.game.entity.*;
import com.game.entity.PlayerFilterCriteria;
import com.game.repository.PlayerCriteriaRepository;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Date;
import java.util.Objects;



@Service
//@ComponentScan(basePackages = {"com.game.service"})
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerCriteriaRepository playerCriteriaRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, PlayerCriteriaRepository playerCriteriaRepository){
        this.playerRepository = playerRepository;
        this.playerCriteriaRepository = playerCriteriaRepository;

    }

    public Page<Player> getPlayers(PlayerPage playerPage){
        Sort sort = Sort.by(playerPage.getSortDirection(), playerPage.getSortBy());
        Pageable pageable = PageRequest.of(playerPage.getPageNumber(), playerPage.getPageSize(),sort);
        return playerRepository.findAll(pageable);
    }

    public Page<Player> getAllPlayersWithFilter(PlayerPage playerPage, PlayerFilterCriteria playerFilterCriteria){
        return playerCriteriaRepository.findAllWithFilters(playerPage, playerFilterCriteria);
    }

    public Player addPlayer (PlayerModel playerModel) {
        Player player = new Player();

        if(Objects.nonNull(playerModel.getName()) && playerModel.isNameValid()){
            player.setName(playerModel.getName());
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(Objects.nonNull(playerModel.getTitle()) && playerModel.isTitleValid()){
            player.setTitle(playerModel.getTitle());
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(Objects.nonNull(playerModel.getRace())){
            player.setRace(Race.valueOf(playerModel.getRace().name()));
        } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(Objects.nonNull(playerModel.getProfession())){
            player.setProfession(Profession.valueOf(playerModel.getProfession().name()));
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(Objects.nonNull(playerModel.getBirthday())){
            player.setBirthday((java.sql.Date) playerModel.getSqlBirthday());
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(Objects.nonNull(playerModel.isBanned()) && playerModel.isBirthdayValid()){
            player.setBanned(playerModel.isBanned());
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if(Objects.nonNull(playerModel.getExperience()) && playerModel.isExperienceValid()){
            player.setExperience(playerModel.getExperience());
            player.setLevel(calLevel(player.getExperience()));
            player.setUntilNextLevel(calNextLevel(player.getLevel(),player.getExperience()));
        }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

            return playerRepository.save(player);
    }

    public Long getFilterPlayersCount(PlayerPage playerPage, PlayerFilterCriteria playerFilterCriteria){
        return playerCriteriaRepository.findAllWithFilters(playerPage, playerFilterCriteria).getTotalElements();
    }

    public Player getPlayer(long id) {

        if (!Objects.nonNull(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(id<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

       if(!playerRepository.existsById(id)){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
       }

        return playerRepository.findById(id)
                .map(player -> new Player(
                        player.getId(),
                        player.getName(),
                        player.getTitle(),
                        player.getRace(),
                        player.getProfession(),
                        player.getExperience(),
                        player.getLevel(),
                        player.getUntilNextLevel(),
                        (java.sql.Date) player.getBirthday(),
                        player.isBanned())
                ).get();
    }

    public Player updatePlayer(long id, PlayerModel playerModel) {
        Player player = getPlayer(id);

        if(Objects.nonNull(playerModel.getName())){
          if (playerModel.isNameValid()) {
              player.setName(playerModel.getName());
          } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(Objects.nonNull(playerModel.getTitle())){
            if( playerModel.isTitleValid()) {
                player.setTitle(playerModel.getTitle());
            }else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(Objects.nonNull(playerModel.getRace())){
            player.setRace(playerModel.getRace());
        }

        if(Objects.nonNull(playerModel.getProfession())){
            player.setProfession(playerModel.getProfession());
        }

        if(Objects.nonNull(playerModel.getExperience())){
            if(playerModel.isExperienceValid()) {
                player.setExperience(playerModel.getExperience());
                player.setLevel(calLevel(player.getExperience()));
                player.setUntilNextLevel(calNextLevel(player.getLevel(),player.getExperience()));
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(Objects.nonNull(playerModel.getBirthday())) {
            if (playerModel.isBirthdayValid()) {
                player.setBirthday((java.sql.Date) playerModel.getSqlBirthday());
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if(Objects.nonNull(playerModel.isBanned())){
            player.setBanned(playerModel.isBanned());
        }
        return playerRepository.save(player);
    }

    public void deletePlayer(long id) {
        Player player = getPlayer(id);
        playerRepository.delete(player);
    }

    public int calLevel (int exp){
        Double level = ((Math.sqrt(200*exp+2500)-50)/100);
       return level.intValue();
    }

    public int calNextLevel(int lvl, int exp){
        return 50*(lvl+1)*(lvl+2) - exp;
    }


}
