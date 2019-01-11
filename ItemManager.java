import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;


class ItemManager implements Common{
    private int max = 100;
    private int num = 0;
    private int setMax = 0;
    private Item[] item = new Item[max];
    private int[] itemEff = new int[max];
    private Image image1,image2,image3,image4,image5,image6;
    private GamePanel panel;
    private Map map;
   
    
    public ItemManager(Map map,GamePanel panel){
	loadImage();
	this.map = map;
	this.panel = panel;
    }

    private void loadImage(){
	ImageIcon icon = new ImageIcon(getClass().getResource("image/item1.png"));
	image1 = icon.getImage();
	
	icon = new ImageIcon(getClass().getResource("image/item1_b.png"));
	image2 = icon.getImage();

	icon = new ImageIcon(getClass().getResource("image/item2.png"));
	image3 = icon.getImage();

	icon = new ImageIcon(getClass().getResource("image/item2_b.png"));
	image4 = icon.getImage();

	icon = new ImageIcon(getClass().getResource("image/item3.png"));
	image5 = icon.getImage();

	icon = new ImageIcon(getClass().getResource("image/item3_b.png"));
	image6 = icon.getImage();
	
	
    }

    public void set(int x, int y,double r){
	if(num < max){
	    if(r >= 0.7){
		item[num] = new Item(x,y,image1,panel,map,num);
		itemEff[num] = 1;
	    }else if(r >= 0.6 && r < 0.7){
		item[num] = new Item(x,y,image2,panel,map,num);
		itemEff[num] = -1;
	    }else if(r >= 0.3 && r < 0.6){
		item[num] = new Item(x,y,image3,panel,map,num);
		itemEff[num] = 2;
	    }else if(r >= 0.2 && r < 0.3){
		item[num] = new Item(x,y,image4,panel,map,num);
		itemEff[num] = -2;
	    }else if(r >= 0.1 && r < 0.2){
		item[num] = new Item(x,y,image5,panel,map,num);
		itemEff[num] = 3;
	    }else if(r < 0.1){
		item[num] = new Item(x,y,image6,panel,map,num);
		itemEff[num] = -3;
	    }
	    num++;
	}
    }
    
    public void resetNum(){
	setMax = num;
	num = 0;
    }

    public void drawItem(int x, int y, Graphics g){
	for(int i = 0; i < setMax; i++){
	    if(item[i].check(x,y) == true){
		item[i].draw(g);
	    }
	}
    }

    public int getItemEff(int x, int y){
	for(int i = 0; i < setMax; i++){
	    if(item[i].check(x,y) == true){
		return itemEff[i];
	    }
	}
	return 0;
    }

    public void stopEff(int x, int y){
	for(int i = 0; i < setMax; i++){
	    if(item[i].check(x,y) == true){
		itemEff[i] = 0;
	    }
	}
    }
	    
}

