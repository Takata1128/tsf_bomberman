import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Map implements Common {
  // 行数（単位：マス）
  private static final int ROW = 15;
  // 列数（単位：マス）
  private static final int COL = 15;

  // マップ 0:床 1:壁
  private int[][] map = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 },
      { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

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
  private NetworkManager network;

  public Map(GamePanel panel, NetworkManager network) {
    // イメージをロード
    this.network = network;
    loadImage();
    im = new ItemManager(this, panel, network);
    for (int i = 1; i < ROW - 1; i++) {
      for (int j = 1; j < ROW; j++) {
        if (map[i][j] == 0 && !(i == 1 && j == 1) && !(i == 2 && j == 1) && !(i == 1 && j == 2) && !(i == 13 && j == 13)
            && !(i == 13 && j == 12) && !(i == 12 && j == 13)) {
          double r = Math.random();
          if (r < 0.6)
            map[i][j] = 2;
        }
      }
    }
    setItem();
  }

  public void draw(Graphics g) {
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
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
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
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
