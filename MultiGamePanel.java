import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class MultiGamePanel extends GamePanel implements Runnable, Common, NetworkCallback{
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

  public MultiGamePanel(MainPanel mp) {
    super(mp);
    this.mp = mp;
    controller = new KeyController();
     setFocusable(true);
    addKeyListener(controller);
    int px, py, ox, oy;
    opponentController = new OpponentController();
    if(mp.is_server){
      network = new NetworkManager(mp.is_server, this);
      map = new Map(this, network);
      network.send(map.getNetworkMap());
      px = py = 1;
      ox = oy = map.col-2;
    }else{
      network = new NetworkManager(mp.is_server, this);
      map = new Map(this, network);
      px = py = map.row-2;
      ox = oy = 1;
    }
    bombManager = new BombManager("image/bomb.png", "image/eff.png", map, this);
    p1 = new Player(px, py, "image/BMW.png", map, this, controller, network, bombManager);
    opponent = new Opponent(ox, oy, "image/BMR.png", map, this, opponentController);
    gameLoop = new Thread(this);
    gameLoop.start();

    network.start();
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
