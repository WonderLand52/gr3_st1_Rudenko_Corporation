package handlers;


import corporationmodules.collaborators.Employee;
import corporationmodules.Company;
import corporationmodules.Department;
import corporationmodules.Project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileWriter implements EntityWriter {

    protected static final String PROJECT_FILENAME = "project.data";
    protected static final String COLLABORATOR_FILENAME = "collaborator.data";
    protected static final String DEPARTMENT_FILENAME = "department.data";
    protected static final String COMPANY_FILENAME = "company.data";

    private List<Project> projects = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();
    private List<Department> departments = new ArrayList<>();

    @Override
    public void writeCollaborator(Employee col) {
        employees.add(col);
        serializeData(COLLABORATOR_FILENAME, employees);
    }

    @Override
    public void writeProject(Project proj) {
        projects.add(proj);
        serializeData(PROJECT_FILENAME, projects);
    }

    @Override
    public void writeDepartment(Department dept) {
        departments.add(dept);
        serializeData(DEPARTMENT_FILENAME, departments);
    }

    @Override
    public void writeCompany(Company comp) {
        serializeData(COMPANY_FILENAME, comp);
    }

    private void serializeData(String fileName, Object obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(obj);

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
