import java.awt.*;

class MultiGamePanel extends GamePanel implements Runnable, Common, NetworkCallback{
  private Map map;
  private Player p1;
  private KeyController controller;
  private Thread gameLoop;
  private MainPanel mp;
  private BombManager bombManager;
  private NetworkManager network;
  private Opponent opponent;
  private OpponentController opponentController;

  public MultiGamePanel(MainPanel mp) {
    super(mp);
    this.mp = mp;
    //キー入力受付
    controller = new KeyController();
    setFocusable(true);
    addKeyListener(controller);
    //サーバー側がマップを生成して通信
    int px, py, ox, oy;
    opponentController = new OpponentController();
    if(mp.is_server){
      network = mp.network;
      network.setCallback(this);
      map = new Map(this, network);
      network.send(map.getNetworkMap());
      px = py = 1;
      ox = map.row-2;
      oy = map.col-2;
    }else{
      network = mp.network;
      network.setCallback(this);
      map = new Map(this, network);
      px = map.row-2;
      py = map.col-2;
      ox = oy = 1;
    }
    //爆弾とプレイヤー生成
    bombManager = new BombManager("image/bomb.png", "image/eff.png", map, this);
    p1 = new Player(px, py, "image/BMW.png", map, this, controller, network, bombManager);
    opponent = new Opponent(ox, oy, "image/BMR.png", map, this, opponentController);
    //ゲームループ開始
    gameLoop = new Thread(this);
    gameLoop.start();
    //通信開始
    network.start();

    if(mp.is_server){
      network.send(map.getNetworkMap());
    }
  }

  public void playerCallback(NetworkPlayer player){
    opponentController.setState(player);
    for (var item : map.im.item) {
      if(item != null){
        item.printItem();
      }
    }
    for(var item : map.im.itemEff){
      System.out.println(item);
    }
  }

  public void bombCallback(NetworkBomb nbomb){
    bombManager.set2(nbomb.x, nbomb.y, nbomb.power, nbomb.is_pane());
  }

  public void mapCallback(NetworkMap nmap){
    map.setNetworkMap(nmap);
    repaint();
  }

  public void itemCallback(NetworkItem nitem){
    System.out.println(nitem.num);
    map.im.num = nitem.num;
    map.im.setMax = nitem.num;
    map.im.reset(nitem);
  }

  public void winCallback(NetworkWin nwin){
    mp.setstate(RESULT_WIN);
    network.close();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    map.draw(g);
    p1.draw(g);
    opponent.draw(g);
  }

  public void run() {
    while (true) {
      repaint();
      if (!p1.isLive) {
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
