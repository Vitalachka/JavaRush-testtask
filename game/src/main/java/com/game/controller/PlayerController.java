package com.game.controller;

import com.game.entity.Player;
import com.game.entity.PlayerFilterCriteria;
import com.game.entity.PlayerModel;
import com.game.entity.PlayerPage;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping (value = "")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
       this.playerService = playerService;
    }

    @RequestMapping (value = "/rest/players", method = RequestMethod.GET)
    public List<Player> getPlayers(PlayerPage playerPage, PlayerFilterCriteria playerFilterCriteria){
    return new ResponseEntity<>(playerService.getAllPlayersWithFilter(playerPage, playerFilterCriteria), HttpStatus.OK).getBody().toList();
    }

   @RequestMapping (value = "/rest/players/count", method = RequestMethod.GET)
    public Integer getPlayersCount(PlayerPage playerPage, PlayerFilterCriteria playerFilterCriteria){
        return new ResponseEntity<>(playerService.getFilterPlayersCount(playerPage, playerFilterCriteria).intValue(),HttpStatus.OK).getBody();
   }

    @RequestMapping (value ="/rest/players", method = RequestMethod.POST)
    public ResponseEntity<Player> addPlayer (@RequestBody PlayerModel playerModel){
        return new ResponseEntity<>(playerService.addPlayer(playerModel), HttpStatus.OK);
    }

    @RequestMapping (value = "/rest/players/{id}" , method = RequestMethod.GET)
    public Player getPlayer (@PathVariable ("id") String id){
        long longId;
        try{
            longId = Long.valueOf(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return  playerService.getPlayer(longId);
    }

    @RequestMapping (value = "/rest/players/{id}", method = RequestMethod.POST)
    public Player updatePlayer (@PathVariable("id") long id, @RequestBody PlayerModel playerModel){
        return playerService.updatePlayer(id, playerModel);
    }

    @RequestMapping (value = "/rest/players/{id}", method = RequestMethod.DELETE)
    public void deletePlayer (@PathVariable ("id") long id){
        playerService.deletePlayer(id);
    }
}
