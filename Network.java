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

class PlayerXY implements NetworkObject {
  int x, y, direction, id;

  PlayerXY(int x, int y, int direction) {
    this.id = 1;
    this.x = x;
    this.y = y;
    this.direction = direction;
  }

  PlayerXY() {
    this.id = 1;
  }

  public String toSendFormat() {
    String string_data = String.format("%d,%d %d %d", id, x, y, direction);
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

class NetworkManager {
  public SyncData data;
  private boolean is_server;
  CommWrap network;

  NetworkManager(boolean is_server) {
    data = new SyncData();
    this.is_server = is_server;
    if (this.is_server) {
      network = new GameServer();
    } else {
      network = new GameClient();
    }
  }

  // NetworkObjectを受け取って文字列に変換して送信
  void send(NetworkObject obj) {
    String msg = obj.toSendFormat();
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
      obj = new PlayerXY();
      obj.toRecvFormat(data);
    }
    return obj;
  }
}

class SyncData {
  int[][] map;
  int sv_x, sv_y;
  int cl_x, cl_y;

  public String toSendFormat() {
    String string_data = String.format("%d %d %d %d", sv_x, sv_y, cl_x, cl_y);
    //System.out.println(string_data);
    return string_data;
  }

  public void toRecvFormat(String data) {
    //System.out.println(data);
    if (data == null)
      return;
    String[] xy = data.split(" ");
    sv_x = Integer.parseInt(xy[0]);
    sv_y = Integer.parseInt(xy[1]);
    cl_x = Integer.parseInt(xy[2]);
    cl_y = Integer.parseInt(xy[3]);
  }
}

// public class TestNetwork {
//   public static void main(String[] arg) {
//     NetworkManager nm = new NetworkManager(true);
//     int x = 0, y = 0;
//     for (int i = 0; i < 1000; i++) {
//       x += 1;
//       y += 1;
//       PlayerXY player = new PlayerXY(x, y);
//       nm.send(player);
//       NetworkObject obj = nm.recv();
//       if (obj == null) {
//         continue;
//       }
//       PlayerXY obj2 = (PlayerXY) obj;
//       System.out.println("[Client]" + obj2.x + " " + obj2.y);
//       System.out.println("[Server]" + x + " " + y);
//     }
//   }
// }