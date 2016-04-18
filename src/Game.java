//package galaga;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main hook of our game. This class with both act as a manager for the
 * display and central mediator for the game logic.
 *
 * Display management will consist of a loop that cycles round all entities in
 * the game asking them to move and then drawing them in the appropriate place.
 * With the help of an inner class it will also allow the player to control the
 * main ship.
 *
 * As a mediator it will be informed when entities within our game detect events
 * (e.g. alient killed, played died) and will take appropriate game actions.
 *
 * @author Khang Quach and Edward Piper
 */
public class Game extends Canvas {

  /**
   * The strategy that allows us to use accelerate page flipping
   */
  private BufferStrategy strategy;
  /**
   * True if the game is currently "running", i.e. the game loop is looping
   */
  private boolean gameRunning = true;
  /**
   * The list of all the entities that exist in our game
   */
  private ArrayList entities = new ArrayList();
  private ArrayList<AlienEntity> aliensEntities = new ArrayList();
  /**
   * The list of entities that need to be removed from the game this loop
   */
  private ArrayList removeList = new ArrayList();
  /**
   * The entity representing the player
   */
  private Entity ship;
  private Entity ship2;//kenny's ship2
  /**
   * The times the player has fired
   */
  private int shotsFired = 0;

  private int shotsFired2 = 0;
  /**
   * The speed at which the player's ship should move (pixels/sec)
   */
  private double moveSpeed = 300;
  /**
   * The time at which last fired a shot
   */
  private long lastFire = 0;
  //CHANGE - time which shots fired for ship2-KENNY
  private long lastFire2 = 0;
  /**
   * The interval between our players shot (ms)
   */
  private long firingInterval = 250;
  /**
   * The number of aliens left on the screen
   */
  private int alienCount;
  private int aliensKilled = 0;

  //CHANGE- ship count for 2 player shootout-KENNY
  private int shipCount;
  private int ship2Count;
  int turretCountp1 = 3;
  int turretCountp2 = 3;

  /**
   *
   * The message to display which waiting for a key press
   */
  private String message = "";
  /**
   * True if we're holding up game play until a key has been pressed
   */
  private boolean waitingForKeyPress = true;
  /**
   * True if the left cursor key is currently pressed
   */
  private boolean leftPressed = false;
  /**
   * True if the right cursor key is currently pressed
   */
  private boolean rightPressed = false;
  /**
   * True if the up cursor key is currently pressed
   */
  private boolean upPressed = false;
  /**
   * True if the down cursor key is currently pressed
   */
  private boolean downPressed = false;
  /**
   * True if we are firing
   */
  private boolean firePressed = false;
  private boolean pressed2 = false;
  private boolean shiftPressed = false;

  //CHANGE BUTTONS FOR PLAYER 2-KENNY
  private boolean leftPressedA = false;
  /**
   * True if the right cursor key is currently pressed for ship2
   */
  private boolean rightPressedD = false;
  /**
   * True if the up cursor key is currently pressed-ship2
   */
  private boolean upPressedW = false;
  /**
   * True if the down cursor key is currently pressed-ship2
   */
  private boolean downPressedS = false;
  /**
   * True if we are firing-ship2
   */
  private boolean firePressed1 = false;
  /**
   * True if game logic needs to be applied this loop, normally as a result of a
   * game event
   */
  private boolean logicRequiredThisLoop = false;
  public double turretCreated;

  /**
   * Create a game and make it run
   */
  public Game() {
    // create a frame to contain our game
    JFrame container = new JFrame("Space Invaders 101");
    // JPanel mainPanel = new JPanel();
    // JPanel menuPanel = new JPanel();
    JPanel panel = (JPanel) container.getContentPane();

    JPanel panel2 = new JPanel();
    panel2.setPreferredSize(new Dimension(800, 600));
    panel2.setBackground(Color.GREEN);
    container.add(panel2, BorderLayout.EAST);

    // mainPanel.setLayout(new BorderLayout());
    //mainPanel.setPreferredSize(new Dimension(300, 600));
    //menuPanel.setBackground(Color.RED);
    //add panel to main panel in the North part of the panel.    
    panel.add(this);
    panel.setPreferredSize(new Dimension(1920, 1080));
    panel.setLayout(null);

    //mainPanel.add(panel, BorderLayout.NORTH);
    //mainPanel.add(menuPanel, BorderLayout.SOUTH);
    //set the size of panel
    // setup our canvas size and put it into the content of the frame
    setBounds(0, 0, 1920, 1080);

    // Tell AWT not to bother repainting our canvas since we're
    // going to do that our self in accelerated mode
    setIgnoreRepaint(true);

    // finally make the window visible 
    container.pack();
    container.setResizable(false);
    container.setVisible(true);
    //exit game if close is press.
    container.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    // add a listener to respond to the user closing the window. If they
    // do we'd like to exit the game
    addKeyListener(new KeyInputHandler());

    requestFocus();

    // to a 3 to do triple buffering
    createBufferStrategy(3);
    strategy = getBufferStrategy();

    initEntities();

  }

