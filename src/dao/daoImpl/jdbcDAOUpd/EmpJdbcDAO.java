package dao.daoImpl.jdbcDAOUpd;

import corporationmodules.collaborators.Customer;
import corporationmodules.collaborators.Director;
import corporationmodules.collaborators.Employee;
import corporationmodules.collaborators.Worker;
import corporationmodules.Module;
import dao.daoImpl.jsonDaoUpd.EventJsonDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmpJdbcDAO extends BaseModuleJdbcDAO<Employee> {

    private static final String COLUMN_EMPLOYEE_NAME = "empName";
    private static final String COLUMN_EMPLOYEE_ID = "empId";
    private static final String TABLE_NAME = "EMPLOYEES";
    private static final String COLUMN_EMPLOYEE_POSITION = "empPosition";
    private static final String GET_ALL_EMPLOYEES = "SELECT empId, empName, empPosition FROM employees";
    private static final String DROP_TABLE = "DROP TABLE employees";
    private static final String CREATE_TABLE = "CREATE TABLE employees (empId VARCHAR2(60), " +
            "                                   empName VARCHAR2(20), empPosition VARCHAR2(20))";
    private static final String ADD_EMPLOYEE = "INSERT INTO employees (empId, empName, empPosition) VALUES (?, ?, ?)";

    protected static boolean changed = false;

    private static EmpJdbcDAO instance;

    public static EmpJdbcDAO getInstance() {
        if (instance == null) {
            instance = new EmpJdbcDAO();
            return instance;
        }

        return instance;
    }

    private EmpJdbcDAO() {
        super(CREATE_TABLE, DROP_TABLE, ADD_EMPLOYEE, getEmployeesFromDB());
    }

    @Override
    public void add(Employee module) {
        super.add(module);

        changed = true;
    }

    @Override
    public void delete(Employee emp) {

        UUID empId = emp.getId();
        for(int i = 0; i < moduleCash.size(); i++) {
            if(moduleCash.get(i).getId().equals(empId)) {
                moduleCash.remove(i);

                EmpProjJdbcDAO.getInstance().deleteKey(empId);
                DeptEmpJdbcDAO.getInstance().deleteValue(empId);

                EventJsonDAO.getInstance().addModuleEvent(emp, "Employee is removed: ");

                changed = true;
            }
        }
    }

    @Override
    protected void writeAllModules() {
        reCreateTable();

        try(PreparedStatement stmt = conn.prepareStatement(ADD_EMPLOYEE)) {

            for(Module module: moduleCash) {
                stmt.setString(1, module.getId().toString());
                stmt.setString(2, module.getName());
                stmt.setString(3, module.getClass().getSimpleName());
                stmt.execute();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected static List<Employee> getEmployeesFromDB() {

        createTableIfNotExists(TABLE_NAME, CREATE_TABLE);

        List<Employee> employees = new ArrayList<>();
        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(GET_ALL_EMPLOYEES);

            while(rs.next()) {
                employees.add(createEmployee(rs.getString(COLUMN_EMPLOYEE_ID),
                        rs.getString(COLUMN_EMPLOYEE_NAME),
                        rs.getString(COLUMN_EMPLOYEE_POSITION)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    private static Employee createEmployee(String id, String name, String position) {

        switch (position) {
            case "Worker":
                return new Worker(UUID.fromString(id), name);
            case "Director":
                return new Director(UUID.fromString(id), name);
            case "Customer":
                return new Customer(UUID.fromString(id), name);
            default:
                return null;
        }
    }
}
