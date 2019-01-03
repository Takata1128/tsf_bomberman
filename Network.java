interface CommWrap {
  void send(String msg);
  String recv();
}

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

interface NetworkObject {
  public int getId();
  public void toRecvFormat(String data);
  public String toSendFormat();
}

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

  // idなし部分を引数に取る
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

class NetworkManager implements Runnable{
  private boolean is_server;
  private Thread recvloop;
  CommWrap network;

  NetworkManager(boolean is_server) {
    this.is_server = is_server;
    if (this.is_server) {
      network = new GameServer();
    } else {
      network = new GameClient();
    }
    recvloop = new Thread(this);
    recvloop.start();
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

  // 文字列を受け取ってidを見て、NetworkObjectをに変換
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
    }
    return obj;
  }

  //別スレッドでデータを待受
  //受信したら適切な型にキャストして通知
  public void run(){
    while(true){
      NetworkObject obj = recv();
      if (obj == null) {
        continue;
      }else if(obj instanceof NetworkPlayer){
        System.out.println("===NetworkPlayer===");
        NetworkPlayer obj2 = (NetworkPlayer) obj;
        System.out.println (obj2.x + " " + obj2.y);
      }else if(obj instanceof NetworkBomb){
        System.out.println("===NetworkBomb===");
        NetworkBomb obj2 = (NetworkBomb)obj;
        System.out.println(obj2.x + " " + obj2.y);
      }
    }
  }
}


//テストコード
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