  /**
   * Start a fresh game, this should clear out any old data and create a new
   * set.
   */
  private void startGame() {
    // clear out any existing entities and intialise a new set
    entities.clear();
    initEntities();

    // blank out any keyboard settings we might currently have
    leftPressed = false;
    rightPressed = false;
    firePressed = false;
    upPressed = false;
    downPressed = false;
    turretCountp1 = 3;
    turretCountp2 = 3;
    //CHANGE FOR SHIP 2-KENNY
    leftPressedA = false;
    rightPressedD = false;
    firePressed1 = false;
    upPressedW = false;
    downPressedS = false;
  }

  /**
   * Initialise the starting state of the entities (ship and aliens). Each
   * entitiy will be added to the overall list of entities in the game.
   */
  private void initEntities() {
    // create the player ship and place it roughly in the center of the screen
    ship = new ShipEntity(this, "sprites/ship.gif", 370, 550);
    shipCount = 1;
    entities.add(ship);

    //CHANGE ADDED SHIP 2-KENNY
    ship2 = new ShipEntity(this, "sprites/ship2.gif", 370, 0);
    ship2Count = 1;
    entities.add(ship2);

    // create a block of aliens (5 rows, by 12 aliens, spaced evenly)
    alienCount = 0;
    aliensKilled = 0;
    int random;
    for (int row = 0; row < 5; row++) {
      for (int x = 0; x < 12; x++) {
        random = (int) Math.floor(Math.random() * 101);
        //adds a random super alien which has two life.
        if (random > 80) {//adjusted y to 150 from 50-kenny
          Entity MegaAlien = new MegaAlien(this, "sprites/superAlien.gif", 100 + (x * 50), (150) + row * 30);
          entities.add(MegaAlien);

          alienCount++;
        } else {                  //also adjusted y to 150 from 50
          Entity alien = new NormalAlien(this, "sprites/alien.gif", 100 + (x * 50), (150) + row * 30);
          entities.add(alien);
          alienCount++;
        }
      }
    }
  }

  /**
   * Notification from a game entity that the logic of the game should be run at
   * the next opportunity (normally as a result of some game event)
   */
  public void updateLogic() {
    logicRequiredThisLoop = true;
  }

  /**
   * Remove an entity from the game. The entity removed will no longer move or
   * be drawn.
   *
   * @param entity The entity that should be removed
   */
  public void removeEntity(Entity entity) {

    removeList.add(entity);
  }

  /**
   * Notification that the player has died.
   */
  public void notifyDeath() {
    message = "Oh no! They got you, try again?";
    waitingForKeyPress = true;
  }

  /**
   * Notification that the player has won since all the aliens are dead.
   */
  public void notifyWin() {
    message = "Well done! You Win!";
    waitingForKeyPress = true;
  }

  /**
   * Notification that an alien has been killed
   */
  public void notifyAlienKilled() {
    // reduce the alient count, if there are none left, the player has won!
    alienCount--;
    aliensKilled++;

    if (alienCount == 0) {//changed from notify win to new wave-kenny
      alienCount = 0;
      aliensKilled = 0;
      int random;
      for (int row = 0; row < 5; row++) {
        for (int x = 0; x < 12; x++) {
          random = (int) Math.floor(Math.random() * 101);
          //adds a random super alien which has two life.
          if (random > 80) {//adjusted y to 150 from 50-kenny
            Entity MegaAlien = new MegaAlien(this, "sprites/superAlien.gif", 100 + (x * 50), (150) + row * 30);
            entities.add(MegaAlien);

            alienCount++;
          } else {                  //also adjusted y to 150 from 50
            Entity alien = new NormalAlien(this, "sprites/alien.gif", 100 + (x * 50), (150) + row * 30);
            entities.add(alien);
            alienCount++;
          }
        }
      }
    }

    // if there are still some aliens left then they all need to get faster, so
    // speed up all the existing aliens
    for (int i = 0; i < entities.size(); i++) {
      Entity entity = (Entity) entities.get(i);

      if (entity instanceof AlienEntity) {
        // speed up by 2%
        entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
      }
    }
  }

