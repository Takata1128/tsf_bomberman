import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class MainPanel extends JPanel{
//パネルサイズ
	private static final int WIDTH = 480;
	private static final int HEIGHT = 480;

	private Map map;
	private Player p1;
	private KeyController controller;

    public MainPanel(){
    	//パネルの推奨サイズを設定
    	setPreferredSize(new Dimension(WIDTH,HEIGHT));
			map = new Map(this);
			controller = new KeyController();
			p1 = new Player(1,1,"image/hero.gif",map,this,controller);
			setFocusable(true);
			addKeyListener(controller);
	  }

    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	//マップを描く
    	map.draw(g);
			//プレイヤーを描く
			p1.draw(g);
    }
}
