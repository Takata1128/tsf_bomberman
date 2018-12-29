import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class GamePanel extends JPanel implements Runnable{
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
		setFocusable(true);
		addKeyListener(controller);

		//bombManager = new BombManager("image/bomb.png", this);
		p1 = new Player(1,1,"image/BMW.png",map,this,controller);//プレイヤー生成
		//bm = new Bomb(1, 1, "image/bomb.png",1, map, this);
		gameLoop = new Thread(this);
		gameLoop.start();
		requestFocus();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
    	//マップを描く
    	map.draw(g);
	//bm.draw(g);
		//プレイヤーを描く
        p1.draw(g);
    }

    public void run(){
    	while(true){
    		repaint();
    		try{
    			Thread.sleep(100);
    		}catch(InterruptedException e){
    			e.printStackTrace();
    		}
    	}
    }
}
