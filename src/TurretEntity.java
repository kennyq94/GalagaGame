/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package galaga;

/**
 *
 * @author Edward
 */
public class TurretEntity extends Entity {

  private Game game;
  private int x;
  private int y;

  /**
   * constructs a turret entity.
   *
   * @param game that game in which it is created
   * @param ref the sprite it refers too
   * @param x the x position
   * @param y the y position
   * @param direction the direction the turret will face
   * @author khang Quach and Edward Piper
   */
  public TurretEntity(Game game, String ref, int x, int y, int direction) {
    super(ref, x, y);
    this.life = 1;
    this.game = game;
    this.age = 500;
    this.direction = direction;

  }

  //if the turret collides with anything, destroy it

  public void collidedWith(Entity entity) {

    game.removeEntity(this);

  }

}
