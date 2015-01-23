package handlers;

import corporationmodules.collaborators.Employee;
import corporationmodules.Company;
import corporationmodules.Department;
import corporationmodules.Project;

import java.io.Serializable;

public interface EntityWriter extends Serializable {

    public void writeCollaborator(Employee col);

    public void writeProject(Project proj);

    public void writeDepartment(Department dept);

    public void writeCompany(Company comp);
}
