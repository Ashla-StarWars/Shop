package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import exception.LimitLoginException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;

import model.Employee;

public class LoginView extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField employeeId;
	private JTextField employeePsw;
	public static int counterLoggin = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setTitle("Login");

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Número de empleado");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(87, 52, 198, 14);
		contentPane.add(lblNewLabel);

		employeeId = new JTextField();
		employeeId.setBounds(97, 79, 160, 20);
		contentPane.add(employeeId);
		employeeId.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(87, 119, 217, 14);
		contentPane.add(lblNewLabel_1);

		employeePsw = new JTextField();
		employeePsw.setColumns(10);
		employeePsw.setBounds(97, 144, 160, 20);
		contentPane.add(employeePsw);

		JButton btnNewButton = new JButton("Acceder");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loggin();
			}
		});

		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(335, 219, 89, 31);
		contentPane.add(btnNewButton);
		btnNewButton.addKeyListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		char key = e.getKeyChar();
		System.out.println(key);
		if (key == '\n') {
			loggin();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void loggin() {
		counterLoggin++;
		if (counterLoggin < 3) {

			int user_id = 0;
			try {
				user_id = Integer.parseInt(employeeId.getText());
			} catch (NumberFormatException error) {
				System.out.println("Error: El número de empleao no es un número válido.");
				employeeId.setText("");
			}

			String user_psw = employeePsw.getText();
			boolean logged = false;

			Employee employee = new Employee(null, user_id, user_psw);
			logged = employee.login(user_id, user_psw);

			if (logged) {
				System.out.println("Mostrar ventana ShopView");
				LoginView.this.setVisible(false);
				ShopView shopView = new ShopView();
				shopView.setVisible(true);
			} else {
				System.out.println("Mostrar login incorrecto como JOptionPane");
				JOptionPane.showMessageDialog(LoginView.this, "Usuario o password incorrectos", "Error",
						JOptionPane.ERROR_MESSAGE);
				employeeId.setText("");
				employeePsw.setText("");
			}
		} else {
			try {
				throw new LimitLoginException("Error loggin: superados los 3 intentos");
			} catch (LimitLoginException ex) {
				JOptionPane.showMessageDialog(LoginView.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}
}
