/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package galaga;

/**
 *
 * @author Khang Quach and Edward piper
 * */
public class NormalAlien extends AlienEntity{
  
  private double moveSpeed = 75;
	/** The game in which the entity exists */
	private Game game;
  
  public NormalAlien(Game game,String ref,int x,int y) {
		super(game,ref,x,y);
		life = 1;


		this.game = game;
	}
  
}
