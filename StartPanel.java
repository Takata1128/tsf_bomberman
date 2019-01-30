import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

class StartPanel extends JPanel implements Observer, Common {
    public KeyController controller;
    private MainPanel mp;
    private String titleImageName;
    private BufferedImage titleImage;
    private int mode=0;

    public StartPanel(MainPanel mp) {
        // キーコントローラー追加
        controller = new KeyController();
        controller.addObserver(this);
        // MainPanel
        this.mp = mp;
        // タイトル画像読み込み
        titleImageName = "image/title.png";
        try {
            titleImage = ImageIO.read(new File(titleImageName));
        } catch (IOException e) {
            System.out.println("image file not found. [" + titleImageName + "]");
        }
        // てきとう
        setLayout(null);
        setBackground(Color.white);
        // キー入力もらう
        addKeyListener(controller);
        setFocusable(true);
        // タイトル画面描画
        repaint();
    }

    public void paintComponent(Graphics g) {
        // ウィンドウサイズ取得
        Dimension size = getSize();
        // 画像描画
        g.drawImage(titleImage, 0, 0, null);
        // 以下文字列のためのあれこれ
        String single_play = "１台でプレイ";
        String server_play = "サーバーモード";
        String client_play = "クライアントモード";
        Font f = new Font("Serif", Font.PLAIN, 40);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(server_play, g).getBounds();
        int x = size.width / 2 - rectText.width / 2;
        int y = size.height / 2 - rectText.height / 2 + fm.getMaxAscent();
        if(mode==0){
            g.setColor(Color.red);
            g.drawString(single_play, x, y);
            g.setColor(Color.black);
            g.drawString(server_play, x, y + 40);
            g.drawString(client_play, x, y + 80);
        }
        if (mode == 1) {
            g.setColor(Color.black);
            g.drawString(single_play, x, y);
            g.setColor(Color.red);
            g.drawString(server_play, x, y + 40);
            g.setColor(Color.black);
            g.drawString(client_play, x, y + 80);
        }
        if (mode == 2) {
            g.setColor(Color.black);
            g.drawString(single_play, x, y);
            g.drawString(server_play, x, y + 40);
            g.setColor(Color.red);
            g.drawString(client_play, x, y + 80);
        }
    }

    // キー入力に反応
    public void update(Observable o, Object arg) {
        int state = controller.getState();
        switch (state) {
        case UP:
            mode--;
            if (mode == -1)
                mode = 2;
            mode %= 3;
            repaint();
            break;
        case DOWN:
            mode++;
            mode %= 3;
            repaint();
            break;
        case BOMB:// Bボタン押されたら
            if(mode == 0){
                System.out.println("startPanel->gamePanel");
                mp.setstate(SINGLE_GAME_SCENE);
            }
            else if (mode == 1) {// サーバーできどう
                System.out.println("startPanel->gamePanel");
                mp.is_server=true;
                mp.setstate(MULTI_GAME_SCENE);
            } else if (mode == 2) {// クライアントできどう
                System.out.println("startPanel->gamePanel");
                mp.is_server=false;
                mp.setstate(MULTI_GAME_SCENE);
            }
            break;
        }
    }
}
