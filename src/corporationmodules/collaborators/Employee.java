package corporationmodules.collaborators;

import corporationmodules.Module;
import dao.IdDAO;
import dao.ModuleDAO;
import dao.daoImpl.jsonDaoUpd.EventJsonDAO;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;
import corporationmodules.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Employee implements Serializable, Module {

    protected final String name;
    protected final UUID employeeId;

    protected Employee(String name){
        this.employeeId = UUID.randomUUID();
        this.name = name;
    }

    protected Employee(UUID employeeId, String name){
        this.employeeId = employeeId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return employeeId;
    }

    public List<Project> getProjects(IdDAO empProjDAO, ModuleDAO<Project> projDAO) throws DaoSystemException, NoSuchEntityException {

        List<Project> projects = new ArrayList<>();
        for(UUID id: empProjDAO.getValues(employeeId)) {
            projects.add(getProject(id, projDAO));
        }

        return projects;
    }

    public Project getProject(UUID projId, ModuleDAO<Project> projDao) throws NoSuchEntityException, DaoSystemException {

        return projDao.get(projId);
    }

    public void setProject(Project project, IdDAO empProjDAO) throws DaoSystemException, NoSuchEntityException {
        System.out.println(empProjDAO);
        System.out.println(project);
        empProjDAO.addValue(this.getId(), project.getId());

        EventJsonDAO.getInstance().addModuleEvent(project, "Project is set: ");
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Employee name: ");
        sb.append(this.getName());
        sb.append(", Employee position: ");
        sb.append(this.getClass().getSimpleName());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;

        Employee employee = (Employee) o;

        if (!employeeId.equals(employee.employeeId)) return false;
        if (!name.equals(employee.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + employeeId.hashCode();
        return result;
    }
}
