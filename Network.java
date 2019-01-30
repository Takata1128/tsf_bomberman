//[責務] Commライブラリのラ(ClientとServerの差異吸収)
interface CommWrap {
  void send(String msg);
  String recv();
  boolean connect();
  void close();
}

// [責務] Commライブラリのclient実装
class GameClient implements CommWrap {
  private CommClient cl = null;
  private String host;
  private int port;

  GameClient() {
    cl = new CommClient("localhost", 1025);
    cl.setTimeout(1); // non-wait で通信
  }

  GameClient(String host, int port){
    cl = new CommClient();
    this.host = host;
    this.port = port;
  }

  public boolean connect(){
    boolean is_connect = cl.open(host, port);
    if(is_connect){
      cl.setTimeout(1);
    }
    return is_connect;
  }

  @Override
  public void send(String msg) {
    cl.send(msg);
  }

  @Override
  public String recv() {
    String msg = cl.recv();
    if (msg == null)
      return null;
    return msg;
  }
  @Override
  public void close() {
    cl.close();
  }
}

// [責務] Commライブラリのserver実装
class GameServer implements CommWrap {
  private CommServer sv = null;
  int port;

  GameServer() {
    sv = new CommServer(1025);
    sv.setTimeout(1); // non-wait で通信
    System.out.println("Connected !");
  }

  GameServer(int port) {
    sv = new CommServer();
    this.port = port;
  }

  @Override
  public boolean connect() {
    boolean is_connect = sv.open(port);
    if(is_connect){
      sv.setTimeout(1);
    }
    return is_connect;
  }

  @Override
  public void send(String msg) {
    sv.send(msg);
  }

  @Override
  public String recv() {
    String msg;
    msg = sv.recv();
    if (msg == null)
      return null;
    return msg;
  }

  @Override
  public void close() {
    sv.close();
  }
}

// [責務] データの送受信に必要なメソッドを定義(これを実装してプロトコルを定義する)
interface NetworkObject {
  public int getId();

  public void toRecvFormat(String data);

  public String toSendFormat();
}

// [責務] プレイヤーのデータの送受信プロトコルを定義
class NetworkPlayer implements NetworkObject {
  int x, y, direction, id;

  NetworkPlayer(int x, int y, int direction) {
    this.id = 1;
    this.x = x;
    this.y = y;
    this.direction = direction;
  }

  NetworkPlayer() {
    this.id = 1;
  }

  public String toSendFormat() {
    String string_data = String.format("%d %d %d", x, y, direction);
    return string_data;
  }

  // idなを引数に取る
  public void toRecvFormat(String data) {
    if (data == null)
      return;
    String[] xy = data.split(" ");
    this.x = Integer.parseInt(xy[0]);
    this.y = Integer.parseInt(xy[1]);
    this.direction = Integer.parseInt(xy[2]);
  }

  public int getId() {
    return this.id;
  }
}

// [責務] 爆弾の送受信に関するプロトコルを定義
class NetworkBomb implements NetworkObject {
  int x, y, power, id, pane;

  NetworkBomb(int x, int y, int power, boolean pane) {
    this.id = 2;
    this.x = x;
    this.y = y;
    this.power = power;
    if (pane) {
      this.pane = 1;
    } else {
      this.pane = 0;
    }
  }

  NetworkBomb() {
    this.id = 2;
  }

  public String toSendFormat() {
    String string_data = String.format("%d %d %d %d", x, y, power, pane);
    return string_data;
  }

  public void toRecvFormat(String data) {
    if (data == null)
      return;
    String[] xy = data.split(" ");
    this.x = Integer.parseInt(xy[0]);
    this.y = Integer.parseInt(xy[1]);
    this.power = Integer.parseInt(xy[2]);
    this.pane = Integer.parseInt(xy[3]);
  }

  public int getId() {
    return this.id;
  }

  boolean is_pane() {
    if (pane == 1) {
      return true;
    } else {
      return false;
    }
  }
}

// [責務] マップデータの送受信プロトコルの定義
class NetworkMap implements NetworkObject {
  public int[][] map;

  private int Row = 15;
  private int Col = 15;
  private int id;

  NetworkMap(int[][] map) {
    this.id = 3;
    this.map = map.clone();
  }

  NetworkMap() {
    this.id = 3;
    this.map = new int[Row][Col];
  }

  public String toSendFormat() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < Row; i++) {
      for (int j = 0; j < Col; j++) {
        sb.append(String.format("%d ", map[i][j]));
      }
    }
    return sb.toString();
  }

  public void toRecvFormat(String data) {
    int count = 0;
    if (data == null)
      return;
    String[] map_data = data.split(" ");
    for (int i = 0; i < Row; i++) {
      for (int j = 0; j < Col; j++) {
        map[i][j] = Integer.parseInt(map_data[count++]);
      }
    }
  }

  public int getId() {
    return this.id;
  }
}

class NetworkItem implements NetworkObject {
  private int id = 4;
  public int x, y, num, img_num, itemEff;

  NetworkItem(int x, int y, int num, int img_num, int itemEff) {
    this.id = 4;
    this.x = x;
    this.y = y;
    this.num = num;
    this.img_num = img_num;
    this.itemEff = itemEff;
  }

  NetworkItem() {
    this.id = 4;
  }

  public String toSendFormat() {
    return String.format("%d %d %d %d %d", x, y, num, img_num, itemEff);
  }

