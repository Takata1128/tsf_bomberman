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
  private int item;
  private Thread threadAnime;
  private Map map;
  private GamePanel panel;
  private KeyController controller;
  private BombManager bm;
  private NetworkManager network;
  public boolean isLive = true;
  public boolean is_1p = true;

  public Player(int x, int y, boolean is_1p,String filename, Map map, GamePanel panel, KeyController kc, BombManager bm) {
    this.x = x;
    this.y = y;
    this.is_1p = is_1p;
    direction = 1;
    count = 0;
    this.map = map;
    this.panel = panel;
    this.bm = bm;
    controller = kc;
    controller.addObserver(this);
    loadImage(filename);
    threadAnime = new Thread(new AnimationThread());
    threadAnime.start();
  }

  public Player(int x, int y, String filename, Map map, GamePanel panel, KeyController kc, NetworkManager network,
      BombManager bm) {
    this.x = x;
    this.y = y;
    direction = 1;
    count = 0;
    this.map = map;
    this.panel = panel;
    this.network = network;
    this.bm = bm;
    controller = kc;
    controller.addObserver(this);
    loadImage(filename);
    threadAnime = new Thread(new AnimationThread());
    threadAnime.start();
  }

  private void loadImage(String filename) {
    ImageIcon icon = new ImageIcon(getClass().getResource(filename));
    image = icon.getImage();
  }

  public void update(Observable o, Object arg) {
    int dir = controller.getState();
    switch (dir) {
    case LEFT:
      if (is_1p) {
        if (!map.isHit(x - 1, y))
          x--;
        direction = LEFT;
      }
      break;
    case RIGHT:
      if (is_1p) {
        if (!map.isHit(x + 1, y))
          x++;
        direction = RIGHT;
      }
      break;
    case UP:
      if (is_1p) {
        if (!map.isHit(x, y - 1))
          y--;
        direction = UP;
      }
      break;
    case DOWN:
      if (is_1p) {
        if (!map.isHit(x, y + 1))
          y++;
        direction = DOWN;
      }
      break;
    case BOMB:
    if (network!=null&&bm.isset()) {
      network.send(new NetworkBomb(x, y, bm.bombPow, bm.pane));
    }
    if(is_1p)
      bm.set(x, y);
    break;
  }
<<<<<<< HEAD
      if (is_1p)
        bm.set(x, y);
      break;
    case P2_LEFT:
      if (!is_1p) {
        if (!map.isHit(x - 1, y))
          x--;
        direction = LEFT;
      }
      break;
    case P2_RIGHT:
      if (!is_1p) {
        if (!map.isHit(x + 1, y))
          x++;
        direction = RIGHT;
      }
      break;
    case P2_UP:
      if (!is_1p) {
        if (!map.isHit(x, y - 1))
          y--;
        direction = UP;
      }
      break;
    case P2_DOWN:
      if (!is_1p) {
        if (!map.isHit(x, y + 1))
          y++;
        direction = DOWN;
      }
      break;
    case P2_BOMB:
      if (!is_1p)
        bm.set(x, y);
      break;
    }
    if (network != null) {
      if (dir == LEFT || dir == RIGHT || dir == UP || dir == DOWN) {
        network.send(getPlayer());
      } 
    }
    panel.repaint();
  }

  public void draw(Graphics g) {
    // テスト用 アイテムについて
    item = map.getItem(x, y);
    if (item == 1 || item == -1) {
      bm.setPow(item);
    } else if (item == 2 || item == -2) {
      bm.addMax(item);
    } else if (item == 3 || item == -3) {
      bm.isPanatrate(item);
    }
    if (isLive == true) {
      if (map.effHit(x, y) == true) {
        isLive = false;
        if(network!=null){
          NetworkWin nwin = new NetworkWin();
          network.send(nwin);
          network.close();
        }
      }
      bm.draw(g);
      g.drawImage(image, x * CS, y * CS, x * CS + CS, y * CS + CS, count * CS, direction * CS, count * CS + CS,
          direction * CS + CS, panel);
    }
  }

  public NetworkPlayer getPlayer() {
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
