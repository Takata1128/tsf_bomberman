import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.util.*;

class BombManager implements Common{

    private int max = 100; //爆弾の上限
    private Bomb[] bomb = new Bomb[max];
    private int[] isSet = new int[max];
    private Image bombImage, effImage;
    private GamePanel panel;
    private int num = 0;
    private Map map;
    public BombManager(String filename1,String filename2, Map map,GamePanel panel){
	loadImage(filename1, filename2);
	this.map = map;
	this.panel = panel;
	
    }

    private void loadImage(String filename1, String filename2){
	ImageIcon icon =  new ImageIcon(getClass().getResource(filename1));
	bombImage = icon.getImage();

	icon = new ImageIcon(getClass().getResource(filename2));
	effImage = icon.getImage();
    }

    //爆弾セット
    public void set(int x, int y){
	if(num < max){
	    bomb[num] = new Bomb(x, y, bombImage, effImage, 3, panel, map);
	    //bomb[num].draw(g);
	    isSet[num] = 1;
	    map.set(x, y, 3);
	    System.out.println("Bomb set: "+num);
	    num++;
	}
    }

    public void draw(Graphics g){
	for(int i = 0; i < max; i++){
	    if(isSet[i]== 1){
		bomb[i].draw(g);
	    }
	}
    }
	
}
