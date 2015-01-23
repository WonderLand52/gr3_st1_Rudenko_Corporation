package dao.daoImpl.jdbcDAOUpd;


import dao.daoImpl.jsonDaoUpd.EventJsonDAO;

import java.sql.*;

public class DBHandler {

    private static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    private static final String URL_ORACLE = "jdbc:oracle:thin:@localhost:1521:xe";
    protected static final String LOGIN_ORACLE = "rud";
    private static final String PASSWORD_ORACLE = "password";

    private static Connection connection;

    public static Connection getConnection() {

        if(connection == null) {
            try {
                Class.forName(DRIVER_ORACLE);

            } catch (ClassNotFoundException e) {
                System.out.println("Where is your oracle jdbc driver?");
                e.printStackTrace();
            }

            try {
                connection = DriverManager.getConnection(URL_ORACLE, LOGIN_ORACLE, PASSWORD_ORACLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return connection;
        }

        return connection;
    }



    public static void closeDAO() throws Exception {

        if(ProjJdbcDAO.changed) {
            System.out.println("write projects");
            ProjJdbcDAO.getInstance().writeAllModules();
            ProjJdbcDAO.changed = false;
        }

        if(EmpJdbcDAO.changed) {
            System.out.println("write employees");
            EmpJdbcDAO.getInstance().writeAllModules();
            EmpJdbcDAO.changed = false;
        }

        if(DeptJdbcDAO.changed) {
            System.out.println("write departments");
            DeptJdbcDAO.getInstance().writeAllModules();
            DeptJdbcDAO.changed = false;
        }

        if(EmpProjJdbcDAO.changed) {
            System.out.println("write empProjIds");
            EmpProjJdbcDAO.getInstance().writeAllEntries();
            EmpProjJdbcDAO.changed = false;
        }

        if(DeptEmpJdbcDAO.changed) {
            System.out.println("write deptEmpIds");
            DeptEmpJdbcDAO.getInstance().writeAllEntries();
            DeptEmpJdbcDAO.changed = false;
        }

        if(EventJsonDAO.isAlive() && EventJsonDAO.isChanged()) {
            System.out.println("write events " + EventJsonDAO.isAlive());
            EventJsonDAO.setAlive(false);
            EventJsonDAO.getInstance().writeAllEvents();
        }

        if(connection != null && !connection.isClosed()) {
            System.out.println("close connection");
            connection.close();
        }
    }
}
