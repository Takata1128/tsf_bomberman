import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

class StartPanel extends JPanel implements Observer,Common{
    public KeyController controller;
    private MainPanel mp;
    private String titleImageName;
    private BufferedImage titleImage;

    public StartPanel(MainPanel mp){
        //キーコントローラー追加
        controller = new KeyController();
        controller.addObserver(this);
        //MainPanel
        this.mp = mp;
        //タイトル画像読み込み
        titleImageName = "image/title.png";
        try{
            titleImage = ImageIO.read(new File(titleImageName));
        }catch(IOException e){
            System.out.println("image file not found. [" + titleImageName + "]");
        }
        //てきとう
        setLayout(null);
        setBackground(Color.white);
        //キー入力もらう
        addKeyListener(controller);
        setFocusable(true);
        //タイトル画面描画
        repaint();
    }

    public void paintComponent(Graphics g){
        //ウィンドウサイズ取得
        Dimension size = getSize();
        //画像描画
        g.drawImage(titleImage,0,0,null);
        //以下文字列のためのあれこれ
        String text = "Press B button to start game.";
        Font f = new Font("Serif",Font.PLAIN,40);
        g.setFont(f);
        g.setColor(Color.black);
        //中央に表示するためのあれこれ
        FontMetrics fm = g.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(text,g).getBounds();
        int x = size.width/2 - rectText.width/2;
        int y = size.height/2 - rectText.height/2+fm.getMaxAscent();
        g.drawString(text, x, y);
      
    }
    //キー入力に反応
    public void update(Observable o,Object arg){
        int state = controller.getState();
        switch(state){
            case BOMB://Bボタン押されたら
                System.out.println("startPanel->gamePanel");
                mp.setstate(GAME_SCENE);
                break;
            //TODO:Sでサーバー側起動、Cでクライアント側起動
        }
    }
}
