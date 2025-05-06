package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import exception.EmployeeNotFoundException;
import exception.InvalidPasswordException;
import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;
import model.ProductList;
import utils.Constants;

public class DaoImplHibernate implements Dao {

	private Session session;
	private SessionFactory factory;

	@Override
	public void connect() {
		// Cargar la configuración de Hibernate
		factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		session = factory.openSession();
	}

	@Override
	public void disconnect() {
		if (session != null && factory != null) {
			session.close();
			factory.close();
		}
	}

	@Override
	public Employee getEmployee(int employeeId, String password)
			throws EmployeeNotFoundException, InvalidPasswordException {
		// Implementación para obtener un empleado.
		// Aquí, sería necesario implementar el manejo de la excepción.
		return null; // TODO: Implementar lógica.
	}

	@Override
	public ProductList getInventory() {

		ProductList productList = new ProductList();
		try {
			if (session == null || !session.isOpen())
				connect();

			session.beginTransaction();

			Query<Product> query = session.createQuery("FROM Product", Product.class);
			List<Product> productListFromDb = query.list();

			for (Product product : productListFromDb) {
				product.setPublicPrice(new Amount(product.getPrice(), Constants.AMOUNT.SYMBOL.EUR));
				product.setWholesalerPrice(new Amount(product.getPrice(), Constants.AMOUNT.SYMBOL.EUR));
			}

			productList.setProducts(new ArrayList<>(productListFromDb));
			session.getTransaction().commit();

		} catch (Exception e) {
			if (session != null && session.getTransaction() != null)
				session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return productList;
	}

	@Override
	public boolean writeInventory(ProductList lista) {
		Boolean isLoad = false;
	    try {
	        connect();
	        session.beginTransaction();
	        
	        for (Product product : lista.getProducts()) {
	            // 1. Actualizar el inventario actual
	            session.saveOrUpdate(product);
	            
	            // 2. Crear el historial de inventario
	            ProductHistory history = new ProductHistory();
	            history.setProductId(product.getId());
	            history.setName(product.getName());
	            history.setWholesalerPrice(product.getWholesalerPrice().getValue());
	            history.setAvailable(product.isAvailable() ? 1 : 0); // Convertir boolean a int si es necesario
	            history.setStock(product.getStock());
	            history.setCreatedAt(new Date()); // Fecha y hora actual

	            // 3. Guardar el historial
	            session.save(history);
	        }

	        session.getTransaction().commit();
	        isLoad = true;
	    } catch (Exception e) {
	        if (session.getTransaction() != null) {
	            session.getTransaction().rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        disconnect();
	    }
	    return isLoad;
	}

	@Override
	public void addProduct(Product item) {
		try {
			connect();
			session.beginTransaction();
			// Crear un producto de prueba
			Product product = new Product();
			System.out.println(item.getId());
			product.setName(item.getName());
			product.setPrice(item.getWholesalerPrice().getValue());
			product.setStock(item.getStock());
			if (item.getStock() > 0) {
				product.setAvailable(true);
			} else {
				product.setAvailable(false);
			}

			// Guardar el producto en la base de datos
			session.save(product); // Guarda el nuevo producto
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	@Override
	public void updateProduct(Product item) {
		try {
			connect();
			session.beginTransaction();
			session.update(item); // Actualiza el producto existente
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	@Override
	public void deleteProduct(Product item) {
		try {
			connect();
			session.beginTransaction();
			session.delete(item); // Elimina el producto
			session.getTransaction().commit();
		} catch (Exception e) {
			if (session.getTransaction() != null) {
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

}
