package dao.daoImpl.jdbcDAOUpd;

import dao.daoImpl.jsonDaoUpd.EventJsonDAO;
import corporationmodules.Project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjJdbcDAO extends BaseModuleJdbcDAO<Project> {

    private static final String COLUMN_PROJ_NAME= "projName";
    private static final String COLUMN_PROJ_ID = "projId";
    private static final String TABLE_NAME = "PROJECTS";
    private static final String ADD_PROJECT = "INSERT INTO projects (projId, projName) VALUES (?, ?)";
    private static final String GET_ALL_PROJECTS = "SELECT projId, projName FROM projects";
    private static final String DROP_TABLE = "DROP TABLE projects";
    private static final String CREATE_TABLE = "CREATE TABLE projects (projId VARCHAR2(60), projName VARCHAR(40))";

    protected static boolean changed = false;

    private static ProjJdbcDAO instance;

    public static ProjJdbcDAO getInstance() {
        if (instance == null) {
            instance = new ProjJdbcDAO();
            return instance;
        }

        return instance;
    }

    private ProjJdbcDAO() {
        super(CREATE_TABLE, DROP_TABLE, ADD_PROJECT, getProjectsFromDB());
    }

    protected static List<Project> getProjectsFromDB() {

        createTableIfNotExists(TABLE_NAME, CREATE_TABLE);

        List<Project> projects = new ArrayList<>();
        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(GET_ALL_PROJECTS);

            while(rs.next()) {
                projects.add(new Project(UUID.fromString(rs.getString(COLUMN_PROJ_ID)), rs.getString(COLUMN_PROJ_NAME)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    @Override
    public void add(Project module) {
        super.add(module);

        changed = true;
    }

    @Override
    public void delete(Project proj) {

        for(int i = 0; i <  moduleCash.size(); i++) {
            if(moduleCash.get(i).getId().equals(proj.getId())) {
                moduleCash.remove(i);
                //delete from projIds
                EmpProjJdbcDAO.getInstance().deleteValue(proj.getId());
                //add event
                EventJsonDAO.getInstance().addModuleEvent(proj, "Project is removed: ");

                changed = true;
            }
        }
    }
}
