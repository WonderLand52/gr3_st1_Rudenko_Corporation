package handlers;

import corporationmodules.collaborators.Employee;
import corporationmodules.Company;
import corporationmodules.Department;
import corporationmodules.Project;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public class BinaryFileReader implements EntityReader {

    @Override
    public List<Employee> readCollaborator() {
        return (List<Employee>) deserializeData(BinaryFileWriter.COLLABORATOR_FILENAME);
    }

    @Override
    public List<Project> readProject() {
        return (List<Project>) deserializeData(BinaryFileWriter. PROJECT_FILENAME);
    }

    @Override
    public List<Department> readDepartment() {
        return (List<Department>) deserializeData(BinaryFileWriter.DEPARTMENT_FILENAME);
    }

    @Override
    public Company readCompany() {
        return (Company) deserializeData(BinaryFileWriter.COMPANY_FILENAME);
    }

    private Object deserializeData(String fileName) {

        Object obj = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            obj = ois.readObject();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
