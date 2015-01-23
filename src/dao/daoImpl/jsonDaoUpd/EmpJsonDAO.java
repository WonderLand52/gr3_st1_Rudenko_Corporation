package dao.daoImpl.jsonDaoUpd;

import corporationmodules.collaborators.Customer;
import corporationmodules.collaborators.Director;
import corporationmodules.collaborators.Employee;
import corporationmodules.collaborators.Worker;
import org.json.simple.JSONObject;

import java.util.UUID;

public class EmpJsonDAO extends ModuleJsonDAO<Employee> {

    protected static final String FILE_NAME_EMPLOYEE = "employees.json";
    private static final String KEY_EMPLOYEE_ID = "empId";
    private static final String KEY_EMPLOYEE_NAME = "empName";
    private static final String KEY_EMPLOYEE_POSITION = "empPos";

    protected static boolean empChanged = false;

    private static EmpJsonDAO instance;

    private EmpJsonDAO() {
        super(FILE_NAME_EMPLOYEE, KEY_EMPLOYEE_ID, KEY_EMPLOYEE_NAME);
        System.out.println("Inside EMployeeJsonDAO");
    }

    public static EmpJsonDAO getInstance() {
        if(instance == null){
            instance = new EmpJsonDAO();
            return instance;
        }

        return instance;
    }

    @Override
    public void add(Employee emp) {
        if(!exists(emp)) {
            JSONObject emplJson = new JSONObject();

            //add every single field
            emplJson.put(KEY_EMPLOYEE_ID, emp.getId().toString());
            emplJson.put(KEY_EMPLOYEE_NAME, emp.getName());
            emplJson.put(KEY_EMPLOYEE_POSITION, emp.getClass().getSimpleName());

            modulesJson.add(emplJson);

            empChanged = true;
        }
    }

    @Override
    public void delete(Employee emp) {

        JSONObject empJson = getJsonObjectByModule(emp);
        modulesJson.remove(empJson);

        UUID empId = emp.getId();
        EmpProjJsonDAO.getInstance().deleteKey(empId);
        DeptEmpJsonDAO.getInstance().deleteValue(empId);

        empChanged = true;
    }

    @Override
    protected Employee getModuleFromJsonObject(JSONObject obj) {

        switch ((String) obj.get(KEY_EMPLOYEE_POSITION)) {
            case "Worker":
                return new Worker(UUID.fromString((String) obj.get(KEY_EMPLOYEE_ID)), (String) obj.get(KEY_EMPLOYEE_NAME));
            case "Director":
                return new Director(UUID.fromString((String) obj.get(KEY_EMPLOYEE_ID)), (String) obj.get(KEY_EMPLOYEE_NAME));
            case "Customer":
                return new Customer(UUID.fromString((String) obj.get(KEY_EMPLOYEE_ID)), (String) obj.get(KEY_EMPLOYEE_NAME));
            default:
                return null;
        }
    }
}
