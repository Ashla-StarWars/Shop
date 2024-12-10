package dao;

import exception.EmployeeNotFoundException;
import exception.InvalidPasswordException;
import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;
import model.ProductList;

public class DaoImplJaxb implements Dao {

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
		return new JaxbUnMarshaller().init();
	}

	@Override
	public boolean writeInventory(ProductList lista) {
		return new JaxbMarshaller().init(lista);
	}

	@Override
	public void addProduct(Product item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduct(Product item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(Product item) {
		// TODO Auto-generated method stub
		
	}

}
