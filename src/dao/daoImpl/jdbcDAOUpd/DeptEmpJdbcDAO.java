package dao.daoImpl.jdbcDAOUpd;

import java.util.UUID;

public class DeptEmpJdbcDAO extends BaseIdJdbcDAO {

    private static final String GET_ALL_ENTRIES = "SELECT deptId, empId FROM deptEmpIds";
    private static final String ADD_ENTRY = "INSERT INTO deptEmpIds (deptId, empId) VALUES (?, ?)";
    private static final String DROP_TABLE = "DROP TABLE deptEmpIds";
    private static final String CREATE_TABLE = "CREATE TABLE deptEmpIds (deptId VARCHAR2(60), empId VARCHAR(60))";
    private static final String COLUMN_DEPARTMENT_ID = "deptId";
    private static final String COLUMN_EMPLOYEE_ID = "empId";
    private static final String TABLE_NAME = "DEPTEMPIDS";

    protected static boolean changed = false;

    private static DeptEmpJdbcDAO instance;

    protected DeptEmpJdbcDAO() {
        super(CREATE_TABLE, DROP_TABLE, ADD_ENTRY, GET_ALL_ENTRIES, COLUMN_DEPARTMENT_ID, COLUMN_EMPLOYEE_ID, TABLE_NAME);
    }

    public static DeptEmpJdbcDAO getInstance() {
        if(instance == null) {
            instance = new DeptEmpJdbcDAO();
            return instance;
        }

        return instance;
    }

    @Override
    public void addValue(UUID keyId, UUID valueId) {
        super.addValue(keyId, valueId);
        changed = true;
    }

    @Override
    public boolean deleteKey(UUID keyId) {
        boolean keyDeleted = super.deleteKey(keyId);
        changed = keyDeleted;
        return keyDeleted;
    }

    @Override
    public boolean deleteValue(UUID valueId) {
        boolean valueDeleted = super.deleteValue(valueId);
        changed = valueDeleted;
        return valueDeleted;
    }
}
