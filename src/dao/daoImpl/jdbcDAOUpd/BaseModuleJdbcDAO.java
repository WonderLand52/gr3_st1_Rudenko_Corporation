package dao.daoImpl.jdbcDAOUpd;

import corporationmodules.Module;
import dao.ModuleDAO;
import dao.daoImpl.jsonDaoUpd.EventJsonDAO;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public abstract class BaseModuleJdbcDAO<T extends Module> implements ModuleDAO<T> {

    private final String addModule;
    private final String dropTable;
    private final String createTable;

    protected final List<T> moduleCash;
    protected static Connection conn = DBHandler.getConnection();

    protected BaseModuleJdbcDAO(String createTable,
                                String dropTable,
                                String addModule,
                                List<T> moduleCash) {

        this.addModule = addModule;
        this.createTable = createTable;
        this.dropTable = dropTable;
        this.moduleCash = moduleCash;
    }

    @Override
    public void add(T module) {
        if(!moduleCash.contains(module)) {
            moduleCash.add(module);

            EventJsonDAO.getInstance().addModuleEvent(module, "Module is added: ");
        }
    }

    /**
     *
     * @param id unique module's id to find one with
     * @return module using unique UUID. If there is no equaled modules, returns null
     */
    @Override
    public T get(UUID id) {

        for(T module: moduleCash) {
            if(module.getId().equals(id))
                return module;
        }

        return null;
    }

    /**
     *
     * @param name module's name to find one with
     * @return first equaled module. If there is no equaled modules, returns null
     */
    @Override
    public T get(String name) {

        for(T module: moduleCash) {
            if(module.getName().equals(name)) {
                return module;
            }
        }

        return null;
    }

    @Override
    public List<T> getAll() {

        return moduleCash;
    }

    @Override
    public boolean exists(Module module) {

        return moduleCash.contains(module);
    }

    @Override
    public void close() throws Exception {
        DBHandler.closeDAO();
    }

    protected void writeAllModules() {

        reCreateTable();

        try(PreparedStatement stmt = conn.prepareStatement(addModule)) {

            for(Module module: moduleCash) {
                stmt.setString(1, module.getId().toString());
                stmt.setString(2, module.getName());
                stmt.execute();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void reCreateTable() {
        try(Statement stmt = conn.createStatement()) {
            stmt.execute(dropTable);
            stmt.execute(createTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected static void createTableIfNotExists(String tableName,
                                                    String createTable) {

        ResultSet rs = null;

        try {
            DatabaseMetaData meta = conn.getMetaData();
            rs = meta.getTables(null, null, tableName, null);
            if(rs.next()) {

            } else {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(createTable);
                stmt.close();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if(rs != null)
                    rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
