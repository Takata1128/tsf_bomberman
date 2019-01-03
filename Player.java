import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.Observable;
import java.util.Observer;

class Player implements Observer, Common {
  private Image image;
  private int x, y;
  private int direction;
  private int count;
  private Thread threadAnime;
  private Map map;
  private GamePanel panel;
  private KeyController Controller;
  private BombManager bm;
  private NetworkManager network;
  public boolean isLive = true;

  public Player(int x, int y, String filename, Map map, GamePanel panel, KeyController kc, NetworkManager network, BombManager bm) {
    this.x = x;
    this.y = y;
    direction = 1;
    count = 0;
    this.map = map;
    this.panel = panel;
    this.network = network;
    this.bm = bm;
    Controller = kc;
    Controller.addObserver(this);
    loadImage(filename);
    threadAnime = new Thread(new AnimationThread());
    threadAnime.start();
  }

  private void loadImage(String filename) {
    ImageIcon icon = new ImageIcon(getClass().getResource(filename));
    image = icon.getImage();
  }

  public void update(Observable o, Object arg) {
    int dir = Controller.getState();
    switch (dir) {
    case LEFT:
      if (!map.isHit(x - 1, y))
        x--;
      direction = LEFT;
      break;
    case RIGHT:
      if (!map.isHit(x + 1, y))
        x++;
      direction = RIGHT;
      break;
    case UP:
      if (!map.isHit(x, y - 1))
        y--;
      direction = UP;
      break;
    case DOWN:
      if (!map.isHit(x, y + 1))
        y++;
      direction = DOWN;
      break;
    case BOMB:
      bm.set(x, y);
      break;
    }
    if(dir == LEFT || dir == RIGHT || dir == UP || dir == DOWN){
      network.send(getPlayer());
    }else if(dir == BOMB){
      network.send(new NetworkBomb(x, y, 3));
    }
    panel.repaint();
  }

  public void draw(Graphics g) {
    if (isLive == true) {
      if (map.effHit(x, y) == true) {
        isLive = false;
      }
      bm.draw(g);
      g.drawImage(image, x * CS, y * CS, x * CS + CS, y * CS + CS, count * CS, direction * CS, count * CS + CS,
          direction * CS + CS, panel);
    }
  }

  public NetworkPlayer getPlayer(){
    return new NetworkPlayer(x, y, direction);
  }

  // アニメーションクラス
  private class AnimationThread extends Thread {
    public void run() {
      while (true) {
        // countを切り替える
        if (count == 0) {
          count = 1;
        } else if (count == 1) {
          count = 0;
        }

        panel.repaint();

        // 300ミリ秒休止＝300ミリ秒おきにキャラクターの絵を切り替える
        try {
          Thread.sleep(300);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
