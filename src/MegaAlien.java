/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package galaga;

/**
 *
 * @author Khang Quach and Edward Piper
 */
public class MegaAlien extends AlienEntity {
  
  private double moveSpeed = 75;
	/** The game in which the entity exists */
	private Game game;
  
  public MegaAlien(Game game,String ref,int x,int y) {
		super(game,ref,x,y);
    life = 5;

		this.game = game;
		dx = -moveSpeed;
	}
}
  
 
 