package model;

import dao.Dao;
import dao.DaoImplJDBC;
import exception.EmployeeNotFoundException;
import exception.InvalidPasswordException;
import main.Logable;

public class Employee extends Person implements Logable {

	public int employeeId;
	public Dao dao;
	public String password;

	public Employee(String name, int id, String password) {
		super(name);
		this.employeeId = id;
		this.password = password;
		this.dao = new DaoImplJDBC();
	}

	protected int getEmployeeId() {
		return employeeId;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public boolean login(int id_user, String password) {
		boolean logged = false;
		Employee employeeConsulta = null;
		try {
			dao.connect();
			employeeConsulta = dao.getEmployee(id_user, password);
			if (id_user == employeeConsulta.getEmployeeId()
					&& password.equalsIgnoreCase(employeeConsulta.getPassword())) {
				System.out.println("Login correcto");
				logged = true;
			}
			dao.disconnect();
		} catch (EmployeeNotFoundException | InvalidPasswordException e) {
			System.out.println("Login incorrecto");
		}
		return logged;
	}
}
