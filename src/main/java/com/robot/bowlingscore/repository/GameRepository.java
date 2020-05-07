package com.robot.bowlingscore.repository;

import com.robot.bowlingscore.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Use for communication between the model and the game service
 *
 * @author Rodrigo Soto
 */
@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    Game findById(long id);
}
