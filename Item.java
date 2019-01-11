import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;


class Item implements Common{
    private int x, y;
    private Image image;
    private GamePanel panel;
    private Map map;
    private int itemNum;

    public Item(int x, int y, Image image, GamePanel panel, Map map,int itemNum){
	this.x = x;
	this.y = y;
	this.image = image;
	this.panel = panel;
	this.map = map;
	this.itemNum = itemNum;

    }

    public boolean check(int x, int y){
	if(this.x == x && this.y == y){
	    return true;
	}
	return false;
    }

    public void draw(Graphics g){
	g.drawImage(image,x*CS,y*CS,panel);
    }

}
