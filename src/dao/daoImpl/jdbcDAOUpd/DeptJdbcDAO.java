package dao.daoImpl.jdbcDAOUpd;

import corporationmodules.Department;
import dao.daoImpl.jsonDaoUpd.EventJsonDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeptJdbcDAO extends BaseModuleJdbcDAO<Department> {

    private static final String GET_ALL_DEPARTMENTS = "SELECT deptId, deptName FROM departments";
    private static final String DROP_TABLE = "DROP TABLE departments";
    private static final String CREATE_TABLE = "CREATE TABLE departments (deptId VARCHAR2(60), deptName VARCHAR2(20))";
    private static final String ADD_DEPARTMENT = "INSERT INTO departments (deptId, deptName) VALUES (?, ?)";
    private static final String COLUMN_DEPARTMENT_ID = "deptId";
    private static final String COLUMN_DEPARTMENT_NAME = "deptName";
    private static final String TABLE_NAME = "DEPARTMENTS";

    protected static boolean changed = false;

    private static DeptJdbcDAO instance;

    public static DeptJdbcDAO getInstance() {
        if (instance == null) {
            instance = new DeptJdbcDAO();
            return instance;
        }

        return instance;
    }

    private DeptJdbcDAO() {
        super(CREATE_TABLE, DROP_TABLE, ADD_DEPARTMENT, getDepartmentsFromDB());
    }

    protected static List<Department> getDepartmentsFromDB() {

        createTableIfNotExists(TABLE_NAME, CREATE_TABLE);

        List<Department> departments = new ArrayList<>();
        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(GET_ALL_DEPARTMENTS);

            while(rs.next()) {
                departments.add(new Department(UUID.fromString(rs.getString(COLUMN_DEPARTMENT_ID)),
                                                rs.getString(COLUMN_DEPARTMENT_NAME)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departments;
    }

    @Override
    public void add(Department module) {
        super.add(module);

        changed = true;
    }

    @Override
    public void delete(Department dept) {
        UUID deptId = dept.getId();

        for(int i = 0; i < moduleCash.size(); i++) {
            if(moduleCash.get(i).getId().equals(deptId)) {
                moduleCash.remove(i);

                //delete from deptIds
                DeptEmpJdbcDAO.getInstance().deleteKey(deptId);

                //add event
                EventJsonDAO.getInstance().addModuleEvent(dept, "Department is removed: ");

                changed = true;
            }
        }
    }
}
