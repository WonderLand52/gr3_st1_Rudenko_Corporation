package corporationmodules;

import corporationmodules.collaborators.Employee;
import dao.IdDAO;
import dao.ModuleDAO;
import dao.daoImpl.jsonDaoUpd.EventJsonDAO;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Department implements Serializable, Module {

    private String name;
    private final UUID deptId;

    public Department(String name) {
        this.deptId = UUID.randomUUID();
        this.name = name;
    }

    public Department(UUID deptId, String name) {
        this.deptId = deptId;
        this.name = name;
    }

    public UUID getId() {
        return deptId;
    }

    public Employee getEmployee(UUID empId, ModuleDAO<Employee> empDAO) throws DaoSystemException, NoSuchEntityException {
        return empDAO.get(empId);
    }

    public List<Employee> getEmployees(ModuleDAO<Employee> empDAO, IdDAO deptEmpDAO) throws DaoSystemException, NoSuchEntityException {
        List<Employee> employees = new ArrayList<>();
        List<UUID> empIds = deptEmpDAO.getValues(deptId);
        for(UUID empId: empIds) {
            employees.add(empDAO.get(empId));
        }

        return employees;
    }

    public void setEmployee(Employee emp, IdDAO deptEmpDAO) throws DaoSystemException, NoSuchEntityException {
        deptEmpDAO.addValue(deptId, emp.getId());

        EventJsonDAO.getInstance().addModuleEvent(emp, "Employee is set: ");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Department id: ");
        sb.append(deptId.toString());
        sb.append(", Department name: ");
        sb.append(name);

        return sb.toString();
    }
}
