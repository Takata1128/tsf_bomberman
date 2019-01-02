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
