package dao.daoImpl.jsonDaoUpd;

public class DeptEmpJsonDAO extends BaseIdJsonDAO {

    protected static final String FILE_NAME_DEPARTMENT_IDS = "deptIds.json";
    private static final String KEY_DEPARTMENT_ID = "deptId";
    private static final String KEY_EMPLOYEES_IDS = "empIds";

    protected static boolean alive = false;

    private static DeptEmpJsonDAO instance;

    private DeptEmpJsonDAO() {
        super(FILE_NAME_DEPARTMENT_IDS, KEY_DEPARTMENT_ID, KEY_EMPLOYEES_IDS);
    }

    public static DeptEmpJsonDAO getInstance() {
        if(instance == null) {
            instance = new DeptEmpJsonDAO();
            alive = true;
            return instance;
        }

        return instance;
    }
}
