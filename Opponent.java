import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.Observable;
import java.util.Observer;

class Opponent implements Observer, Common {
  private Image image;
  private int x, y;
  private int direction;
  private int count;
  private Thread threadAnime;
  private Map map;
  private MainPanel panel;
  private OpponentController Controller;

  public Opponent(int x, int y, String filename, Map map, MainPanel panel, OpponentController controller) {
    this.x = x;
    this.y = y;

    direction = 1;
    count = 0;
    this.map = map;
    this.panel = panel;
    Controller = controller;
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
    PlayerXY player = (PlayerXY)arg;
    System.out.println("direction: " + player.direction);
    x = player.x;
    y = player.y;
    direction = player.direction;
    panel.repaint();
  }

  public void draw(Graphics g) {
    g.drawImage(image, x * CS, y * CS, x * CS + CS, y * CS + CS, count * CS, direction * CS, count * CS + CS,
        direction * CS + CS, panel);
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
