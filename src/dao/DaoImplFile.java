package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import exception.EmployeeNotFoundException;
import exception.InvalidPasswordException;
import model.Amount;
import model.Employee;
import model.Product;
import model.ProductList;
import utils.Constants;

public class DaoImplFile implements Dao {

	public ProductList inventory;

	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee getEmployee(int employeeId, String password)
			throws EmployeeNotFoundException, InvalidPasswordException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public ProductList getInventory() {
		inventory = new ProductList();
		try {

			String rutaArchivo = "Files" + File.separator + "inputInventory.txt";
			File txt = new File(rutaArchivo);
			String rutaAbsoluta = txt.getAbsolutePath();

			File archivo = new File(rutaAbsoluta);

			if (archivo.canRead()) {
				FileReader fr = null;
				fr = new FileReader(archivo);
				BufferedReader br = new BufferedReader(fr);
				String linea = br.readLine();

				while (linea != null) {
					String[] parts = linea.split(";");
					String[] productName = parts[0].split(":");
					String[] productPrice = parts[1].split(":");
					String[] productStock = parts[2].split(":");
					inventory.add(new Product(productName[1],
							new Amount(Double.parseDouble(productPrice[1]), Constants.AMOUNT_SYMBOL), true,
							Integer.parseInt(productStock[1])));

					linea = br.readLine();
				}
				br.close();
			}
			return inventory;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean writeInventory(ProductList lista) {
		// Obtenemos y guardamos la fecha del sistema
		Date myDate = new Date();

		// Aquí obtenemos el formato que deseamos
		String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(myDate);

		try {

			String rutaArchivo = "Files" + File.separator + "inventory_" + formatDate + ".txt";
			File txt = new File(rutaArchivo);
			String rutaAbsoluta = txt.getAbsolutePath();

			PrintWriter file = new PrintWriter(rutaAbsoluta);
			int i = 1;
			for (Product product : lista.getProducts()) {
				if (product != null) {
					file.println(i + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";");
					i++;
				}
			}
			file.println("Numero total de productos:" + (i - 1) + ";");
			file.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
