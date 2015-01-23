import corporationmodules.Company;
import corporationmodules.Project;
import corporationmodules.collaborators.Customer;
import corporationmodules.collaborators.Employee;
import dao.IdDAO;
import dao.ModuleDAO;
import dao.daoImpl.jdbcDAOUpd.EmpJdbcDAO;
import dao.daoImpl.jdbcDAOUpd.EmpProjJdbcDAO;
import dao.daoImpl.jdbcDAOUpd.ProjJdbcDAO;
import dao.daoImpl.jsonDaoUpd.*;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        long before = System.nanoTime();

        //initialite DAO you want to using try with resources
        try(ModuleDAO<Project> projDAO = ProjJdbcDAO.getInstance();
            ModuleDAO<Employee> empDAO = EmpJdbcDAO.getInstance();
            IdDAO empProjDAO = EmpProjJdbcDAO.getInstance()
            /*ModuleDAO<Department> deptDAO = DeptJdbcDAO.getInstance()*/) {
            //do everything
            //if you want to save some new module, invoke "add"

//            Employee emp = new Customer("Alan");
//            empDAO.add(emp);
//            Project proj = projDAO.get("helloProj");
//            emp.setProject(proj, empProjDAO);


//            Company comp = new Company();
//            List<Project> projects = comp.getProjectsByCustomer((Customer) empDAO.get("Alan"), projDAO, empProjDAO);
//            System.out.println(projects);
            Company comp = new Company();
            List<Project> projects = comp.getProjectsByCustomer((Customer) empDAO.get("Alan"), projDAO, empProjDAO);
            System.out.println(projects);




            //every DAO closes automatically and safely
        } catch (DaoSystemException e) {
            System.out.println("DaoSystemException: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchEntityException e) {
            System.out.println("NoSuchEntityException" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long after = System.nanoTime();
        System.out.println(after - before);

        //EAV entity attribute value model
        //Testing records to implement

    }
}