  public void toRecvFormat(String data) {
    if (data == null)
      return;
    String[] xy = data.split(" ");
    this.x = Integer.parseInt(xy[0]);
    this.y = Integer.parseInt(xy[1]);
    this.num = Integer.parseInt(xy[2]);
    this.img_num = Integer.parseInt(xy[3]);
    this.itemEff = Integer.parseInt(xy[4]);
  }

  public int getId() {
    return id;
  }
}

class NetworkWin implements NetworkObject{
  int id = 5;
  int score = 100000; //ダミー
  int is_win = 1;     //ダミー

  NetworkWin(){
    this.id = 5;
  }

  public String toSendFormat() {
    return String.format("%d %d", score, is_win);
  }

  public void toRecvFormat(String data) {
    if (data == null)
      return;
    String[] xy = data.split(" ");
    this.score = Integer.parseInt(xy[0]);
    this.is_win = Integer.parseInt(xy[1]);
  }

  public int getId() {
    return id;
  }

}

// [責務] NetworkObjectの送受信
class NetworkManager {
  public boolean is_server;
  CommWrap network;
  NetworkCallback callback;

  NetworkManager(boolean is_server, NetworkCallback callback) {
    this.is_server = is_server;
    this.callback = callback;
    if (this.is_server) {
      network = new GameServer();
    } else {
      network = new GameClient();
    }
  }

  //Serverモードの場合
  NetworkManager(int port){
    network = new GameServer(port);
    is_server = true;
  }

  //Clientモードの場合
  NetworkManager(String host, int port){
    network = new GameClient(host, port);
    is_server = false;
  }

  public boolean connect(){
    return network.connect();
  } 

  public void setCallback(NetworkCallback callback){
    this.callback = callback;
  }

  // NetworkObjectを受け取って文字列に変換して送信
  void send(NetworkObject obj) {
    String id = String.valueOf(obj.getId());
    String msg = id + "," + obj.toSendFormat();
    if (msg != null) {
      System.out.println("[debug send]" + msg);
    }
    network.send(msg);
  }

  // 文字列を受け取ってidを見て、NetworkObjectに変換
  // msgはid,data1 data2 data3 ...となっておりidを見て適切なNetworkObjectを生成する
  NetworkObject recv() {
    String msg = network.recv();
    if (msg != null) {
      System.out.println(msg);
    }
    if (msg == null) {
      return null;
    }

    String[] msg_split = msg.split(",");
    int id = Integer.parseInt(msg_split[0]);
    String data = msg_split[1];

    System.out.println("[debug recv]" + data);
    NetworkObject obj = null;

    if (id == 1) {
      obj = new NetworkPlayer();
      obj.toRecvFormat(data);
    } else if (id == 2) {
      obj = new NetworkBomb();
      obj.toRecvFormat(data);
    } else if (id == 3) {
      obj = new NetworkMap();
      obj.toRecvFormat(data);
    } else if (id == 4) {
      obj = new NetworkItem();
      obj.toRecvFormat(data);
    } else if(id == 5){
      obj = new NetworkWin();
      obj.toRecvFormat(data);
    }
    return obj;
  }

  public void close(){
    network.close();
  }

  public void start() {
    new Thread(new NetworkThread()).start();
  }

  // 相手からのデータを待ち受ける
  // 受信した情報に応じて処理を分ける
  private class NetworkThread extends Thread {
    public void run() {
      while (true) {
        NetworkObject obj = recv();
        if (obj == null) {
          continue;
        } else if (obj instanceof NetworkPlayer) {
          NetworkPlayer player = (NetworkPlayer) obj;
          callback.playerCallback(player);
        } else if (obj instanceof NetworkBomb) {
          NetworkBomb nbomb = (NetworkBomb) obj;
          callback.bombCallback(nbomb);
        } else if (obj instanceof NetworkMap) {
          NetworkMap nmap = (NetworkMap) obj;
          callback.mapCallback(nmap);
        } else if (obj instanceof NetworkItem) {
          NetworkItem nitem = (NetworkItem) obj;
          callback.itemCallback(nitem);
        } else if (obj instanceof NetworkWin) {
          NetworkWin nwin = (NetworkWin) obj;
          callback.winCallback(nwin);
        }
      }
    }
  }
}

interface NetworkCallback {
  public void playerCallback(NetworkPlayer player);

  public void bombCallback(NetworkBomb bomb);

  public void mapCallback(NetworkMap map);

  public void itemCallback(NetworkItem item);

  public void winCallback(NetworkWin win);
}

// public class Network {
// public static void main(String[] arg) {
// boolean is_server = false;
// String recvMessage = "Server";
// String sendMessage = "Clinet";
// if(arg[0].equals("s")){
// is_server = true;
// recvMessage = "Client";
// sendMessage = "Server";
// }
// NetworkManager nm = new NetworkManager(is_server);
// int x = 0, y = 0;
// int bomb_x = 0, bomb_y = 0;
// for (int i = 0; i < 100; i++) {
// x += 1;
// y += 1;
// bomb_x *= x + y;
// bomb_y += bomb_x - x;
// if(is_server){
// bomb_x = 10;
// bomb_y = 10;
// }
// NetworkPlayer player = new NetworkPlayer(x, y, 2);
// NetworkBomb bomb = new NetworkBomb(bomb_x, bomb_y, 1);
// nm.send(player);
// nm.send(bomb);
// }
// }
// }