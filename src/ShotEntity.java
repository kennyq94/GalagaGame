//package galaga;

import java.util.ArrayList;

/**
 * An entity representing a shot fired by the player's ship
 *
 * @author Khang Quach and Edward Piper
 */
public class ShotEntity extends Entity {

  /**
   * The vertical speed at which the players shot moves
   */
  private double moveSpeed = -300;
  /**
   * The game in which this entity exists
   */
  private Game game;
  /**
   * True if this shot has been "used", i.e. its hit something
   */
  private boolean used = false;

  /**
   * Create a new shot from the player
   *
   * @param game The game in which the shot has been created
   * @param sprite The sprite representing this shot
   * @param x The initial x location of the shot
   * @param y The initial y location of the shot
   */
  public ShotEntity(Game game, String sprite, int x, int y, int direction) {
    super(sprite, x, y);

    this.game = game;

    dy = moveSpeed * direction;
  }

  /**
   * Request that this shot moved based on time elapsed
   *
   * @param delta The time that has elapsed since last move
   */
  public void move(long delta) {
    // proceed with normal move
    super.move(delta);

    // if we shot off the screen, remove ourselfs
    if (y < -100) {
      game.removeEntity(this);
    }
  }

  /**
   * Notification that this shot has collided with another entity
   *
   * @parma other The other entity with which we've collided
   */
  public void collidedWith(Entity entity) {
    // prevents double kills, if we've already hit something,
    // don't collide

    if (used) {
      return;
    }
    //Removes the shot entity in colision
    game.removeEntity(this);
    used = true;
    //checks if its an alien. If it is, the alien loses a life.
    if (entity instanceof AlienEntity) {
      System.out.println(entity.getLife());
      entity.loseLife();
    }

    //If the alien has zero life, remove it.
    if (entity.getLife() <= 0) {
    // if we've hit an alien, kill it!

      game.removeEntity(entity);

      // notify the game that the alien has been killed
      game.notifyAlienKilled();

    }
    //DEPLETE HP FOR SHiP entity if it collides with a laser/shot
    if (entity instanceof ShipEntity) {
      System.out.println(entity.getLife());
      entity.loseLife();

      if (entity.getLife() <= 0) {

        game.removeEntity(entity);

            // notify the game that the ship has been killed
        game.notyifyShipDestroyed();
      }

    }

  }
}
