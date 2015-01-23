package corporationmodules;

import corporationmodules.collaborators.Customer;
import corporationmodules.collaborators.Employee;
import corporationmodules.collaborators.Director;
import dao.*;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Company implements Serializable {

    public List<Employee> getWorkersByProject(Project project,
                                              IdDAO empProjDAO,
                                              ModuleDAO<Employee> empDAO) throws NoSuchEntityException, DaoSystemException {

        List<Employee> employees = new ArrayList<>();

        if(project != null) {
            List<UUID> empIds = empProjDAO.getKeys(project.getId());
            for(UUID empId: empIds) {
                employees.add(empDAO.get(empId));
            }
        }

        return employees;
    }

    public List<Project> getProjectsByWorker(Employee worker,
                                             IdDAO empProjDAO,
                                             ModuleDAO<Project> projDAO)
                            throws DaoSystemException, NoSuchEntityException {
        if(worker != null) {
            return worker.getProjects(empProjDAO, projDAO);
        }

        return new ArrayList<>();
    }

    public List<Employee> getFreeWorkersFromDept(Department dept,
                                                 ModuleDAO<Employee> empDAO,
                                                 IdDAO deptEmpDAO,
                                                 IdDAO empProjDAO)
                                                    throws NoSuchEntityException, DaoSystemException {


        List<Employee> freeWorkers = new ArrayList<>();

        if(dept != null){
            for(UUID empId: deptEmpDAO.getValues(dept.getId())) {
                if(empProjDAO.getValues(empId).isEmpty()) {
                    freeWorkers.add(empDAO.get(empId));
                }
            }
        }

        return freeWorkers;
    }

    public List<Employee> getFreeWorkers(ModuleDAO<Employee> empDAO,
                                         IdDAO empProjDAO)
                                            throws NoSuchEntityException, DaoSystemException {
        List<Employee> freeWorkers = new ArrayList<>();

        for(Employee emp: empDAO.getAll()) {
            if(empProjDAO.getValues(emp.getId()).isEmpty()) {
                freeWorkers.add(emp);
            }
        }

        return freeWorkers;
    }

    public List<Employee> getEmployees(Director director,
                                       IdDAO empProjDAO,
                                       ModuleDAO<Project> projDAO,
                                       ModuleDAO<Employee> empDAO)
                                            throws NoSuchEntityException, DaoSystemException {
        List<Employee> directorWorkers = new ArrayList<>();

        if(director != null) {
            for(Project project: director.getProjects(empProjDAO, projDAO)) {
                directorWorkers.addAll(getWorkersByProject(project, empProjDAO, empDAO));
            }
        }

        return directorWorkers;
    }

    public List<Director> getWorkerDirectors(Employee emp,
                                             IdDAO empProjDAO,
                                             ModuleDAO<Project> projDAO,
                                             ModuleDAO<Project> empDAO)
                                                throws NoSuchEntityException, DaoSystemException {

        List<Director> workerDirectors = new ArrayList<>();

        if(emp != null) {
            for(Project project: emp.getProjects(empProjDAO, projDAO)){
                workerDirectors.add(project.getDirector(empDAO));
            }
        }

        return workerDirectors;
    }

    public List<Employee> getWorkersInTheSameProjects(Employee emp,
                                                      IdDAO empProjDAO,
                                                      ModuleDAO<Employee> empDAO)
                                                        throws NoSuchEntityException, DaoSystemException {
        List<Employee> employees = new ArrayList<>();

        if(emp != null) {
            List<UUID> projIds = empProjDAO.getValues(emp.getId());

            for(UUID projId: projIds) {
                for(UUID empId: empProjDAO.getKeys(projId)) {
                    Employee emp1 = empDAO.get(empId);
                    if(emp1 != null && !emp1.getId().equals(emp.getId())) {
                        employees.add(emp1);
                    }
                }
            }
        }

        return employees;
    }

    public List<Project> getProjectsByCustomer(Customer customer,
                                               ModuleDAO<Project> projDAO,
                                               IdDAO empProjDAO)
                                                throws NoSuchEntityException, DaoSystemException {
        if(customer != null) {
            return customer.getProjects(empProjDAO, projDAO);
        }

        return new ArrayList<>();
    }

    public List<Employee> getWorkersByCustomer(Customer customer,
                                               IdDAO empProjDAO,
                                               ModuleDAO<Employee> empDAO) throws NoSuchEntityException, DaoSystemException {

        List<Employee> customerWorkers = new ArrayList<>();
        if(customer != null) {
            List<UUID> empIds = new ArrayList<>();
            for(UUID projId: empProjDAO.getValues(customer.getId())) {
                empIds.addAll(empProjDAO.getKeys(projId));
            }

            for(UUID empId: empIds) {
                customerWorkers.add(empDAO.get(empId));
            }
        }

        return customerWorkers;
    }
}
