package handlers;

import corporationmodules.collaborators.Employee;
import corporationmodules.Company;
import corporationmodules.Department;
import corporationmodules.Project;

import java.io.Serializable;
import java.util.List;

public interface EntityReader extends Serializable {

    public List<Employee> readCollaborator();

    public List<Project> readProject();

    public List<Department> readDepartment();

    public Company readCompany();
}
