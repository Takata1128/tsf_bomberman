import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class GamePanel extends JPanel implements Runnable,Common{
    private Map map;
	private Player p1;
    private Bomb bm;
	private KeyController controller;
	private Thread gameLoop;
	private MainPanel mp;
	private BombManager bombManager;
    
    public GamePanel(MainPanel mp){
		this.mp = mp;
        map = new Map(this);//マップ生成
		controller = new KeyController();//キーコントローラー生成
		//キー入力受付
		setFocusable(true);
		addKeyListener(controller);
		p1 = new Player(1,1,"image/BMW.png",map,this,controller);//プレイヤー生成
		//ゲームループ開始
		gameLoop = new Thread(this);
		gameLoop.start();
    }

    public void paintComponent(Graphics g){
		super.paintComponent(g);
    	//マップを描く
    	map.draw(g);
		//プレイヤーを描く
        p1.draw(g);
    }

    public void run(){
    	while(true){//ゲームループ
			repaint();
			if(!p1.isLive){//死んだらリザルトに
				mp.setstate(RESULT_SCENE);
				break;
			}
    		try{
    			Thread.sleep(100);
    		}catch(InterruptedException e){
    			e.printStackTrace();
    		}
    	}
    }
}
