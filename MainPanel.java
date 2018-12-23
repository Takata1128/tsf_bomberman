import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class MainPanel extends JPanel implements Runnable{
//パネルサイズ
	private static final int WIDTH = 480;
	private static final int HEIGHT = 480;

	private Map map;
	private Player p1;
	private Opponent opponent;
	private KeyController controller;
	private OpponentController opponentController;
	private NetworkManager nm;
	private Thread gameLoop;

    public MainPanel(boolean is_server){
    	//パネルの推奨サイズを設定
    	setPreferredSize(new Dimension(WIDTH,HEIGHT));
			map = new Map(this);//マップ生成
			controller = new KeyController();//キーコントローラー生成
			opponentController = new OpponentController();

			//サーバーかクライアントかでPlayerの位置を変更
			if(is_server){
				nm = new NetworkManager(true);
				p1 = new Player(1, 1, "image/Bomberman.png", map, this, controller, nm);// プレイヤー生成
				opponent = new Opponent(13, 13, "image/Bomberman.png", map, this, opponentController);//相手の生成
			}else{
				nm = new NetworkManager(false);
				p1 = new Player(13, 13, "image/Bomberman.png", map, this, controller, nm);
				opponent = new Opponent(1, 1, "image/Bomberman.png", map, this, opponentController);
			}
			gameLoop = new Thread(this);
			gameLoop.start();
			setFocusable(true);
			addKeyListener(controller);
	  }

    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	//マップを描く
    	map.draw(g);
			//プレイヤーを描く
			p1.draw(g);
			opponent.draw(g);
    }
    
    public void run(){
    	while(true){
				NetworkObject obj = nm.recv();
				if(obj instanceof PlayerXY){
					opponentController.setState((PlayerXY)obj);
				}
				repaint();
    		try{
    			Thread.sleep(100);
    		}catch(InterruptedException e){
    			e.printStackTrace();
    		}
    	}
    }
}
