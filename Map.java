import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Map implements Common {
    // 行数（単位：マス）
    private static final int ROW = 15;
    // 列数（単位：マス）
    private static final int COL = 15;

    // マップ 0:床 1:壁
    private int[][] map = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};

    // チップセット
    private Image floorImage;
    private Image wallImage;
    private Image blockImage;

    // メインパネルへの参照
    private GamePanel panel;

    public Map(GamePanel panel) {
        // イメージをロード
        loadImage();
        for(int i=1;i<ROW-1;i++){
            for(int j=1;j<ROW;j++){
                if(map[i][j]==0&&!(i==1&&j==1)&&!(i==2&&j==1)&&!(i==1&&j==2)){
                    double r = Math.random();
                    if(r<0.6)map[i][j]=2;
                }
            }
        }
    }

    public void draw(Graphics g) {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                // mapの値に応じて画像を描く
                switch (map[i][j]) {
                    case 0 : // 床
                        g.drawImage(floorImage, j * CS, i * CS, panel);
                        break;
                    case 1 : // 壁
                        g.drawImage(wallImage, j * CS, i * CS, panel);
                        break;
                    case 2 : // 壁
                        g.drawImage(blockImage, j * CS, i * CS, panel);
                        break;
                    
                }
            }
        }
    }

    public boolean isHit(int x, int y) {
        // (x,y)に壁があったらぶつかる
        if (map[y][x] != 0) {
            return true;
        }

        // なければぶつからない
        return false;
    }

    private void loadImage() {
        ImageIcon icon = new ImageIcon(getClass().getResource("image/floor.gif"));
        floorImage = icon.getImage();

        icon = new ImageIcon(getClass().getResource("image/wall.gif"));
        wallImage = icon.getImage();

        icon = new ImageIcon(getClass().getResource("image/block.png"));
        blockImage = icon.getImage();
    }
}
