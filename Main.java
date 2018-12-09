import java.awt.Container;
import javax.swing.JFrame;

class Main extends JFrame{
	public Main(){
		setTitle("bomberman");

		MainPanel panel = new MainPanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
		pack();
	}

	public static void main(String argv[]){
		Main frame = new Main();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