  //CHANGE PLAYER 2 SHOOTOUT MODE-KENNY
  public void notyifyShipDestroyed() {
    shipCount--;
    ship2Count--;
    //NOTIFY WIN WHEN SHIP IS DESTROYED
    if (shipCount == 0 || ship2Count == 0) {
      notifyWin();
    }
  }

  /**
   * Attempt to fire a shot from the player. Its called "try" since we must
   * first check that the player can fire at this point, i.e. has he/she waited
   * long enough between shots
   */
  public void tryToFire(Entity ship, int direction, long lastFire) {
    // check that we have waiting long enough to fire 

    if (System.currentTimeMillis() - ship.lastFire < firingInterval) {

      return;

    }
    // if we waited long enough, create the shot entity, and record the time.
    ship.lastFire = System.currentTimeMillis();
    shotsFired = shotsFired + 2;

    if (direction < 0) {
      ShotEntity shot = new ShotEntity(this, "sprites/shot.gif", ship.getX() + 10, ship.getY() + 50, direction);
      ShotEntity shot2 = new ShotEntity(this, "sprites/shot.gif", ship.getX() + -10, ship.getY() + 50, direction);
      entities.add(shot);
      entities.add(shot2);
    } else {

      ShotEntity shot = new ShotEntity(this, "sprites/shot.gif", ship.getX() + 10, ship.getY() - 30, direction);
      ShotEntity shot2 = new ShotEntity(this, "sprites/shot.gif", ship.getX() + 50, ship.getY() - 40, direction);
      entities.add(shot);
      entities.add(shot2);
    }
  }

  /**
   * deploys a turret, and checks if it is facing up or down. incriments the
   * turret count of the respected player
   *
   * @param ship is the ship you are deploying the turret from
   * @param direction is the direction the ship is facing
   * @return returns the turret entity
   */
  private Entity deployTurret(Entity ship, int direction) {
    if (System.currentTimeMillis() - ship.lastFire < firingInterval) {

      return null;

    }
    ship.lastFire = System.currentTimeMillis();

    
      if (direction > 0) {
        turretCountp1--;

      } else {
        turretCountp2--;
      }
      turretCreated = System.currentTimeMillis();
      TurretEntity turret = new TurretEntity(this, "sprites/turret.gif", ship.getX() + 50, ship.getY(), direction);
      entities.add(turret);
      return turret;
    

  }
  /**
   * responsible for creating menu of p1 and p2.
   * @param g 
   */
  private void buildMenu(Graphics2D g){
     g.setColor(Color.white);
      g.drawString("HUD P1:", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 450);
      g.drawString("P1 Life: " + ship.life + "", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 500);

      g.drawString("P1 Shots fired: " + shotsFired + "[press space]", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 550);
      g.drawString("P1 Turrets Left: " + turretCountp1 + "[press shift]", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 600);

      g.drawString("[Aliens hit: " + aliensKilled + "]", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 350);
      //CHANGE SHOTS FIRED FOR PLAYER 2-KENNY

      g.drawString("HUD P2:", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 100);

      g.drawString("P2 Life: " + ship2.life + "", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 150);
      g.drawString("P2 Shots fired: " + shotsFired2 + "[press 1]", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 200);
      g.drawString("P2 Turrets Left: " + turretCountp2 + "press 2]", (1700 - g.getFontMetrics().stringWidth(message)) / 2, 250);
  }

