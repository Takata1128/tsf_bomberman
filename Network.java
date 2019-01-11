//[責務] Commライブラリのラ(ClientとServerの差異吸収)
interface CommWrap {
  void send(String msg);
  String recv();
}
//[責務] Commライブラリのclient実装
class GameClient implements CommWrap {
  private CommClient cl = null;

  GameClient() {
    cl = new CommClient("localhost", 1025);
    cl.setTimeout(1); // non-wait で通信
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
}
//[責務] Commライブラリのserver実装
class GameServer implements CommWrap {
  private CommServer sv = null;

  GameServer() {
    sv = new CommServer(1025);
    sv.setTimeout(1); // non-wait で通信
    System.out.println("Connected !");
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
}

//[責務] データの送受信に必要なメソッドを定義(これを実装してプロトコルを定義する)
interface NetworkObject {
  public int getId();
  public void toRecvFormat(String data);
  public String toSendFormat();
}

//[責務] プレイヤーのデータの送受信プロトコルを定義
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
//[責務] 爆弾の送受信に関するプロトコルを定義
class NetworkBomb implements NetworkObject{
  int x, y, power, id;

  NetworkBomb(int x, int y, int power){
    this.id = 2;
    this.x = x;
    this.y = y;
    this.power = power;
  }

  NetworkBomb(){
    this.id = 2;
  }

  public String toSendFormat(){
    String string_data = String.format("%d %d %d", x, y, power);
    return string_data;
  }

  public void toRecvFormat(String data){
    if (data == null)
      return;
    String[] xy = data.split(" ");
    this.x = Integer.parseInt(xy[0]);
    this.y = Integer.parseInt(xy[1]);
    this.power = Integer.parseInt(xy[2]);
  }

  public int getId(){
    return this.id;
  }
}

//[責務] マップデータの送受信プロトコルの定義
class NetworkMap implements NetworkObject{
  public int[][] map;

  private int Row = 15;
  private int Col = 15;
  private int id;

  NetworkMap(int[][] map){
    this.id = 3;
    this.map = map.clone();
  }

  NetworkMap(){
    this.id = 3;
    this.map = new int[Row][Col];
  }
  public String toSendFormat(){
    StringBuilder sb = new StringBuilder();
    for(int i=0; i<Row; i++){
      for(int j=0; j<Col; j++){
         sb.append(String.format("%d ", map[i][j]));
      }
    }
    return sb.toString();
  }

  public void toRecvFormat(String data){
    int count = 0;
    if(data == null)
      return;
    String[] map_data = data.split(" ");
    for(int i=0; i<Row; i++){
      for(int j=0; j<Col; j++){
        map[i][j] = Integer.parseInt(map_data[count++]);
      }
    }
  }

  public int getId(){
    return this.id;
  }
}

//[責務] NetworkObjectの送受信
class NetworkManager{
  private boolean is_server;
  CommWrap network;

  NetworkManager(boolean is_server) {
    this.is_server = is_server;
    if (this.is_server) {
      network = new GameServer();
    } else {
      network = new GameClient();
    }
  }

  // NetworkObjectを受け取って文字列に変換して送信
  void send(NetworkObject obj) {
    String id = String.valueOf(obj.getId());
    String msg = id + "," + obj.toSendFormat();
    if(msg != null){
      System.out.println("[debug send]" + msg);
    }
    network.send(msg);
  }

  // 文字列を受け取ってidを見て、NetworkObjectに変換
  // msgはid,data1 data2 data3 ...となっておりidを見て適切なNetworkObjectを生成する
  NetworkObject recv() {
    String msg = network.recv();
    if(msg != null){
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
    }else if(id == 2){
      obj = new NetworkBomb();
      obj.toRecvFormat(data);
    }else if(id == 3){
      obj = new NetworkMap();
      obj.toRecvFormat(data);
    }
    return obj;
  }
}


//
public class Network {
  public static void main(String[] arg) {
    boolean is_server = false;
    String recvMessage = "Server";
    String sendMessage = "Clinet";
    if(arg[0].equals("s")){
      is_server = true;
      recvMessage = "Client";
      sendMessage = "Server";
    }
    NetworkManager nm = new NetworkManager(is_server);
    int x = 0, y = 0;
    int bomb_x = 0, bomb_y = 0;
    for (int i = 0; i < 100; i++) {
      x += 1;
      y += 1;
      bomb_x *= x + y;
      bomb_y += bomb_x - x;
      if(is_server){
        bomb_x = 10;
        bomb_y = 10;
      }
      NetworkPlayer player = new NetworkPlayer(x, y, 2);
      NetworkBomb bomb = new NetworkBomb(bomb_x, bomb_y, 1);
      nm.send(player);
      nm.send(bomb);
    }
  }
}