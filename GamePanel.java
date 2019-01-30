import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class GamePanel extends JPanel implements Common{
  private Map map;
  private Player p1,p2;
  private Bomb bm;
  private KeyController controller;
  private Thread gameLoop;
  private MainPanel mp;
  private BombManager bombManager;

  public GamePanel(MainPanel mp) {
    this.mp = mp;
    controller = new KeyController();// キーコントローラー生成
    // キー入力受付
    setFocusable(true);
    addKeyListener(controller);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

}
