package dao.daoImpl.jsonDaoUpd;

import dao.IdDAO;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class BaseIdJsonDAO implements IdDAO {

    protected JSONArray idsJson;

    private String keyName;
    private String valuesName;


    protected BaseIdJsonDAO(String fileName, String keyName, String valuesName) {

        this.keyName = keyName;
        this.valuesName = valuesName;
        System.out.println("Inside BaseIdJsonDAO (fileName: " + fileName + ")");
        try {
            idsJson = new JsonFileHandler().getArrayObjectsFromJson(fileName);
        } catch (DaoSystemException e) {
            e.printStackTrace();
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addValue(UUID keyId, UUID valueId) {

        if(keyExists(keyId)){
            JSONObject empJson = deleteAndGetByKey(keyId);
            JSONArray projIds = (JSONArray) empJson.get(keyName);

            projIds.add(valueId.toString());

            idsJson.add(empJson);

        } else {
            JSONObject empJson = new JSONObject();
            JSONArray projIds = new JSONArray();

            projIds.add(valueId.toString());
            empJson.put(keyName, keyId.toString());
            empJson.put(valuesName, projIds);

            idsJson.add(empJson);

        }
    }

    @Override
    public List<UUID> getValues(UUID keyId) {
        List<UUID> projectsIds = new ArrayList<>();

        JSONObject empJson = getJsonObject(keyId);

        if(empJson != null) {
            JSONArray projectsIdsJson = (JSONArray) empJson.get(valuesName);
            for(Object obj: projectsIdsJson) {
                projectsIds.add(UUID.fromString((String) obj));
            }
        }

        return projectsIds;
    }

    @Override
    public List<UUID> getKeys(UUID valueId) {

        List<UUID> keyIds = new ArrayList<>();
        String valueIdStr = valueId.toString();
        for(Object obj: idsJson) {
            JSONObject objJson = (JSONObject) obj;
            JSONArray projectsIdsJson = (JSONArray) objJson.get(valuesName);

            for(Object valueJson: projectsIdsJson) {
                if(valueJson.equals(valueIdStr))
                    keyIds.add(UUID.fromString((String) objJson.get(keyName)));
            }
        }

        return keyIds;
    }

    @Override
    public boolean deleteValue(UUID valueId) {
        String valueIdStr = valueId.toString();

        for(Object obj: idsJson) {
            JSONObject empJson = (JSONObject) obj;
            JSONArray valuesJson = (JSONArray) empJson.get(valuesName);

            for(Object valueJson: valuesJson) {
                if(valueJson.equals(valueIdStr)) {
                    valuesJson.remove(valueJson);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean deleteKey(UUID keyId) {
        String keyIdStr = keyId.toString();

        for(Object obj: idsJson) {
            JSONObject empJson = (JSONObject) obj;
            if(empJson.get(keyName).equals(keyIdStr)) {
                idsJson.remove(empJson);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyExists(UUID keyId) {

//        for(Object obj: idsJson) {
//            JSONObject empJson = (JSONObject) obj;
//            if(empJson.get(KEY_EMPLOYEE_ID).equals(empId.toString())) {
//                System.out.println("EmpId exists!");
//                return true;
//            }
//        }
//
//        return false;
        return idsJson.contains(keyId);
    }

    @Override
    public void close() throws Exception {
        JsonFileHandler.closeJson();
    }

    private JSONObject deleteAndGetByKey(UUID keyId) {
        for (Object obj : idsJson) {
            JSONObject valueJson = (JSONObject) obj;
            if (valueJson.get(valuesName).equals(keyId.toString())) {
                idsJson.remove(valueJson);
                return valueJson;
            }
        }

        return null;
    }

    private JSONObject getJsonObject(UUID keyId) {
        for(Object obj: idsJson) {
            JSONObject empJson = (JSONObject) obj;
            if(empJson.get(keyName).equals(keyId.toString()))
                return empJson;
        }

        return null;
    }
}
