import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class MainPanel extends JPanel implements Runnable,Common{
//パネルサイズ
	private static final int WIDTH = 480;
	private static final int HEIGHT = 480;

	private Map map;
	private Player p1;
<<<<<<< HEAD
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
=======
	private Thread mainLoop;
	private StartPanel sp;
	private GamePanel gp;
	private ResultPanel rp;
	private int state;
	private int oldState;

    public MainPanel(){
		//パネルの推奨サイズを設定
		setLayout(new GridLayout(1, 1, 0, 0));
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		//gp = new GamePanel(this);
		//this.add(gp);
		//state = GAME_SCENE;
		//oldState = GAME_SCENE;
		sp = new StartPanel(this);
		this.add(sp);
		state = TITLE_SCENE;
		oldState = TITLE_SCENE;
		mainLoop = new Thread(this);
		mainLoop.start();
	  }
    
    public void run(){
    	while(true){
>>>>>>> master
    		try{
    			Thread.sleep(100);
    		}catch(InterruptedException e){
    			e.printStackTrace();
			}
			if(state!=oldState){
				if(oldState==TITLE_SCENE){
					remove(sp);
					System.out.println("StartPanel was removed.");
				}
				else if(oldState==GAME_SCENE){
					gp.removeAll();
					removeAll();
					System.out.println("GamePanel was removed.");
				}
				else if(oldState==RESULT_SCENE){
					remove(rp);
					System.out.println("ResultPanel was removed.");
				}
				if(state==TITLE_SCENE){
					sp = new StartPanel(this);
					this.add(sp);
					sp.requestFocus();//キー入力のため
					System.out.println("StartPanel was added.");
				}else if(state==GAME_SCENE){
					gp = new GamePanel(this);
					this.add(gp);
					gp.requestFocus();//キー入力のため
					System.out.println("GamePanel was added.");
				}else if(state==RESULT_SCENE){
					rp = new ResultPanel(this);
					this.add(rp);
					rp.requestFocus();//キー入力のため
					System.out.println("ResultPanel was added.");
				}
				validate();
				oldState=state;
			}
    	}
	}
	
	public void setstate(int s){
		state = s;
	}
}
