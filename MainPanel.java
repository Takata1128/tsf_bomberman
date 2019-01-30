import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.KeyListener;

class MainPanel extends JPanel implements Runnable, Common {
  // パネルサイズ
  private static final int WIDTH = 480;
  private static final int HEIGHT = 480;

  private Thread mainLoop;
  private StartPanel sp;
  private GamePanel gp;
  private ResultPanel rp;
  private int state;
  private int oldState;
  public  boolean is_server;

  public MainPanel() {
    // パネルの推奨サイズを設定
    setLayout(new GridLayout(1, 1, 0, 0));
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    sp = new StartPanel(this);
    this.add(sp);
    state = TITLE_SCENE;
    oldState = TITLE_SCENE;
    mainLoop = new Thread(this);
    mainLoop.start();
  }

  public void run() {
    while (true) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (state != oldState) {
        if (oldState == TITLE_SCENE) {
          remove(sp);
          System.out.println("StartPanel was removed.");
        } else if (oldState == MULTI_GAME_SCENE||oldState== SINGLE_GAME_SCENE) {
          gp.removeAll();
          removeAll();
          System.out.println("GamePanel was removed.");
        } else if (oldState == RESULT_WIN||oldState == RESULT_LOSE) {
          remove(rp);
          System.out.println("ResultPanel was removed.");
        }
        if (state == TITLE_SCENE) {//タイトルシーン
          sp = new StartPanel(this);
          this.add(sp);
          sp.requestFocus();
          System.out.println("StartPanel was added.");
        } else if (state == MULTI_GAME_SCENE) {//マルチゲームシーン
          gp = new MultiGamePanel(this);
          this.add(gp);
          gp.requestFocus();
          System.out.println("GamePanel was added.");
        } else if (state == SINGLE_GAME_SCENE){//シングルゲームシーン
          gp = new SingleGamePanel(this);
          this.add(gp);
          gp.requestFocus();
          System.out.println("GamePanel was added.");
        } else if (state == RESULT_WIN) {//勝利シーン
          rp = new ResultPanel(this,WIN);
          this.add(rp);
          rp.requestFocus();
          System.out.println("ResultPanel was added.");
        } else if(state == RESULT_LOSE){//敗北シーン
          rp = new ResultPanel(this,LOSE);
          this.add(rp);
          rp.requestFocus();
          System.out.println("ResultPanel was added.");
        }
        validate();
        oldState = state;
      }
    }
  }

  public void setstate(int s) {
    state = s;
  }
}
