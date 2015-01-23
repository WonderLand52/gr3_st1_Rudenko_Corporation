package dao.daoImpl.jdbcDAOUpd;

import java.util.UUID;

public class EmpProjJdbcDAO extends BaseIdJdbcDAO {

    private static final String COLUMN_EMPLOYEE_ID = "empId";
    private static final String COLUMN_PROJECT_ID = "projId";
    private static final String TABLE_NAME = "EMPPROJIDS";
    private static final String ADD_ENTRY = "INSERT INTO empProjIds (empId, projId) VALUES (?, ?)";
    private static final String GET_ALL_ENTRIES = "SELECT empId, projId FROM empProjIds";
    private static final String DROP_TABLE = "DROP TABLE empProjIds";
    private static final String CREATE_TABLE = "CREATE TABLE empProjIds (empId VARCHAR2(60), projId VARCHAR(60))";

    protected static boolean changed = false;

    private static EmpProjJdbcDAO instance;

    private EmpProjJdbcDAO() {
        super(CREATE_TABLE, DROP_TABLE, ADD_ENTRY, GET_ALL_ENTRIES, COLUMN_EMPLOYEE_ID, COLUMN_PROJECT_ID, TABLE_NAME);
    }

    public static EmpProjJdbcDAO getInstance() {
        if(instance == null) {
            instance = new EmpProjJdbcDAO();
            return instance;
        }

        return instance;
    }

    @Override
    public void addValue(UUID keyId, UUID valueId) {

        BaseModuleJdbcDAO.createTableIfNotExists(TABLE_NAME, CREATE_TABLE);

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
