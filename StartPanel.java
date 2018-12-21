import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

class StartPanel extends JPanel implements Observer,Common{
    private KeyController controller;
    private MainPanel mp;

    public StartPanel(MainPanel mp){
        controller = new KeyController();
        controller.addObserver(this);
        this.mp = mp;
        setLayout(null);
        setBackground(Color.white);
        addKeyListener(controller);
        setFocusable(true);
    }

    public void paintComponent(Graphics g){

    }

    public void update(Observable o,Object arg){
        int state = controller.getState();
        switch(state){
            case UP:
                System.out.print("yes");
                mp.setstate(1);
                break;
        }
    }
}
