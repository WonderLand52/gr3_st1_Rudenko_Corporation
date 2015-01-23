package dao.daoImpl.jdbcDAOUpd;

import dao.IdDAO;

import java.sql.*;
import java.util.*;

public abstract class BaseIdJdbcDAO implements IdDAO {

    private String dropTable;
    private String createTable;
    private String addEntry;
    private String getAllEntries;
    private String keyId;
    private String valueId;

    private static Connection conn = DBHandler.getConnection();
    private Map<UUID, List<UUID>> idsCash = new HashMap<>();

    protected BaseIdJdbcDAO(String createTable,
                            String dropTable,
                            String addEntry,
                            String getAllEntries,
                            String keyId,
                            String valueId,
                            String tableName) {

        this.createTable = createTable;
        this.dropTable = dropTable;
        this.getAllEntries = getAllEntries;
        this.addEntry = addEntry;
        this.keyId = keyId;
        this.valueId = valueId;

        BaseModuleJdbcDAO.createTableIfNotExists(tableName, createTable);
        this.getAllEntriesFromDB();
    }

    @Override
    public void addValue(UUID keyId, UUID valueId) {

        if(!idsCash.containsKey(keyId)) {
            List<UUID> projIds = new ArrayList<>();
            projIds.add(valueId);
            idsCash.put(keyId, projIds);
        } else {
            List<UUID> projIds = idsCash.get(keyId);
            if(!projIds.contains(valueId)) {
                projIds.add(valueId);
            }
        }
    }

    @Override
    public List<UUID> getValues(UUID keyId) {
        return idsCash.get(keyId);
    }

    @Override
    public List<UUID> getKeys(UUID valueId) {
        List<UUID> empIds = new ArrayList<>();

        Set<Map.Entry<UUID, List<UUID>>> entries = idsCash.entrySet();
        for(Map.Entry<UUID, List<UUID>> entry: entries) {
            List<UUID> projIds = entry.getValue();
            for(UUID id: projIds) {
                if(id.equals(valueId))
                    empIds.add(entry.getKey());
            }
        }
        return empIds;
    }

    @Override
    public boolean deleteValue(UUID valueId) {
        Set<Map.Entry<UUID, List<UUID>>> entries = idsCash.entrySet();
        for(Map.Entry<UUID, List<UUID>> entry: entries) {
            List<UUID> projIds = entry.getValue();
            for(int i = 0; i < projIds.size(); i++) {
                if(projIds.get(i).equals(valueId)) {
                    projIds.remove(i);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean deleteKey(UUID keyId) {
        if(idsCash.containsKey(keyId)) {
            idsCash.remove(keyId);
            return true;
        }

        return false;
    }

    @Override
    public boolean keyExists(UUID keyId) {
        return idsCash.containsKey(keyId);
    }

    @Override
    public void close() throws Exception {
        DBHandler.closeDAO();
    }

    private void getAllEntriesFromDB() {
        try(Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(getAllEntries);

            while(rs.next()) {
                UUID empId = UUID.fromString(rs.getString(keyId));
                UUID projId = UUID.fromString(rs.getString(valueId));

                if(!idsCash.containsKey(empId)) {
                    List<UUID> projIds = new ArrayList<>();
                    projIds.add(projId);
                    idsCash.put(empId, projIds);
                } else {
                    List<UUID> projIds = idsCash.get(empId);
                    projIds.add(projId);
                    idsCash.put(empId, projIds);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void writeAllEntries() {

        reCreateTable();

        try(PreparedStatement stmt = conn.prepareStatement(addEntry)) {

            Set<Map.Entry<UUID, List<UUID>>> entries = idsCash.entrySet();

            //get every entry from list
            for(Map.Entry<UUID, List<UUID>> entry: entries) {
                UUID empId = entry.getKey();

                //write pair empId, projId to the table
                for(UUID projId: entry.getValue()) {
                    stmt.setString(1, empId.toString());
                    stmt.setString(2, projId.toString());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void reCreateTable() {

        try(Statement stmt = conn.createStatement()) {
            stmt.execute(dropTable);
            stmt.execute(createTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

//    private static void createTableIfNotExists(String tableName,
//                                                 String createTable) {
//
//        ResultSet rs = null;
//
//        try {
//            DatabaseMetaData meta = conn.getMetaData();
//            rs = meta.getTables(null, null, tableName, null);
//            if(!rs.next()) {
//                Statement stmt = conn.createStatement();
//                stmt.executeUpdate(createTable);
//            }
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                if(rs != null)
//                    rs.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
