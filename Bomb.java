import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Bomb implements Common, ActionListener {
	private int power;// 火力
	private int x, y;// 座標
	private Image bombImage, effImage; // 爆弾、爆風の画像
	private static int resttime = 3;// 爆発までの時間
	private int isSet;
	private boolean panatrate;
	private GamePanel panel;// パネル
	private javax.swing.Timer timer;
	private int illust = 0; // 0 = bomb, 1 = eff;
	private Map map;
	private int mapState; // マップの状態
	private int velocity = 0;

	public Bomb(int x, int y, Image image1, Image image2, int power, GamePanel panel, Map map, boolean p) {
		this.x = x;
		this.y = y;
		this.bombImage = image1;
		this.effImage = image2;
		this.power = power;
		this.panel = panel;
		this.map = map;
		panatrate = p;
		isSet = 0;
		timer = new javax.swing.Timer(resttime * 1000, this);
		timer.start();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int pow) {
		power = pow;
	}

	public int checkBomb() {
		return isSet;
	}

	public void move() {
		// もろもろの計算処理

	}

	// mapState 0:床 1:壊せない壁 2:壊せる壁 3:床＋爆弾 4:床＋爆風 5:床＋アイテム
	public void draw(Graphics g) {
		// 描画処理
		if (map.effHit(x, y) == true && illust == 0) { // 爆弾が爆風に当たったら
			illust = 1;
		}
		switch (illust) {
		case 0: // 爆弾
			// isSet = 1;
			g.drawImage(bombImage, x * CS, y * CS, panel);
			// velocity++;
			break;
		case 1: // 爆風
			// isSet = 2;

			draw_eff1(x, y, g);
			break;
		case 2: // 後処理
			// isSet = 0;
			draw_eff2(x, y);
			break;
		case 3:
			break;
		}

	}

	private void draw_eff1(int x, int y, Graphics g) {

		for (int i = x; i >= x - power && i >= 0; i--) {
			mapState = map.get(i, y);
			if (mapState == 2) {
				g.drawImage(effImage, i * CS, y * CS, panel);
				// map.set(i,y,5);
				if (panatrate == false) {
					break;
				}
				/*
				 * }else if(mapState == 5){ g.drawImage(effImage,i*CS,y*CS,panel);
				 */
			} else if (mapState != 1) {
				g.drawImage(effImage, i * CS, y * CS, panel);
				map.set(i, y, 4);
			} else {
				break;
			}
		}
		for (int i = x + 1; i <= x + power && i <= 15; i++) {

			mapState = map.get(i, y);
			if (mapState == 2) {
				g.drawImage(effImage, i * CS, y * CS, panel);
				if (panatrate == false) {
					break;
				}
				/*
				 * }else if(mapState == 5){ g.drawImage(effImage,i*CS,y*CS,panel);
				 */
			} else if (mapState != 1) {
				g.drawImage(effImage, i * CS, y * CS, panel);
				map.set(i, y, 4);
			} else {
				break;
			}
		}
		for (int j = y; j >= y - power && j >= 0; j--) {
			mapState = map.get(x, j);
			if (mapState == 2) {
				g.drawImage(effImage, x * CS, j * CS, panel);
				if (panatrate == false) {
					break;
				}
				/*
				 * }else if(mapState == 5){ g.drawImage(effImage,x*CS,j*CS,panel);
				 * //map.set(x,j,4);
				 */
			} else if (mapState != 1) {
				g.drawImage(effImage, x * CS, j * CS, panel);
				map.set(x, j, 4);
			} else {
				break;
			}
		}
		for (int j = y + 1; j <= y + power && j <= 15; j++) {
			mapState = map.get(x, j);
			if (mapState == 2) {
				g.drawImage(effImage, x * CS, j * CS, panel);
				if (panatrate == false) {
					break;
				}
				/*
				 * }else if(mapState == 5){ g.drawImage(effImage,x*CS,j*CS,panel);
				 * //map.set(x,j,4);
				 */
			} else if (mapState != 1) {
				g.drawImage(effImage, x * CS, j * CS, panel);
				map.set(x, j, 4);
			} else {
				break;
			}
		}

	}

	private void draw_eff2(int x, int y) {
		for (int i = x; i >= x - power && i >= 0; i--) {
			mapState = map.get(i, y);
			if (mapState == 2) {
				map.set(i, y, 5);
				if (panatrate == false) {
					break;
				}
			} else if (mapState != 1 /* && mapState != 5 */) {
				map.set(i, y, 0);
			} else {
				break;
			}
		}
		for (int i = x + 1; i <= x + power && i <= 15; i++) {
			mapState = map.get(i, y);
			if (mapState == 2) {
				map.set(i, y, 5);
				if (panatrate == false) {
					break;
				}
			} else if (mapState != 1 /* && mapState != 5 */) {
				map.set(i, y, 0);
			} else {
				break;
			}
		}
		for (int j = y; j >= y - power && j >= 0; j--) {
			mapState = map.get(x, j);
			if (mapState == 2) {
				map.set(x, j, 5);
				if (panatrate == false) {
					break;
				}
			} else if (mapState != 1 /* && mapState != 5 */) {
				map.set(x, j, 0);
			} else {
				break;
			}
		}
		for (int j = y + 1; j <= y + power && y <= 15; j++) {
			mapState = map.get(x, j);
			if (mapState == 2) {
				map.set(x, j, 5);
				if (panatrate == false) {
					break;
				}
			} else if (mapState != 1 /* && mapState != 5 */) {
				map.set(x, j, 0);
			} else {
				break;
			}
		}
		illust = 3;

	}

	public void actionPerformed(ActionEvent e) {
		switch (illust) {
		case 0:
			System.out.println("BOMB!");
			isSet = 2;
			illust = 1;
			timer.setInitialDelay(200); // 爆風は0.5秒で消す
			timer.restart();
			break;
		case 1:
			// isSet = 2;
			illust = 2;
			break;
		case 2:
			// isSet = 0;
			illust = 3;
			timer.stop();
			break;
		}
	}

}
