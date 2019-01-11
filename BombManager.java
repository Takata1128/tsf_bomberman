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
    private int setMax = 2;  //設置できる最大数
    private int bombPow = 3; //爆弾の範囲
    private boolean pane = false;
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
	if(num < setMax){
	    bomb[num] = new Bomb(x, y, bombImage, effImage, bombPow, panel, map, pane);
	    isSet[num] = 1;
	    map.set(x, y, 3);
	    System.out.println("Bomb set: "+num);
	    num++;
	}
    }

    //ボムの範囲拡大(アイテム用)
    public void setPow(int pow){
	    bombPow += pow;
	    if(bombPow > 15){
		bombPow = 15;
	    }else if(bombPow < 1){
		bombPow = 1;
	    }
	    System.out.println("BombPow : "+bombPow);
    }

    //置ける数を変える（アイテム用）
    public void addMax(int num){
	if(num == -2){
	    setMax--;
	    if(setMax < 1){
		setMax = 1;
	    }
	}else if(num == 2){
	    setMax++;
	}
	System.out.println("BombMax : "+setMax);
    }

    //貫通ボムにするかどうか（アイテム用）
    public void isPanatrate(int num){
	if(num == -3){
	    pane = false;
	}else if(num == 3){
	    pane = true;
	}
	System.out.println("Panatrate :"+pane);
    }

    public void draw(Graphics g){
	for(int i = 0; i < setMax; i++){
	    if(isSet[i]== 1){
		bomb[i].draw(g);
		if(map.get(bomb[i].getX(), bomb[i].getY()) == 4){
		    num--;
		    if(num < 0){
			num = 0;
		    }
		}
	    }
	}
    }
	  	
}
