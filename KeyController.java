import java.util.Observable;
import java.util.Observer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class KeyController extends Observable implements KeyListener,Common{
  private int keyState;

  public KeyController(){
    System.out.print("KC generated.");
  }

  public int getState(){return keyState;}

  public void keyPressed(KeyEvent e){
      int keyCode = e.getKeyCode();
      switch(keyCode){
        case KeyEvent.VK_LEFT:
          keyState = LEFT;
          break;
        case KeyEvent.VK_RIGHT:
          keyState = RIGHT;
          break;
        case KeyEvent.VK_UP:
          keyState = UP;
          break;
        case KeyEvent.VK_DOWN:
          keyState = DOWN;
          break;
        case KeyEvent.VK_B:
	    keyState = BOMB;
	    break;
      }
      setChanged();
      notifyObservers();
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}