  /**
   * The main game loop. This loop is running during all game play as is
   * responsible for the following activities:
   * <p>
   * - Working out the speed of the game loop to update moves - Moving the game
   * entities - Drawing the screen contents (entities, text) - Updating game
   * events - Checking Input
   * <p>
   */
  public void gameLoop() {
    long lastLoopTime = System.currentTimeMillis();

    // keep looping round til the game ends
    while (gameRunning) {
      // work out how long its been since the last update, this
      // will be used to calculate how far the entities should
      // move this loop
      long delta = System.currentTimeMillis() - lastLoopTime;
      lastLoopTime = System.currentTimeMillis();

      // Get hold of a graphics context for the accelerated 
      // surface and blank it out  
      Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

      //creates a background
      g.setColor(Color.black);
      g.fillRect(0, 0, 1920, 1080);

      // cycle round asking each entity to move itself
      if (!waitingForKeyPress) {
        for (int i = 0; i < entities.size(); i++) {
          Entity entity = (Entity) entities.get(i);

          entity.move(delta);
        }
      }

      // cycle round drawing all the entities we have in the game
      for (int i = 0; i < entities.size(); i++) {
        Entity entity = (Entity) entities.get(i);

        entity.draw(g);
      }
      // brute force collisions, compare every entity against
      // every other entity. If any of them collide notify 
      // both entities that the collision has occured

      for (int p = 0; p < entities.size(); p++) {
        for (int s = p + 1; s < entities.size(); s++) {
          Entity me = (Entity) entities.get(p);
          Entity him = (Entity) entities.get(s);
          if (me.collidesWith(him)) {
            me.collidedWith(him);
            him.collidedWith(me);
          }
        }
      }

      //brute force checks for turrets, and makes them shoot
      for (int p = 0; p < entities.size(); p++) {
        Entity turret = (Entity) entities.get(p);
        if (turret instanceof TurretEntity && (turret.age > 0)) {
          tryToFire(turret, turret.direction, turret.lastFire);
          turret.age--;
        }
      }

      // remove any entity that has been marked for clear up
      entities.removeAll(removeList);
      removeList.clear();

      // if a game event has indicated that game logic should
      // be resolved, cycle round every entity requesting that
      // their personal logic should be considered.
      if (logicRequiredThisLoop) {
        for (int i = 0; i < entities.size(); i++) {
          Entity entity = (Entity) entities.get(i);
          entity.doLogic();
        }

        logicRequiredThisLoop = false;
      }
      //Draws the hud. Displays shots fired and aliens hit
      buildMenu(g);

      // if we're waiting for an "any key" press then draw the 
      // current message 
      if (waitingForKeyPress) {
        g.setColor(Color.white);
        g.drawString(message, (800 - g.getFontMetrics().stringWidth(message)) / 2, 250);
        g.drawString("Press any key", (800 - g.getFontMetrics().stringWidth("Press any key")) / 2, 300);
      }

      // finally, we've completed drawing so clear up the graphics
      // and flip the buffer over
      g.dispose();
      strategy.show();

      // resolve the movement of the ship. First assume the ship 
      // isn't moving. If either cursor key is pressed then
      // update the movement appropraitely
      ship.setHorizontalMovement(0);
      ship.setVerticalMovement(0);

      if ((leftPressed) && (!rightPressed)) {
        ship.setHorizontalMovement(-moveSpeed);
      } else if ((rightPressed) && (!leftPressed)) {
        ship.setHorizontalMovement(moveSpeed);
      }

      if ((upPressed) && (!downPressed)) {
        ship.setVerticalMovement(-moveSpeed);
      } else if ((downPressed) && (!upPressed)) {
        ship.setVerticalMovement(moveSpeed);
      }

      // if we're pressing fire, attempt to fire
      if (firePressed) {
        tryToFire(ship, 1, ship.lastFire);
      }
      //TURRET DEPLOYMENT ON SHIFT KEY PRESS, and there are turrets left
      if (shiftPressed && turretCountp1 > 0) {
        deployTurret(ship, 1);
      }
      //CHANGE CONTROLS AND MECHANISMS FOR SHIP2-KENNY
      ship2.setHorizontalMovement(0);
      ship2.setVerticalMovement(0);

      if ((leftPressedA) && (!rightPressedD)) {
        ship2.setHorizontalMovement(-moveSpeed);
      } else if ((rightPressedD) && (!leftPressedA)) {
        ship2.setHorizontalMovement(moveSpeed);
      }
      //CHANGE ADDED AND SUBTRACTED MOVESPEED INSTEAD FOR OPPOSITE SCREEN-KENNY
      if ((upPressedW) && (!downPressedS)) {
        System.out.println("weeeeeeeee");
        ship2.setVerticalMovement(-moveSpeed);
      } else if ((downPressedS) && (!upPressedW)) {
        ship2.setVerticalMovement(moveSpeed);
      }

      // if we're pressing fire, attempt to fire
      if (firePressed1) {
        tryToFire(ship2, -1, ship2.lastFire);//CHANGE TO tryToFire1
      }
      // if Player 2 is pressing two, and has adequate turrets, deploy turret
      if (pressed2 && turretCountp2 > 0) {
        deployTurret(ship2, -1);
        System.out.println(turretCountp2);
      }

      // finally pause for a bit. Note: this should run us at about
      // 100 fps but on windows this might vary each loop due to
      // a bad implementation of timer
      try {
        Thread.sleep(10);
      } catch (Exception e) {
      }
    }
  }

