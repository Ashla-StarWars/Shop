package main;

import model.Amount;
import model.Client;
import model.Employee;
import model.Product;
import model.ProductList;
import model.Sale;
import utils.Constants;
import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import dao.Dao;
import dao.DaoImplFile;
import dao.DaoImplJDBC;
import dao.DaoImplJaxb;
import dao.DaoImplXml;

public class Shop {

	public Amount cash;
	public Dao dao;
	public ProductList inventory;
	private ArrayList<Sale> sales;
	private Employee employee;

	public Shop() {
		inventory = new ProductList();
		
		sales = new ArrayList<Sale>();
		cash = new Amount(10.5, Constants.AMOUNT.SYMBOL.EUR);
		employee = new Employee(null, 0, null);
		
		dao = new DaoImplJDBC();
		// dao = new DaoImplFile();
		// dao = new DaoImplXml();
		// dao = new DaoImplJaxb();
		
		this.inventory = dao.getInventory();

	}

	/**
	 * main
	 * 
	 * @throws SQLException
	 */
	public static void main(String[] args) {

		Shop shop = new Shop();
		
		shop.initSession();

		Scanner scanner = new Scanner(System.in);

		int opcion = 0;
		boolean exit = false;

		do {
			System.out.println("\n");
			System.out.println("===========================");
			System.out.println("Menu principal miTienda.com");
			System.out.println("===========================");
			System.out.println("0) Exportar inventario");
			System.out.println("1) Contar caja");
			System.out.println("2) Añadir producto");
			System.out.println("3) Añadir stock");
			System.out.println("4) Marcar producto proxima caducidad");
			System.out.println("5) Ver inventario");
			System.out.println("6) Venta");
			System.out.println("7) Ver ventas");
			System.out.println("8) Ver valor total ventas");
			System.out.println("9) Eliminar producto del Inventario");
			System.out.println("10) Salir programa");
			System.out.println("11) Ver ventas de un cliente");
			System.out.println("\n");

			System.out.print("Seleccione una opción: ");
			try {
				opcion = scanner.nextInt();
			} catch (InputMismatchException ex) {
				scanner.next();
			}
			System.out.println("\n");

			switch (opcion) {

			case 0:
				shop.writeInventory();
				break;

			case 1:
				shop.showCash();
				break;

			case 2:
				shop.addProduct();
				break;

			case 3:
				shop.addStock(shop);
				break;

			case 4:
				shop.setExpired();
				break;

			case 5:
				shop.showInventory();
				break;

			case 6:
				shop.sale();
				break;

			case 7:
				shop.showSales();
				break;

			case 8:
				shop.showValueSales();
				break;

			case 9:
				shop.deleteProduct(shop);
				break;

			case 10:
				System.out.println("Se ha cerrado correctamente");
				exit = true;
				break;

			case 11:
				shop.showClientSales();
				break;

			default:
				System.out.println("Elige una opcion del menu correcta");
				break;
			}
		} while (!exit);
	}



