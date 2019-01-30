import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class SingleGamePanel extends GamePanel implements Runnable, Common{
  private Map map;
  private Player p1,p2;
  private Bomb bm;
  private KeyController controller;
  private Thread gameLoop;
  private MainPanel mp;
  private BombManager bombManager;

  public SingleGamePanel(MainPanel mp) {
    super(mp);
    this.mp = mp;
    controller = new KeyController();// キーコントローラー生成
    // キー入力受付
    setFocusable(true);
    addKeyListener(controller);
    //サーバーかクライアントかによってプレイヤーの生成位置を決定
    map = new Map(this);
    bombManager = new BombManager("image/bomb.png", "image/eff.png", map, this);
    p1 = new Player(1, 1, true,"image/BMW.png", map, this, controller, bombManager);// プレイヤー生成
    p2 = new Player(map.col-2, map.row-2, false, "image/BMR.png", map, this, controller, bombManager);// プレイヤー生成
    
    // ゲームループ開始
    gameLoop = new Thread(this);
    gameLoop.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // マップを描く
    map.draw(g);

    // プレイヤーを描く
    p1.draw(g);
    p2.draw(g);
  }

  public void run() {
    while (true) {// ゲームループ
      repaint();
      if (!p1.isLive) {// 死んだらリザルトに
        mp.setstate(RESULT_LOSE);
        break;
      }
      if(!p2.isLive){
          mp.setstate(RESULT_LOSE);
          break;
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
