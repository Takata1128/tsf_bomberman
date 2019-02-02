import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Main extends JFrame {
	public Main(/*boolean is_server*/) {
		setTitle("bomberman");

		MainPanel panel = new MainPanel();
		Container contentPane = getContentPane();
		//panel.is_server = is_server;
		contentPane.add(panel);
		pack();
	}

	public static void main(String argv[]) {
		Main frame = new Main(/*is_server*/);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void change(JPanel panel) {
		getContentPane().removeAll();
		super.add(panel);
		validate();
		repaint();
	}
}
