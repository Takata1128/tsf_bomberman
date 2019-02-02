import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Map implements Common {
  // 行数（単位：マス）
  public int row = 15;
  // 列数（単位：マス）
  public int col = 15;
  
  // マップ 0:床 1:壁
  private int[][] map;

  // チップセット
  private Image floorImage;
  private Image wallImage;
  private Image blockImage;
  private Image itemImage;

  // メインパネルへの参照
  private GamePanel panel;

  // アイテム管理
  public ItemManager im;
  private double random;
  private int forReturn;
  private NetworkManager network=null;
  public Map(GamePanel panel) {
    // イメージをロード
    loadImage();
    load("map.txt");
    create_block(panel.block_cfg);
    im = new ItemManager(this, panel);
    setItem();
  }
  public Map(GamePanel panel, NetworkManager network) {
    // イメージをロード
    this.network = network;
    loadImage();
    load("map.txt");
    create_block(panel.block_cfg);
    im = new ItemManager(this, panel, network);
    setItem();
  }

  private void load(String filename) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
      String line = br.readLine();
      // マップを作成
      map = new int[row][col];
      for (int i = 0; i < row; i++) {
        line = br.readLine();
        for (int j = 0; j < col; j++) {
          map[i][j] = Integer.parseInt(line.charAt(j) + "");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void create_block(double p){
    for (int i = 1; i < row - 1; i++) {
      for (int j = 1; j < col - 1; j++) {
        if (map[i][j] == 0 && !(i == 1 && j == 1) && !(i == 2 && j == 1) && !(i == 1 && j == 2)
            && !(i == row - 2 && j == col - 2) && !(i == row - 3 && j == col - 2) && !(i == row - 2 && j == col - 3)) {
          double r = Math.random();
          if (r < p)
            map[i][j] = 2;
        }
      }
    }
  }

  public void draw(Graphics g) {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        // mapの値に応じて画像を描く
        switch (map[i][j]) {
        case 0: // 床
          g.drawImage(floorImage, j * CS, i * CS, panel);
          break;
        case 1: // 壁
          g.drawImage(wallImage, j * CS, i * CS, panel);
          break;
        case 2: // 壁
          g.drawImage(blockImage, j * CS, i * CS, panel);
          break;
        case 3: // 床と爆弾
          g.drawImage(floorImage, j * CS, i * CS, panel);
          break;
        case 4: // 床と爆風
          g.drawImage(floorImage, j * CS, i * CS, panel);
          break;
        case 5:// 床とアイテム？
          g.drawImage(floorImage, j * CS, i * CS, panel);
          im.drawItem(j, i, g);
          break;
        }
      }
    }
  }

  public boolean isHit(int x, int y) {
    // (x,y)が床or爆風があったらぶつからない
    if (map[y][x] == 0 || map[y][x] == 4 || map[y][x] == 5) {
      return false;
    }

    // なければぶつかる
    return true;
  }

  // マップの状態を所得
  public int get(int x, int y) {
    return map[y][x];
  }

  public NetworkMap getNetworkMap() {
    return new NetworkMap(map);
  }

  public void setNetworkMap(NetworkMap nmap) {
    map = nmap.map.clone();
  }

  // マップの状態をセット
  public void set(int x, int y, int i) {
    map[y][x] = i;
  }

  // 爆風について
  public boolean effHit(int x, int y) {
    if (map[y][x] == 4) { // 爆風に当たったら
      return true;
    }
    return false;
  }

  // アイテム所得用 アイテムが増えたらいじるかも
  public int getItem(int x, int y) {
    if (map[y][x] == 5) {
      map[y][x] = 0;
      // return 1;
    }
    forReturn = im.getItemEff(x, y);
    im.stopEff(x, y);
    return forReturn;
  }

  private void setItem() {
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        if (map[i][j] == 2) {
          random = Math.random();
          im.set(j, i, random);
        }
      }
    }
    im.resetNum();
  }

  private void loadImage() {
    ImageIcon icon = new ImageIcon(getClass().getResource("image/floor.gif"));
    floorImage = icon.getImage();

    icon = new ImageIcon(getClass().getResource("image/wall.gif"));
    wallImage = icon.getImage();

    icon = new ImageIcon(getClass().getResource("image/block.png"));
    blockImage = icon.getImage();

    icon = new ImageIcon(getClass().getResource("image/item1.png"));
    itemImage = icon.getImage();
  }
}
