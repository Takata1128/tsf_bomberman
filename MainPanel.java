import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class MainPanel extends JPanel implements Runnable{
//パネルサイズ
	private static final int WIDTH = 480;
	private static final int HEIGHT = 480;

	private Map map;
	private Player p1;
	private Thread mainLoop;
	public KeyController controller;
	private StartPanel sp;
	private GamePanel gp;
	private int state=0;
	private int oldState=0;

    public MainPanel(){
		//パネルの推奨サイズを設定
		setLayout(new GridLayout(1, 1, 0, 0));
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		gp = new GamePanel(this);
		this.add(gp);
		//sp = new StartPanel(this);
		//this.add(sp);
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
				if(oldState==0)remove(sp);
				else if(oldState==1)remove(gp);
				if(state==0){
					sp = new StartPanel(this);
					this.add(sp);
				}else if(state==1){
					gp = new GamePanel(this);
					this.add(gp);
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
