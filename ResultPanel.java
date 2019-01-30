import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

class ResultPanel extends JPanel implements Observer,Common{
    public KeyController controller;
    private MainPanel mp;
    private String resultImageName;
    private BufferedImage resultImage;
    private int mode = 0;

    public ResultPanel(MainPanel mp,boolean is_win){
        //キーコントローラー追加
        controller = new KeyController();
        controller.addObserver(this);
        //MainPanel
        this.mp = mp;
        //画像読み込み
        if(is_win){//かち
            resultImageName = "image/win.png";
        }else{//まけ
            resultImageName = "image/lose.png";
        }
        try{
            resultImage = ImageIO.read(new File(resultImageName));
        }catch(IOException e){
            System.out.println("image file not found. [" + resultImageName + "]");
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
        //ウィンドウサイズ描画
        Dimension size = getSize();
        //画像描画
        g.drawImage(resultImage,0,0,null);
        //以下文字列のためのあれこれ
        String next_play = "もういちど";
        String exit = "やめる";
        Font f = new Font("Serif",Font.PLAIN,40);
        g.setFont(f);
        //中央に表示するためのあれこれ
        FontMetrics fm = g.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(next_play,g).getBounds();
        int x = size.width/2 - rectText.width/2;
        int y = size.height/2 - rectText.height/2+fm.getMaxAscent();
        if(mode==0){
            g.setColor(Color.red);
            g.drawString(next_play, x, y);
            g.setColor(Color.black);
            g.drawString(exit, x, y+40);
        }else if (mode ==1){
            g.setColor(Color.black);
            g.drawString(next_play, x, y);
            g.setColor(Color.red);
            g.drawString(exit, x, y+40);
        }
    }

    public void update(Observable o,Object arg){
        int state = controller.getState();
        switch(state){
            case UP:
            mode--;
            if (mode == -1)
                mode = 1;
            mode %= 2;
            repaint();
            break;
        case DOWN:
            mode++;
            mode %= 2;
            repaint();
            break;
        case BOMB:
            if(mode==0){//もういちど
            System.out.println("resultPanel->startPanel");
            mp.setstate(TITLE_SCENE);
            break;
            }else if(mode == 1){//やめる
                System.exit(0);
            }
        }
    }
}