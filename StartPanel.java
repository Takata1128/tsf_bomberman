import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.net.InetAddress;
import java.net.UnknownHostException;

class StartPanel extends JPanel implements Observer, Common {
    public KeyController controller;
    private MainPanel mp;
    private String titleImageName;
    private BufferedImage titleImage;
    private int menu_num = 4;
    private int mode = 0;
    private final int max_mode = 4;
    private int block_mode = 1;
    private int max_block_mode = 3;
    private JTextField input_IP;
    private InetAddress addr;
    // メニュー部分
    int str_size = 20;
    String single_play = "１台でプレイ";
    String server_play = "サーバーモード";
    String client_play = "クライアントモード";
    String block_str = "ブロック：　ふつう";
    String wait_matching = "マッチングを待っています";
    String seak_opponent = "対戦相手を探しています";

    public StartPanel(MainPanel mp) {
        // キーコントローラー追加
        controller = new KeyController();
        controller.addObserver(this);
        // MainPanel
        this.mp = mp;
        // タイトル画像読み込み
        titleImageName = "image/title.png";
        try {
            titleImage = ImageIO.read(getClass().getResource(titleImageName));
            titleImage = changSize(titleImage, 480, 480);
        } catch (IOException e) {
            System.out.println("image file not found. [" + titleImageName + "]");
        }
        // IP入力用フォーム
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3,1));
        input_IP = new JTextField("IPアドレスをいれてね(クライアントモードのみ）",30);     
        p.add(input_IP);
        input_IP.addActionListener(event -> {
            input_IP.transferFocus();
        });
        try{
            addr = InetAddress.getLocalHost();
        }catch(UnknownHostException e){
            e.printStackTrace();
        }
        JLabel host_name = new JLabel();
        JLabel host_addr = new JLabel();
        host_name.setText("Localhostname: "+ addr.getHostName());
        host_addr.setText("Localaddress: "+addr.getHostAddress());
        p.add(host_name);
        p.add(host_addr);
        this.add(p, BorderLayout.NORTH);
        
        // キー入力もらう
        addKeyListener(controller);
        setFocusable(true);
        // タイトル画面描画
        repaint();
    }

    BufferedImage changSize(BufferedImage image, int width, int height) {
        BufferedImage shrinkImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d = shrinkImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2d.drawImage(image, 0, 0, width, height, null);
        return shrinkImage;
    }

    public void paintComponent(Graphics g) {
        // 画像描画
        g.drawImage(titleImage, 0, 0, null);
        drawMenu(g, menu_num);
    }

    void setStringCenter(Graphics g, String str, int str_size, int xdiff, int ydiff,Color color) {
        // ウィンドウサイズ取得     
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
        if(mode<=3){
            g.setColor(new Color(0, 50, 255, 128));
            g.fillRoundRect(d.width / 2 - menu_width / 2, d.height / 2 - str_size, menu_width, menu_height, 0, 0);
        }

        if (mode == 0) {
            setStringCenter(g, single_play, str_size, 0, 0,Color.red);
            setStringCenter(g, server_play, str_size, 0, 40,Color.black);
            setStringCenter(g, client_play, str_size, 0, 80,Color.black);
            setStringCenter(g, block_str, str_size, 0, 120, Color.black);
        }
        if (mode == 1) {
             setStringCenter(g, single_play, str_size, 0, 0,Color.black);
            setStringCenter(g, server_play, str_size, 0, 40,Color.red);
            setStringCenter(g, client_play, str_size, 0, 80,Color.black);
            setStringCenter(g, block_str, str_size, 0, 120, Color.black);
        }
        if (mode == 2) {
            setStringCenter(g, single_play, str_size, 0, 0,Color.black);
            setStringCenter(g, server_play, str_size, 0, 40,Color.black);
            setStringCenter(g, client_play, str_size, 0, 80,Color.red);
            setStringCenter(g, block_str, str_size, 0, 120, Color.black);
        }
        if(mode==3){
            setStringCenter(g, single_play, str_size, 0, 0,Color.black);
            setStringCenter(g, server_play, str_size, 0, 40,Color.black);
            setStringCenter(g, client_play, str_size, 0, 80,Color.black);
            setStringCenter(g, block_str, str_size, 0, 120, Color.red);
        }
        if (mode == 4) {
            setStringCenter(g, wait_matching, 20, 0, 0,Color.black);
        }
        if (mode == 5) {
            setStringCenter(g, seak_opponent, 20, 0, 0,Color.black);
        }
    }

    // キー入力に反応
    public void update(Observable o, Object arg) {
        int state = controller.getState();
        switch (state) {
        case UP:
            mode--;
            if (mode == -1)
                mode = max_mode - 1;
            mode %= max_mode;
            break;
        case DOWN:
            mode++;
            mode %= max_mode;
            break;
        case LEFT:
            if (mode == 3) {
                block_mode--;
                if (block_mode == -1)
                    block_mode = max_block_mode - 1;
                block_mode %= max_block_mode;
            }
            break;
        case RIGHT:
            if (mode == 3) {
                block_mode++;
                block_mode %= max_block_mode;
            }
            break;
        case BOMB:// Bボタン押されたら
            if (mode == 0) {
                System.out.println("startPanel->gamePanel");
                mp.setstate(SINGLE_GAME_SCENE);
            } else if (mode == 1) {// サーバーできどう
                System.out.println("startPanel->gamePanel");
                mp.is_server = true;
                mode = 4;
                repaint();
                mp.network = new NetworkManager();
                if (!mp.network.connect()) {
                    break;
                }
                mp.setstate(MULTI_GAME_SCENE);
            } else if (mode == 2) {// クライアントできどう
                System.out.println("startPanel->gamePanel");
                mp.is_server = false;
                mode = 5;
                repaint();
                String ip = input_IP.getText();
                mp.network = new NetworkManager(ip);
                if (!mp.network.connect()) {
                    break;
                }
                mp.setstate(MULTI_GAME_SCENE);
            }
            break;
        }
        if(block_mode==0){
            block_str = "ブロック：　すくなめ";
            mp.block_cfg=0.4;
        }else if(block_mode==1){
            block_str = "ブロック：　ふつう";
            mp.block_cfg=0.6;
        } else if(block_mode ==2 )  {
            block_str = "ブロック：　おおめ";
            mp.block_cfg=0.8;
        }
        repaint();
    }
}
