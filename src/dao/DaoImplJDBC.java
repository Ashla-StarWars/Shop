package dao;

import model.Employee;
import model.Product;
import model.ProductList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import exception.EmployeeNotFoundException;
import exception.InvalidPasswordException;

public class DaoImplJDBC implements Dao {

	private Connection connection;

	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Employee getEmployee(int employeeId, String password)
			throws EmployeeNotFoundException, InvalidPasswordException {
		Employee employee = null;
		// Prepare query
		String query = "SELECT * FROM employee WHERE employeeID = ?";

		try (PreparedStatement ps = connection.prepareStatement(query)) {
			// Set id to search for
			ps.setInt(1, employeeId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Employee found, now verify the password
					String storedPassword = rs.getString("password");
					if (storedPassword.equals(password)) {
						// Password matches, create Employee object
						employee = new Employee(rs.getString("name"), rs.getInt("employeeId"), storedPassword);
					} else {
						// Password does not match, throw exception
						throw new InvalidPasswordException("Password does not match for employee ID: " + employeeId);
					}
				} else {
					// No employee found, throw exception
					throw new EmployeeNotFoundException("No employee found with ID: " + employeeId);
				}
			}
		} catch (SQLException e) {
			// In case of an SQL error
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public void disconnect() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Failed to close the database connection.");
			}
		}

	}

	@Override
	public ProductList getInventory() {

		ProductList inventory = new ProductList();
		String query = "SELECT * FROM inventory";
		connect();
		try (PreparedStatement ps = connection.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Product item = new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("wholesalerPrice"),
							rs.getBoolean("available"), rs.getInt("stock"));
					inventory.add(item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return inventory;
	}

	@Override
	public boolean writeInventory(ProductList lista) {

		boolean isExported = false;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDate = now.format(formatter);

		// Borrar tabla antes de guardar datos
/*
		String truncateQuery = "TRUNCATE TABLE historical_inventory;";
		try (PreparedStatement ps = connection.prepareStatement(truncateQuery)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
*/
		for (Product product : lista.getProducts()) {

			String query = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, ?)";
			connect();
			try (PreparedStatement statement = connection.prepareStatement(query)) {
				statement.setInt(1, product.getId());
				statement.setString(2, product.getName());
				statement.setDouble(3, product.getWholesalerPrice().getValue());
				statement.setBoolean(4, product.isAvailable());
				statement.setInt(5, product.getStock());
				statement.setString(6, formattedDate);
				statement.executeUpdate();
				isExported = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		disconnect();
		return isExported;
	}

	@Override
	public void addProduct(Product product) {

		String query = "INSERT INTO inventory (id, name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?);";
		connect();
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, product.getId());
			statement.setString(2, product.getName());
			statement.setDouble(3, product.getWholesalerPrice().getValue());
			statement.setBoolean(4, product.isAvailable());
			statement.setInt(5, product.getStock());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	@Override
	public void updateProduct(Product product) {
		
		boolean isAvailable = false;
		if(product.getStock()>0) {
			isAvailable = true;
		}
		
	    String query = "UPDATE inventory SET stock = ? , available = ? WHERE id = ?;";
	    
		connect();
	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, product.getStock());
	        statement.setBoolean(2, isAvailable);
	        statement.setInt(3, product.getId());

	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Stock actualizado correctamente: " + product.getName());
	        } else {
	            System.out.println("No se encontró el producto: " + product.getName());
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }

	}

	@Override
	public void deleteProduct(Product product) {
	    connect();
	    String query = "DELETE FROM inventory WHERE id = ?;";

	    try (PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setInt(1, product.getId());

	        int rowsAffected = statement.executeUpdate();
	        if (rowsAffected > 0) {
	            System.out.println("Producto eliminado correctamente: " + product.getName());
	        } else {
	            System.out.println("No se encontró el producto: " + product.getName());
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }

	}

}
