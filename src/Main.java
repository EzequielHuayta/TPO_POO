import view.LoginView;
import view.MainFrame;

/**
 * Clase principal. Responsable de ejecutar la aplicación desde el Login.
 */
public class Main {
	public static void main(String[] args) {
		MainFrame frame = MainFrame.getInstance();
		frame.addPanel(new LoginView(), "loginView");
		frame.setVisible(true);
	}
}
