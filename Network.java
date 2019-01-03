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
    System.out.println(msg);
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
    System.out.println(msg);
    if (msg == null)
      return null;
    return msg;
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

  void send() {
    String msg = data.toSendFormat();
    network.send(msg);
  }

  void recv() {
    String msg = network.recv();
    data.toRecvFormat(msg);
  }
}

class SyncData {
  int[][] map;
  int sv_x, sv_y;
  int cl_x, cl_y;

  public String toSendFormat() {
    String string_data = String.format("%d %d %d %d", sv_x, sv_y, cl_x, cl_y);
    System.out.println(string_data);
    return string_data;
  }

  public void toRecvFormat(String data) {
    System.out.println(data);
    if (data == null)
      return;
    String[] xy = data.split(" ");
    sv_x = Integer.parseInt(xy[0]);
    sv_y = Integer.parseInt(xy[1]);
    cl_x = Integer.parseInt(xy[2]);
    cl_y = Integer.parseInt(xy[3]);
  }
}

public class TestNetwork {
  public static void main(String[] arg) {
    NetworkManager nm = new NetworkManager(true);
    nm.data.sv_x = -1;
    nm.data.sv_y = -1;
    nm.data.cl_x = -1;
    nm.data.cl_y = -1;
    for (int i = 0; i < 1000000; i++) {
      nm.recv();
      System.out.println(nm.data.cl_x + " " + nm.data.cl_y);
    }
  }
}