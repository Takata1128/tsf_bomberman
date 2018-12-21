import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.awt.event.KeyListener;

class MainPanel extends JPanel implements Runnable{
//パネルサイズ
	private static final int WIDTH = 480;
	private static final int HEIGHT = 480;

	private Map map;
	private Player p1;
	private KeyController controller;
	private Thread gameLoop;
	private StartPanel sp;
	private GamePanel gp;
	private int state;
	private int oldState;

    public MainPanel(){
    	//パネルの推奨サイズを設定
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setLayout(new GridLayout(1, 1, 0, 0));
		gp = new GamePanel(this);
		this.add(gp);
		gameLoop = new Thread();
		gameLoop.start();
	  }
    
    public void run(){
    	while(true){
			if(state!=oldState){
				if(oldState==0)
				this.remove(sp);
				else if(oldState==1)
				this.remove(gp);
				if(state==0){
					sp = new StartPanel(this);
					this.add(sp);
				}else if(state==1){
					new GamePanel(this);
					this.add(gp);
				}
				validate();
				oldState=state;
			}
    		try{
    			Thread.sleep(100);
    		}catch(InterruptedException e){
    			e.printStackTrace();
    		}
    	}
    }
}
