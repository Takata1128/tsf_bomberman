import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Main extends JFrame{
	public Main(boolean is_server){
		setTitle("bomberman");
<<<<<<< HEAD

		MainPanel panel = new MainPanel(is_server);
=======
		MainPanel panel = new MainPanel();
>>>>>>> master
		Container contentPane = getContentPane();
		contentPane.add(panel);
		pack();
	}

	public static void main(String argv[]){
		boolean is_server;
		System.out.println(argv[0]);
		if(argv[0].equals("s")){
			is_server = true;
		}else{
			is_server = false;
		}
		System.out.println(is_server);
		Main frame = new Main(is_server);
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
