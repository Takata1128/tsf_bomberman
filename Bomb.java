class Bomb{
  private int power;//火力
  private int x,y;//座標
  private Image image;//画像
  private static int resttime = 3;//爆発までの時間
  private Map map;//マップ
  private MainPanel panel;//パネル
  public void Bomb(int x,int y,int power,Map map,Mainpanel panel){
    this.x = x;
    this.y = y;
    this.power = power;
    this.map = map;
    this.panel = panel;
  }
  public int getpower(){
    return power;
  }
  public void move(){
    //もろもろの計算処理
  }
  public void draw(){
    //描画処理
  }
}
