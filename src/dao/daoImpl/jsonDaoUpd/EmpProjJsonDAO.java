package dao.daoImpl.jsonDaoUpd;


public class EmpProjJsonDAO extends BaseIdJsonDAO {

    protected static final String FILE_NAME_JSON_PROJECT_IDS = "projIds.json";
    private static final String KEY_EMPLOYEE_ID = "empId";
    private static final String KEY_PROJECTS_IDS = "projIds";

    protected static boolean alive = false;

    private static EmpProjJsonDAO instance;

    public EmpProjJsonDAO() {
        super(FILE_NAME_JSON_PROJECT_IDS, KEY_EMPLOYEE_ID, KEY_PROJECTS_IDS);
    }

    public static EmpProjJsonDAO getInstance() {
        if(instance == null) {
            instance = new EmpProjJsonDAO();
            alive = true;
            return instance;
        }

        return instance;
    }
}
