import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

class ResultPanel extends JPanel implements Observer, Common {
    public KeyController controller;
    private MainPanel mp;
    private String resultImageName;
    private BufferedImage resultImage;
    // メニュー部分
    private int menu_num = 2;
    private int mode = 0;
    private final int max_mode = 2;
    private int str_size = 20;
    private String next_play = "タイトル画面へ";
    private String exit = "やめる";

    public ResultPanel(MainPanel mp, boolean is_win) {
        // キーコントローラー追加
        controller = new KeyController();
        controller.addObserver(this);
        // MainPanel
        this.mp = mp;
        // 画像読み込み
        if (is_win) {// かち
            resultImageName = "image/win.png";
        } else {// まけ
            resultImageName = "image/lose.png";
        }
        if(mp.network==null){
            if(is_win)resultImageName = "image/1p_win.png";
            else resultImageName = "image/2p_win.png";
        }
        try {
            resultImage = ImageIO.read(getClass().getResource(resultImageName));
        } catch (IOException e) {
            System.out.println("image file not found. [" + resultImageName + "]");
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
        // 画像描画
        g.drawImage(resultImage, 0, 0, null);
        // メニュー描画
        drawMenu(g, menu_num);
    }

    void setStringCenter(Graphics g, String str, int str_size, int xdiff, int ydiff, Color color) {
        Dimension size = getSize();
        Font f = new Font(Font.MONOSPACED, Font.PLAIN, str_size);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        Rectangle rectText = fm.getStringBounds(str, g).getBounds();
        int x = size.width / 2 - rectText.width / 2;
        int y = size.height / 2 - rectText.height / 2 + fm.getMaxAscent();
        x += xdiff;
        y += ydiff;
        g.setColor(color);
        g.drawString(str, x, y);
    }

    void drawMenu(Graphics g, int num) {
        Dimension d = getSize();
        int menu_width = 200, menu_height = 40 * num;
        g.setColor(new Color(0, 50, 255, 128));
        g.fillRoundRect(d.width / 2 - menu_width / 2, d.height / 2 - str_size, menu_width, menu_height, 0, 0);
        if (mode == 0) {
            setStringCenter(g, next_play, str_size, 0, 0, Color.red);
            setStringCenter(g, exit, str_size, 0, 40, Color.black);
        }
        if (mode == 1) {
            setStringCenter(g, next_play, str_size, 0, 0, Color.black);
            setStringCenter(g, exit, str_size, 0, 40, Color.red);
        }
    }

    public void update(Observable o, Object arg) {
        int state = controller.getState();
        switch (state) {
        case UP:
            mode--;
            if (mode == -1)
                mode = max_mode - 1;
            mode %= max_mode;
            repaint();
            break;
        case DOWN:
            mode++;
            mode %= max_mode;
            repaint();
            break;
        case BOMB:
            if (mode == 0) {// タイトル画面へ
                System.out.println("resultPanel->startPanel");
                mp.setstate(TITLE_SCENE);
                break;
            } else if (mode == 1) {// やめる
                System.exit(0);
            }
        }
    }
}