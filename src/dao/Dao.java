package dao;

import exception.EmployeeNotFoundException;
import exception.InvalidPasswordException;
import model.Employee;
import model.ProductList;

public interface Dao {

	public void connect();

	public Employee getEmployee(int employeeId, String password)
			throws EmployeeNotFoundException, InvalidPasswordException;

	public void disconnect();

	public ProductList getInventory();

	public boolean writeInventory(ProductList lista);

}
