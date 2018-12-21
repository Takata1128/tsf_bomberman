import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.util.Observer;

class StartPanel extends JPanel implements Observer{
    private KeyController controller;
    private MainPanel mp;

    public StartPanel(MainPanel mp){
        controller = new KeyController();
        controller.addObserver(this);
        this.mp = mp;
    }

    public void paintComponent(Graphics g){

    }

    public update(Observable o,Object arg){
        int state = controller.getState();
    }
}
