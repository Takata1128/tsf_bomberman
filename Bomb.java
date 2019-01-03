<<<<<<< HEAD
=======
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Bomb implements Common, ActionListener{
    private int power;//火力
    private int x,y;//座標
    private Image bombImage, effImage; //爆弾、爆風の画像
    private static int resttime = 3;//爆発までの時間
    private GamePanel panel;//パネル
    private javax.swing.Timer timer;
    private int illust = 0; // 0 = bomb, 1 = eff;
    private Map map;
    private int mapState; //マップの状態
    public Bomb(int x,int y,Image image1,Image image2,int power,GamePanel panel, Map map){
	this.x = x;
	this.y = y;
	this.bombImage = image1;
	this.effImage = image2;
	this.power = power;
	this.panel = panel;
	this.map = map;
	timer = new javax.swing.Timer(resttime*1000, this);
	timer.start();
    }


    public int getPower(){
	return power;
    }

    public void setPower(int pow){
	power = pow;
    }
    
    public void move(){
    //もろもろの計算処理
      
    }

    
    public void draw(Graphics g){
	//描画処理
	if(map.effHit(x,y) == true && illust == 0){ //爆弾が爆風に当たったら
	    illust = 1;
	}
	switch(illust){
	case 0: //爆弾
	    g.drawImage(bombImage,x*CS,y*CS,panel);
	    break;
	case 1: //爆風
	    for(int i = x; i >= x-power && i >= 0; i--){
		mapState = map.get(i,y);
		if(mapState == 2){
		    g.drawImage(effImage,i*CS,y*CS,panel);
		    break;
		}else if(mapState != 1){
		    g.drawImage(effImage,i*CS,y*CS,panel);
		    map.set(i,y,4);
		}else{
		    break;
		}
	    }
	    for(int i = x+1; i <= x+power && i <= 15; i++){
		mapState = map.get(i,y);
		if(mapState == 2){
		    g.drawImage(effImage,i*CS,y*CS,panel);
		    break;
		}else if(mapState != 1){
		    g.drawImage(effImage,i*CS,y*CS,panel);
		    map.set(i,y,4);
		}else{
		    break;
		}
	    }
	    for(int j = y; j >= y-power && j >= 0; j--){
		mapState = map.get(x,j);
		if(mapState == 2){
		    g.drawImage(effImage,x*CS,j*CS,panel);
		    break;
		}else if(mapState != 1){
		    g.drawImage(effImage,x*CS,j*CS,panel);
		    map.set(x,j,4);
		}else{
		    break;
		}
	    }
	    for(int j = y+1; j <= y+power && j <= 15; j++){
		mapState = map.get(x,j);
		if(mapState == 2){
		    g.drawImage(effImage,x*CS,j*CS,panel);
		    break;
		}else if(mapState != 1){
		    g.drawImage(effImage,x*CS,j*CS,panel);
		    map.set(x,j,4);
		}else{
		    break;
		}
	    }
	    break;
	case 2: //後処理
	    for(int i = x; i >= x-power && i >= 0; i--){
		mapState = map.get(i,y);
		if(mapState == 2){
		    map.set(i,y,0);
		    break;
		}else if(mapState != 1){
		    map.set(i,y,0);
		}else{
		    break;
		}
	    }
	    for(int i = x+1; i <= x+power && i <= 15; i++){
		mapState = map.get(i,y);
		if(mapState == 2){
		    map.set(i,y,0);
		    break;
		}else if(mapState != 1){
		    map.set(i,y,0);
		}else{
		    break;
		}
	    }
	    for(int j = y; j >= y-power && j >= 0; j--){
		mapState = map.get(x,j);
		if(mapState == 2){
		    map.set(x,j,0);
		    break;
		}else if(mapState != 1){
		    map.set(x,j,0);
		}else{
		    break;
		}
	    }
	    for(int j = y+1; j <= y+power && y <= 15; j++){
		mapState = map.get(x,j);
		if(mapState == 2){
		    map.set(x,j,0);
		    break;
		}else if(mapState != 1){
		    map.set(x,j,0);
		}else{
		    break;
		}
	    }
	    illust = 3;
	    break;
	case 3:
	    break;
	}
	
	
    }

    public void actionPerformed(ActionEvent e){
	switch(illust){
	case 0:
	    illust = 1;
	    timer.setInitialDelay(1000); //爆風は1秒で消す
	    timer.restart();
	    break;
	case 1:
	    illust = 2;
	    break;
	case 2:
	    illust = 3;
	    timer.stop();
	    break;
	}
    }
	
}
>>>>>>> master
