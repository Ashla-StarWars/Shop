package dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import exception.EmployeeNotFoundException;
import exception.InvalidPasswordException;
import model.Amount;
import model.Employee;
import model.Product;
import model.ProductList;

public class DaoImplMongoDB implements Dao {

	private MongoClient mongoClient;
	private MongoDatabase database;

	@Override
	public void connect() {
		String uri = "mongodb://localhost:27017";
		mongoClient = MongoClients.create(uri);
		database = mongoClient.getDatabase("shop");
		System.out.println("Conexión exitosa a MongoDB");
	}

	@Override
	public Employee getEmployee(int employeeId, String password)
			throws EmployeeNotFoundException, InvalidPasswordException {
		connect();
		Employee employee = null;

		try {
			connect();
			MongoCollection<Document> collection = database.getCollection("users");

			// Buscar al empleado por employeeId
			Document doc = collection.find(Filters.eq("employeeId", employeeId)).first();

			if (doc == null) {
				throw new EmployeeNotFoundException("Empleado no encontrado con ID: " + employeeId);
			}

			// Verificar si la contraseña coincide
			String storedPassword = doc.getString("password");
			if (!storedPassword.equals(password)) {
				throw new InvalidPasswordException("Contraseña incorrecta para el empleado con ID: " + employeeId);
			}

			// Crear y devolver el objeto Employee
			employee = new Employee(doc.getString("name"), doc.getInteger("employeeId"), doc.getString("password"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return employee;
	}

	@Override
	public void disconnect() {
		if (mongoClient != null) {
			mongoClient.close();
			System.out.println("Desconectado de MongoDB");
		}
	}

	@Override
	public ProductList getInventory() {
		ProductList productList = new ProductList();
		try {
			connect();

			MongoCollection<Document> collection = database.getCollection("inventory");

			// Leer documentos
			try (MongoCursor<Document> cursor = collection.find().iterator()) {
				while (cursor.hasNext()) {
					Document doc = cursor.next();
					int id = doc.getInteger("id");
					String name = doc.getString("name");
	                Document wholesalerPriceDoc = doc.get("wholesalerPrice", Document.class);
	                double priceValue = wholesalerPriceDoc.getDouble("value");
	                String priceCurrency = wholesalerPriceDoc.getString("currency");
					int stock = doc.getInteger("stock");
					boolean available = false;
					if (stock > 0)
						available = true;
					Product product = new Product(id, name, new Amount(priceValue, priceCurrency), available, stock);
					productList.add(product);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return productList;
	}

	@Override
	public boolean writeInventory(ProductList lista) {
		boolean success = false;
		try {
			connect();
			MongoCollection<Document> collection = database.getCollection("historical_inventory");

			List<Document> documents = new ArrayList<>();
			LocalDateTime now = LocalDateTime.now();
			String createdAt = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			// Recorrer la lista de productos y crear los documentos
			for (Product product : lista.getProducts()) {
				Document wholesalerPriceDoc = new Document("value", product.getWholesalerPrice().getValue())
                        .append("currency", product.getWholesalerPrice().getCurrency());
				Document doc = new Document("id", product.getId())
						.append("name", product.getName())
						.append("wholesalerPrice", wholesalerPriceDoc)
						.append("available", product.isAvailable()).append("stock", product.getStock())
						.append("created_at", createdAt);
				documents.add(doc);
			}

			// Insertar todos los documentos en una sola operación
			if (!documents.isEmpty()) {
				collection.insertMany(documents);
				success = true;
				System.out.println("Inventario histórico guardado correctamente.");
			} else {
				System.out.println("La lista de productos está vacía, no se insertó nada.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return success;
	}

	@Override
	public void addProduct(Product item) {
		try {
			connect();
			MongoCollection<Document> collection = database.getCollection("inventory");

	        // Crear un Document a partir del objeto Product
	        Document priceDoc = new Document("value", item.getWholesalerPrice().getValue())
	                            .append("currency", item.getWholesalerPrice().getCurrency());
			// Crear un Document a partir del objeto Product
			Document doc = new Document("id", item.getId())
					.append("name", item.getName())
					.append("wholesalerPrice", priceDoc)
					.append("available", item.isAvailable())
					.append("stock", item.getStock());

			// Insertar el documento en la colección
			collection.insertOne(doc);
			System.out.println("Producto agregado: " + item.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	@Override
	public void updateProduct(Product item) {
		try {
			connect();
			MongoCollection<Document> collection = database.getCollection("inventory");

			// Crear un filtro para encontrar el producto por nombre
			Document filter = new Document("name", item.getName());
			
	        // Crear un Document a partir del objeto Product
	        Document priceDoc = new Document("value", item.getWholesalerPrice().getValue())
	                            .append("currency", item.getWholesalerPrice().getCurrency());

			// Crear el documento con los nuevos valores
			Document update = new Document("$set",
					new Document("wholesalerPrice", priceDoc)
					.append("stock", item.getStock()));

			// Realizar la actualización
			collection.updateOne(filter, update);
			System.out.println("Producto actualizado: " + item.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	@Override
	public void deleteProduct(Product item) {
		try {
			connect();
			MongoCollection<Document> collection = database.getCollection("inventory");

			// Crear un filtro para encontrar el producto por nombre
			Document filter = new Document("name", item.getName());

			// Eliminar el producto que coincida con el filtro
			collection.deleteOne(filter);
			System.out.println("Producto eliminado: " + item.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

}