  /**
   * A class to handle keyboard input from the user. The class handles both
   * dynamic input during game play, i.e. left/right and shoot, and more static
   * type input (i.e. press any key to continue)
   *
   * This has been implemented as an inner class more through habbit then
   * anything else. Its perfectly normal to implement this as seperate class if
   * slight less convienient.
   *
   * @author Kevin Glass
   */
  private class KeyInputHandler extends KeyAdapter {

    /**
     * The number of key presses we've had while waiting for an "any key" press
     */
    private int pressCount = 1;

    /**
     * Notification from AWT that a key has been pressed. Note that a key being
     * pressed is equal to being pushed down but *NOT* released. Thats where
     * keyTyped() comes in.
     *
     * @param e The details of the key that was pressed
     */
    public void keyPressed(KeyEvent e) {
      // if we're waiting for an "any key" typed then we don't 
      // want to do anything with just a "press"
      if (waitingForKeyPress) {
        return;
      }
      if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
        shiftPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        upPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        downPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        leftPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        rightPressed = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        firePressed = true;
      }

      //CHANGE-CONTROLS FOR PLAYER 2-KENNY
      if (e.getKeyCode() == KeyEvent.VK_2) {
        pressed2 = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_W) {
        upPressedW = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_S) {
        downPressedS = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_A) {
        leftPressedA = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_D) {
        rightPressedD = true;
      }
      if (e.getKeyCode() == KeyEvent.VK_1) {
        firePressed1 = true;
      }
    }

    /**
     * Notification from AWT that a key has been released.
     *
     * @param e The details of the key that was released
     */
    public void keyReleased(KeyEvent e) {
      // if we're waiting for an "any key" typed then we don't 
      // want to do anything with just a "released"
      if (waitingForKeyPress) {
        return;
      }

      if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
        shiftPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_UP) {
        upPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_DOWN) {
        downPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        leftPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        rightPressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        firePressed = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_2) {
        pressed2 = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_W) {
        upPressedW = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_S) {
        downPressedS = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_A) {
        leftPressedA = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_D) {
        rightPressedD = false;
      }
      if (e.getKeyCode() == KeyEvent.VK_1) {
        firePressed1 = false;
      }
    }

    /**
     * Notification from AWT that a key has been typed. Note that typing a key
     * means to both press and then release it.
     *
     * @param e The details of the key that was typed.
     */
    public void keyTyped(KeyEvent e) {
      // if we're waiting for a "any key" type then
      // check if we've recieved any recently. We may
      // have had a keyType() event from the user releasing
      // the shoot or move keys, hence the use of the "pressCount"
      // counter.
      if (waitingForKeyPress) {
        if (pressCount == 1) {
          // since we've now recieved our key typed
          // event we can mark it as such and start 
          // our new game
          waitingForKeyPress = false;
          startGame();
          pressCount = 0;
        } else {
          pressCount++;
        }
      }

      // if we hit escape, then quit the game
      if (e.getKeyChar() == 27) {
        System.exit(0);
      }
    }
  }

  /**
   * The entry point into the game. We'll simply create an instance of class
   * which will start the display and game loop.
   *
   * @param argv The arguments that are passed into our game
   */
  public static void main(String args[]) {
    //Game g =new Game();
    Game g = new Game();

    // Start the main game loop, note: this method will not
    // return until the game has finished running. Hence we are
    // using the actual main thread to run the game.
    g.gameLoop();

  }
}
