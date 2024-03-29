import java.awt.*;

class SingleGamePanel extends GamePanel implements Runnable, Common{
  private Map map;
  private Player p1,p2;
  private KeyController controller;
  private Thread gameLoop;
  private MainPanel mp;
  private BombManager bm1,bm2;

  public SingleGamePanel(MainPanel mp) {
    super(mp);
    this.mp = mp;
    controller = new KeyController();// キーコントローラー生成
    mp.network=null;//networkをnullに
    // キー入力受付
    setFocusable(true);
    addKeyListener(controller);
    //マップと爆弾、プレイヤー生成
    map = new Map(this);
    bm1 = new BombManager("image/bomb.png", "image/eff.png", map, this);
    bm2 = new BombManager("image/bomb.png", "image/eff.png", map, this);
    p1 = new Player(1, 1, true,"image/BMW.png", map, this, controller, bm1);// プレイヤー生成
    p2 = new Player(map.col-2, map.row-2, false, "image/BMR.png", map, this, controller, bm2);// プレイヤー生成
    
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
          mp.setstate(RESULT_WIN);
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
