package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;
import utils.Constants;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ShopView extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Shop shop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShopView frame = new ShopView();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws SQLException
	 */
	public ShopView() {

		shop = new Shop();
		Shop.inventory = shop.dao.getInventory();

		setTitle("MiTienda.com - Menu Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 455, 382);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 15));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Seleccione o pulse una opción:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(39, 29, 262, 20);
		contentPane.add(lblNewLabel);

		// OPCION o BOTON 0
		JButton option_0_Boton = new JButton("0. Exportar inventario");
		option_0_Boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				writeInventory(shop);
			}
		});
		option_0_Boton.setHorizontalAlignment(SwingConstants.LEFT);
		option_0_Boton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		option_0_Boton.setBounds(55, 60, 262, 36);
		contentPane.add(option_0_Boton);

		// OPCION o BOTON 1
		JButton option_1_Boton = new JButton("1. Contar caja");
		option_1_Boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openCashView(shop);
			}
		});
		option_1_Boton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		option_1_Boton.setHorizontalAlignment(SwingConstants.LEFT);
		option_1_Boton.setBounds(55, 108, 262, 36);
		contentPane.add(option_1_Boton);
		option_1_Boton.addKeyListener(this);

		// OPCION o BOTON 2
		JButton option_2_Boton = new JButton("2. Añadir producto");
		option_2_Boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openProductView(shop, Constants.OPTION.ADD_PRODUCT);
			}
		});
		option_2_Boton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		option_2_Boton.setHorizontalAlignment(SwingConstants.LEFT);
		option_2_Boton.setBounds(55, 155, 262, 36);
		contentPane.add(option_2_Boton);
		option_2_Boton.addKeyListener(this);

		// OPCION o BOTON 3
		JButton option_3_Boton = new JButton("3. Añadir stock");
		option_3_Boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openProductView(shop, Constants.OPTION.ADD_STOCK);

			}
		});
		option_3_Boton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		option_3_Boton.setHorizontalAlignment(SwingConstants.LEFT);
		option_3_Boton.setBounds(55, 202, 262, 36);
		contentPane.add(option_3_Boton);
		option_3_Boton.addKeyListener(this);

		// OPCION o BOTON 5
		JButton option_4_Boton = new JButton("5. Mostrar inventario");
		option_4_Boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openInventoryView(shop);
			}
		});
		option_4_Boton.setHorizontalAlignment(SwingConstants.LEFT);
		option_4_Boton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		option_4_Boton.setBounds(55, 249, 262, 36);
		contentPane.add(option_4_Boton);
		option_4_Boton.addKeyListener(this);

		// OPCION o BOTON 9
		JButton option_9_Boton = new JButton("9. Eliminar producto");
		option_9_Boton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openProductView(shop, Constants.OPTION.DELETE_PRODUCT);
			}
		});
		option_9_Boton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		option_9_Boton.setHorizontalAlignment(SwingConstants.LEFT);
		option_9_Boton.setBounds(55, 296, 262, 36);
		contentPane.add(option_9_Boton);
		option_9_Boton.addKeyListener(this);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {

		char key = e.getKeyChar();

		switch (key) {

		case '0':
			writeInventory(shop);
			break;

		case '1':
			openCashView(shop);
			break;

		case '2':
			openProductView(shop, Constants.OPTION.ADD_PRODUCT);
			break;

		case '3':
			openProductView(shop, Constants.OPTION.ADD_STOCK);
			break;

		case '5':
			openInventoryView(shop);
			break;

		case '9':
			openProductView(shop, Constants.OPTION.DELETE_PRODUCT);
			break;

		default:
			// No hacer nada si se presiona una tecla no asociada a una opción del menú
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void openCashView(Shop shop) {
		CashView cashView = new CashView(shop);
		cashView.setVisible(true);
	}

	public void openProductView(Shop shop, int option) {
		ProductView productView = new ProductView(shop, option);
		productView.setVisible(true);
	}

	public void openInventoryView(Shop shop) {
		InventoryView inventoryView = new InventoryView(shop);
		inventoryView.setVisible(true);
	}

	public void writeInventory(Shop shop) {
		if (shop.dao.writeInventory(Shop.inventory)) {
			JOptionPane.showMessageDialog(null, "Inventario exportado correctamente", "Información",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(ShopView.this, "Error exportando el inventario", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
