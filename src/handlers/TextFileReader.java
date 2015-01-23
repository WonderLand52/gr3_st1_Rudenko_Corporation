package handlers;


import corporationmodules.collaborators.Employee;
import corporationmodules.Company;
import corporationmodules.Department;
import corporationmodules.Project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFileReader implements EntityReader {

    private static final String PROJECT_REGEX = "Project name: (\\w+), Director name: (\\w+)";
    private static final String COLLABORATOR_REGEX = "Collaborator name: (\\w+), Collaborator position: (\\w+)";
    private static final String DEPARTMENT_REGEX = "Department name: (\\w+)";
    private static final String COMPANY_REGEX = "Company name: (\\w+)";

    @Override
    public List<Employee> readCollaborator() {
        String collaboratorsStr = readStringFromFile(TextFileWriter.FILE_NAME_COLLABORATOR);

//        return parseCollaborator(collaboratorsStr, COLLABORATOR_REGEX);
        return null;
    }


    @Override
    public List<Project> readProject() {
        String projectsStr = readStringFromFile(TextFileWriter.FILE_NAME_PROJECT);

        return parseProject(projectsStr, PROJECT_REGEX);
    }

    @Override
    public List<Department> readDepartment() {
        String deptStr = readStringFromFile(TextFileWriter.FILE_NAME_DEPARTMENT);

        return parseDepartment(deptStr, DEPARTMENT_REGEX);
    }

    @Override
    public Company readCompany() {
        String compStr = readStringFromFile(TextFileWriter.FILE_NAME_COMPANY);

        return parseCompany(compStr, COMPANY_REGEX);
    }

    private List<Project> parseProject(String projStr, String regEx) {
//        Pattern pat0 = Pattern.compile(regEx);
//        Matcher match = pat0.matcher(projStr);
//        List<Project> projFromStr = new ArrayList<>();
//        while (match.find()) {
//            projFromStr.add(new Project(match.group(1), new Director(match.group(2))));
//        }

        return null;
    }

//    private List<Employee> parseCollaborator(String colStr, String regEx) {
//        Pattern pat0 = Pattern.compile(regEx);
//        Matcher match = pat0.matcher(colStr);
//        List<Employee> colFromStr = new ArrayList<>();
//        while (match.find()) {
//            switch (match.group(2)) {
//                case "Customer":
//                    Employee customer = new Customer(match.group(1));
//                    customer.setProjects(parseProject(colStr, PROJECT_REGEX));
//                    colFromStr.add(customer);
//                    break;
//
//                case "Director":
//                    Employee director = new Director(match.group(1));
//                    director.setProjects(parseProject(colStr, PROJECT_REGEX));
//                    colFromStr.add(director);
//                    break;
//
//                case "Worker":
//                    Employee worker = new Worker(match.group(1));
//                    worker.setProjects(parseProject(colStr, PROJECT_REGEX));
//                    colFromStr.add(worker);
//                    break;
//            }
//        }
//
//        return colFromStr;
//    }

    private List<Department> parseDepartment(String deptStr, String regex) {
        Pattern pat0 = Pattern.compile(regex);
        Matcher match = pat0.matcher(deptStr);
        List<Department> deptFromStr = new ArrayList<>();
        while (match.find()) {
            Department dept = new Department(match.group(1));
//            dept.setWorkers(parseCollaborator(deptStr, COLLABORATOR_REGEX));
            deptFromStr.add(dept);
        }

        return deptFromStr;
    }

    private Company parseCompany(String compStr, String regex) {
//        Pattern pat0 = Pattern.compile(regex);
//        Matcher match = pat0.matcher(compStr);
//        Company comp = null;
//        while (match.find()) {
//            comp = new Company(match.group(1));
//            comp.setDepartments(parseDepartment(compStr, DEPARTMENT_REGEX));
//        }

        return null;
    }

    public String readStringFromFile(String fileName) {

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.lineSeparator();
        try (BufferedReader reader = new BufferedReader( new FileReader (fileName))){
            while( ( line = reader.readLine() ) != null ) {
                stringBuilder.append( line );
                stringBuilder.append( ls );
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
