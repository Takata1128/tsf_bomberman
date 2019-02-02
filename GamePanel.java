import java.awt.*;
import javax.swing.*;

class GamePanel extends JPanel implements Common{
  private Map map;
  private Player p1,p2;
  private KeyController controller;
  private Thread gameLoop;
  private MainPanel mp;
  private BombManager bombManager;
  public double block_cfg; 

  public GamePanel(MainPanel mp) {
    this.mp = mp;
    controller = new KeyController();// キーコントローラー生成
    this.block_cfg = mp.block_cfg;
    // キー入力受付
    setFocusable(true);
    addKeyListener(controller);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

}
