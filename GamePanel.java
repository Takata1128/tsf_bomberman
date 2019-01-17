import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class GamePanel extends JPanel implements Runnable, Common, NetworkCallback{
  private Map map;
  private Player p1;
  private Bomb bm;
  private KeyController controller;
  private Thread gameLoop;
  private MainPanel mp;
  private BombManager bombManager;
  private NetworkManager network;
  private Opponent opponent;
  private OpponentController opponentController;

  public GamePanel(MainPanel mp) {
    this.mp = mp;
    int px, py, ox, oy;
    controller = new KeyController();// キーコントローラー生成
    opponentController = new OpponentController();
    // キー入力受付
    setFocusable(true);
    addKeyListener(controller);

    //サーバーかクライアントかによってプレイヤーの生成位置を決定
    //TODO: NetworkManagerインスタンス化時に接続待ちが発生するので、その時のviewをどうするか考える
    if(mp.is_server){
      network = new NetworkManager(mp.is_server, this);
      map = new Map(this, network);
      network.send(map.getNetworkMap());
      px = py = 1;
      ox = oy = 13;
    }else{
      network = new NetworkManager(mp.is_server, this);
      map = new Map(this, network);
      px = py = 13;
      ox = oy = 1;
    }

    bombManager = new BombManager("image/bomb.png", "image/eff.png", map, this);
    p1 = new Player(px, py, "image/BMW.png", map, this, controller, network, bombManager);// プレイヤー生成
    opponent = new Opponent(ox, oy, "image/BMW.png", map, this, opponentController);
    // ゲームループ開始
    gameLoop = new Thread(this);
    gameLoop.start();

    network.start();

    //情報受信ループ
    //new Thread(new NetworkThread()).start();
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
    bombManager.set(nbomb.x, nbomb.y);
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

  //相手からのデータを待ち受ける
  //受信した情報に応じて処理を分ける
  // private class NetworkThread extends Thread{
  //   public void run(){
  //     while(true){
  //       NetworkObject obj = network.recv();
  //       if (obj == null) {
  //         continue;
  //       } else if (obj instanceof NetworkPlayer) {
  //         System.out.println("===NetworkPlayer===");
  //         NetworkPlayer player = (NetworkPlayer) obj;
  //         System.out.println("x:" + player.x + " y:" + player.y + " dir:" + player.direction);
  //         opponentController.setState(player);
  //       } else if (obj instanceof NetworkBomb) {
  //         System.out.println("===NetworkBomb===");
  //         NetworkBomb nbomb = (NetworkBomb) obj;
  //         System.out.println(nbomb.x + " " + nbomb.y);
  //         bombManager.set(nbomb.x, nbomb.y);
  //       } else if (obj instanceof NetworkMap) {
  //         System.out.println("===NetworkMap===");
  //         NetworkMap nmap = (NetworkMap) obj;
  //         map.setNetworkMap(nmap);
  //         repaint();
  //       }
  //     }
  //   }
  // }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    // マップを描く
    map.draw(g);

    // プレイヤーを描く
    p1.draw(g);
    opponent.draw(g);
  }

  public void run() {
    while (true) {// ゲームループ
      repaint();
      if (!p1.isLive) {// 死んだらリザルトに
        mp.setstate(RESULT_SCENE);
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
