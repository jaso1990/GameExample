
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameScreen extends BasicGameState {
    
    private ArrayList <Asteroid> rocks = new ArrayList();
    TrueTypeFont ttf;
    java.awt.Font f;
    private int t;

    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        Asteroid.setGameSize(800,600);
        f = new java.awt.Font("Impact", 0, 55);
        ttf = new TrueTypeFont (f, true);
        
        for (int i = 0; i < 20; i++) {
            int xloc = (int)(Math.random() * 750);
            int yloc = (int)(Math.random() * 550);
            rocks.add(new Asteroid(xloc, yloc));
        }
        
        pickRock();
    }
    
    public void pickRock(){
        int randrock = (int) (Math.random() * rocks.size());
        rocks.get(randrock).setChosen();
    }

    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        t ++;
        if (t == 400){
            t = 0;
            if (rocks.size() > 0){
                int rx = (int)(Math.random() * 750 + 1);
                int ry = (int)(Math.random() * 550 + 1);
                rocks.add(new Asteroid(rx, ry));
            }
        }
        
        for (Asteroid a : rocks) 
            a.move();
        
        Input in = gc.getInput();
        if (in.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            //go thru all rocks and see if any hit
            int mx = in.getMouseX();
            int my = in.getMouseY();
            for (int j = 0; j < rocks.size(); j++) {
                Asteroid ast = rocks.get(j);
                if (ast.isHit(mx,my) && ast.isChosen()){
                    rocks.remove(j);
                    if (rocks.size() > 0)
                        pickRock();
                }
            }
        }
        if (rocks.size() == 0)
            sbg.enterState(2, new FadeOutTransition(), new FadeInTransition());
    }

    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
         for (Asteroid a: rocks) {
            a.draw();
        }
        g.drawString("Asteroids Left: " + rocks.size(), 10, 10);
        if (rocks.size() == 0)
            ttf.drawString(250, 200, "GAME OVER", Color.yellow);
    }

    public int getID() {
        return 1;  //this id will be different for each screen
    }

}