	/**
	 * initSession
	 * 
	 * @throws SQLException
	 */
	private void initSession() {
		boolean logged = false;
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.print("Introduzca numero de empleado: ");
			int user = scanner.nextInt();
			System.out.print("Introduzca contrasenya: ");
			String passw = scanner.next();
			logged = employee.login(user, passw);
		} while (!logged);
	}
	
	/*
	 *  export inventory
	 */
	private void writeInventory() {
		if (dao.writeInventory(inventory)) {
			System.out.println("Inventario exportando correctamente");
		} else {
			System.out.println("Error exportando el inventario");
		}
	}

	/**
	 * show current total cash
	 */
	private void showCash() {
		System.out.println("Dinero actual: " + cash.getValue() + " " + cash.getCurrency());
	}

	/**
	 * add a new product to inventory getting data from console
	 */
	private void addProduct() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Nombre: ");
		String name = scanner.nextLine();
		Product product = findProduct(name);
		if (product == null) {
			System.out.print("Precio mayorista: ");
			Amount wholesalerPrice = new Amount(scanner.nextDouble(), Constants.AMOUNT.SYMBOL.EUR);
			System.out.print("Stock: ");
			int stock = scanner.nextInt();
			
			Product newProduct = new Product(name, wholesalerPrice, true, stock);
			inventory.add(newProduct);
			dao.addProduct(newProduct);
			
			System.out.println("Producto añadido correctamente");
		} else {
			System.out.println("El producto con nombre " + name + " ya existe en el inventario");
		}
	}

	/**
	 * delete a product to inventory getting data from console
	 */
	private void deleteProduct(Shop shop) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto a eliminar: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
			inventory.remove(product);
			shop.dao.deleteProduct(product);
			System.out.println("El producto con nombre " + name + " se ha eliminado");
		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
	}

	/**
	 * add stock for a specific product
	 */
	private void addStock(Shop shop) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();
		Product product = findProduct(name);

		if (product != null) {
			// ask for stock
			System.out.print("Seleccione la cantidad a añadir: ");
			int stock = scanner.nextInt();
			// update stock product
			product.setStock(product.getStock() + stock);
			shop.dao.updateProduct(product);
			System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());

		} else {
			System.out.println("No se ha encontrado el producto con nombre " + name);
		}
	}

	/**
	 * set a product as expired
	 */
	private void setExpired() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Seleccione un nombre de producto: ");
		String name = scanner.next();

		Product product = findProduct(name);

		if (product != null) {
			product.expire();
			System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());

		}
	}

	/**
	 * show all inventory
	 */
	private void showInventory() {
		System.out.println("Contenido actual de la tienda:");
		for (Product product : inventory.getProducts()) {
			if (product != null) {
				System.out.println(product.toString());
			}
		}
	}

	/**
	 * make a sale of products to a client
	 */
	private void sale() {

		boolean paid = false;
		// ask for client name
		Scanner sc = new Scanner(System.in);
		System.out.print("Realizar venta, escribir nombre cliente: ");
		String client_name = sc.nextLine();
		Client client = new Client(client_name);
		System.out.println();

		double totalAmount = 0.0;
		Amount total;
		ArrayList<Product> venta = new ArrayList<Product>();
		Product product;
		String name = "";

		// sale product until input name is not 0
		while (!name.equals("0")) {
			System.out.print("Introduce el nombre del producto, escribir 0 para terminar: ");
			name = sc.nextLine();

			if (name.equals("0")) {
				break;
			}
			product = findProduct(name);
			boolean productAvailable = false;

			if (product != null && product.isAvailable()) {
				productAvailable = true;
				totalAmount += product.getPublicPrice().getValue();
				product.setStock(product.getStock() - 1);
				// if no more stock, set as not available to sale
				if (product.getStock() == 0) {
					product.setAvailable(false);
				}
				venta.add(product);
				System.out.println("Producto añadido con éxito");
				System.out.println();
			}

			if (!productAvailable) {
				System.out.println("Producto no encontrado o sin stock");
				System.out.println();
			}
		}
		// Obtenemos y guardamos la fecha del sistema
		Date myDate = new Date();

		// Aquí obtenemos el formato que deseamos
		String formatDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(myDate);

		// show cost total
		totalAmount = totalAmount * Constants.TAX.RATE;
		total = new Amount(totalAmount, Constants.AMOUNT.SYMBOL.EUR);

		paid = client.pay(total);

		cash.setValue(totalAmount + cash.getValue());
		System.out.println();
		System.out.println("Venta realizada con éxito, total: " + Math.round(totalAmount * 100.00) / 100.00 + " "
				+ cash.getCurrency() + " Date: " + formatDate);
		if (!paid) {
			System.out.println("Cliente debe: " + client.getBalance());
		} else {
			System.out.println("Saldo restante del cliente: " + client.getBalance());
		}

		// guardamos venta
		sales.add(new Sale(client, venta, myDate, totalAmount));
	}

	/**
	 * show all sales
	 */
	private void showSales() {

		if (sales.isEmpty()) {
			System.out.println("Lista de ventas vacia");
		} else {
			System.out.println("Lista de ventas:");
			for (Sale sale : sales) {
				if (sale != null) {
					System.out.println(sale.toString());
				}
			}
			Scanner sc = new Scanner(System.in);
			System.out.println("");
			System.out.print("Desea guardar las ventas realizadas? escribir 0 para guardar: ");
			String op = sc.nextLine();

			if (op.equals("0")) {
				// Obtenemos y guardamos la fecha del sistema
				Date myDate = new Date();

				// Aquí obtenemos el formato que deseamos
				String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(myDate);

				try {

					String rutaArchivo = "Files" + File.separator + "sales_" + formatDate + ".txt\"";
					File txt = new File(rutaArchivo);
					String rutaAbsoluta = txt.getAbsolutePath();

					PrintWriter file = new PrintWriter(rutaAbsoluta);

					for (Sale sale : sales) {
						if (sale != null) {
							formatDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sale.getDate());
							ArrayList<Product> productList = new ArrayList<Product>();
							productList = sale.getProducts();
							String list = "";

							for (Product product : productList) {
								if (product != null) {
									list = list + product.toList();
								}
							}

							file.println((sales.indexOf(sale) + 1) + ";Cliente=" + sale.getClient() + ";Date="
									+ formatDate + ";");
							file.println((sales.indexOf(sale) + 1) + ";Products=" + list);
							file.println((sales.indexOf(sale) + 1) + ";Amount=" + sale.getAmount() + ";");
						}
					}
					System.out.println("");
					System.out.println("Ventas guardadas correctamente");
					file.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * show all client sales
	 */
	private void showClientSales() {

		// ask for client name
		Scanner sc = new Scanner(System.in);
		System.out.print("Buscar ventas, escribir nombre cliente: ");
		String client_name = sc.nextLine();
		boolean encontrado = false;

		while (true) {
			if (sales.isEmpty()) {
				System.out.println("Lista de ventas vacia");
				break;
			} else {

				for (Sale sale : sales) {
					if (sale != null) {
						if (sale.getClient().getName().equalsIgnoreCase(client_name)) {
							encontrado = true;
							break;
						}
					}
				}
				if (encontrado == true) {
					if (sales.isEmpty()) {
						System.out.println("El cliente no existe");
						break;
					} else {
						System.out.println("Lista de ventas:");
						for (Sale sale : sales) {
							if (sale != null) {
								if (sale.getClient().getName().equalsIgnoreCase(client_name)) {
									System.out.println(sale);
								}
							}
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * show all value sales
	 */
	private void showValueSales() {

		if (sales.isEmpty()) {
			System.out.println("Lista de ventas vacia");
		} else {
			double res = 0;
			System.out.print("Valor de todas las ventas: ");
			for (Sale sale : sales) {
				if (sale != null) {
					res += sale.getAmount();
				}
			}
			System.out.println(Math.round(res * 100.00) / 100.00 + " " + cash.getCurrency());
		}
	}

	/**
	 * find product by name
	 * 
	 * @param product name
	 */
	public Product findProduct(String name) {

		for (Product product : inventory.getProducts()) {
			if (product.getName().equalsIgnoreCase(name)) {
				return product;
			}
		}
		return null;
	}

}