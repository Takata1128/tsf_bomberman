import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Main extends JFrame{
	public Main(){
		setTitle("bomberman");

		MainPanel panel = new MainPanel(this);
		Container contentPane = getContentPane();
		contentPane.add(panel);
		pack();
	}

	public static void main(String argv[]){
		Main frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void change(JPanel panel){
		getContentPane().removeAll();
		super.add(panel);
		validate();
		repaint();
	}
}
