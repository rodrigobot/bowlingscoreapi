package com.robot.bowlingscore.repository;

import com.robot.bowlingscore.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Use for communication between the model and the game service
 *
 * @author Rodrigo Soto
 */
@Repository
public interface PlayerRepository extends CrudRepository<Player, String> {
    Player findByName(String name);
    Player findByNameAndGameId(String name, Long gameId);
}
