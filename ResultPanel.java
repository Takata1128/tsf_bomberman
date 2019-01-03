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

    public ResultPanel(MainPanel mp){
        //キーコントローラー追�?
        controller = new KeyController();
        controller.addObserver(this);
        //MainPanel
        this.mp = mp;
        //タイトル画像読み込み
        resultImageName = "image/result.png";
        try{
            resultImage = ImageIO.read(new File(resultImageName));
        }catch(IOException e){
            System.out.println("image file not found. [" + resultImageName + "]");
        }
        //てきと�?
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
        //以下文字�?��?�ためのあれこれ
        String text = "Press B button to play again.";
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

    public void update(Observable o,Object arg){
        int state = controller.getState();
        switch(state){
            case BOMB:
                System.out.println("resultPanel->startPanel");
                mp.setstate(TITLE_SCENE);
                break;
        }
    }
}