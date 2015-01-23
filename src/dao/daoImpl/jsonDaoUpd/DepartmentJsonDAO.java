package dao.daoImpl.jsonDaoUpd;

import corporationmodules.Department;
import org.json.simple.JSONObject;

import java.util.UUID;

public class DepartmentJsonDAO extends ModuleJsonDAO<Department> {


    protected static final String FILE_NAME_JSON_DEPARTMENT = "departments.json";
    private static final String KEY_DEPARTMENT_ID = "deptId";
    private static final String KEY_DEPARTMENT_NAME = "deptName";

    protected static boolean deptChanged = false;

    private static DepartmentJsonDAO instance;

    private DepartmentJsonDAO() {
        super(FILE_NAME_JSON_DEPARTMENT, KEY_DEPARTMENT_ID, KEY_DEPARTMENT_NAME);
    }

    public static DepartmentJsonDAO getInstance() {
        if(instance == null){
            instance = new DepartmentJsonDAO();
            return instance;
        }

        return instance;
    }

    @Override
    public void add(Department dept) {
        if(!exists(dept)) {
            //create new Json object
            JSONObject newDept = new JSONObject();

            newDept.put(KEY_DEPARTMENT_ID, dept.getId().toString());
            newDept.put(KEY_DEPARTMENT_NAME, dept.getName());

            modulesJson.add(newDept);

            deptChanged = true;
        }
    }

    @Override
    public void delete(Department dept) {
        JSONObject projJson = getJsonObjectByModule(dept);
        modulesJson.remove(projJson);

        DeptEmpJsonDAO.getInstance().deleteKey(dept.getId());

        deptChanged = true;
    }

    @Override
    protected Department getModuleFromJsonObject(JSONObject obj) {
        return new Department(UUID.fromString((String) obj.get(KEY_DEPARTMENT_ID)),
                        (String) obj.get(KEY_DEPARTMENT_NAME));
    }
}
