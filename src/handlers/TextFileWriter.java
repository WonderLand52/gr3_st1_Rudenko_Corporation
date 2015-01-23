package handlers;


import corporationmodules.collaborators.Employee;
import corporationmodules.Company;
import corporationmodules.Department;
import corporationmodules.Project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TextFileWriter implements EntityWriter {

    protected static final String FILE_NAME_COLLABORATOR = "collaborator.txt";
    protected static final String FILE_NAME_PROJECT = "project.txt";
    protected static final String FILE_NAME_DEPARTMENT = "department.txt";
    protected static final String FILE_NAME_COMPANY = "company.txt";

    @Override
    public void writeCollaborator(Employee col) {
        writeStrObj(FILE_NAME_COLLABORATOR, col.toString());
    }

    @Override
    public void writeProject(Project proj) {
        writeStrObj(FILE_NAME_PROJECT, proj.toString());
    }

    @Override
    public void writeDepartment(Department dept) {
        writeStrObj(FILE_NAME_DEPARTMENT, dept.toString());
    }

    @Override
    public void writeCompany(Company comp) {
        writeStrObj(FILE_NAME_COMPANY, comp.toString());
    }

    private void writeStrObj(String fileName, String objToString) {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.append(objToString.replaceAll("\n", System.lineSeparator()));
            bw.newLine();
        } catch(IOException ex){
            ex.printStackTrace();
            System.exit(-1);
        }
    }
}
