import java.util.Observable;
import java.util.Observer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class KeyController extends Observable implements KeyListener,Common{
  private int keyState;

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
        case KeyEvent.VK_ENTER:
	        keyState = BOMB;
          break;
        case KeyEvent.VK_A:
          keyState = P2_LEFT;
          break;
        case KeyEvent.VK_D:
          keyState = P2_RIGHT;
          break;
        case KeyEvent.VK_W:
          keyState = P2_UP;
          break;
        case KeyEvent.VK_S:
          keyState = P2_DOWN;
          break;
        case KeyEvent.VK_B:
	        keyState = P2_BOMB;
          break;
      }
      setChanged();
      notifyObservers();
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
}
