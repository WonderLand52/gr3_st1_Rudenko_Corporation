package dao.daoImpl.jsonDaoUpd;

import corporationmodules.Module;
import dao.ModuleDAO;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ModuleJsonDAO<T extends Module> implements ModuleDAO<T> {

    private String keyId;
    private String keyName;

    protected JsonFileHandler handler = new JsonFileHandler();
    protected JSONArray modulesJson;

    public ModuleJsonDAO(String fileName, String keyId, String keyName) {
        this.keyId = keyId;
        this.keyName = keyName;

        try {
            modulesJson = handler.getArrayObjectsFromJson(fileName);
        } catch (DaoSystemException | NoSuchEntityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public T get(UUID id) {

        for(Object obj: modulesJson) {
            JSONObject objJson = (JSONObject) obj;
            UUID moduleId = UUID.fromString((String) objJson.get(keyId));
            if(moduleId.equals(id)){
                return getModuleFromJsonObject(objJson);
            }
        }

        return null;
    }

    @Override
    public T get(String name) {

        for(Object obj: modulesJson) {
            JSONObject objJson = (JSONObject) obj;

            if(name.equals(objJson.get(keyName))){
                return getModuleFromJsonObject(objJson);
            }
        }

        return null;
    }

    @Override
    public List<T> getAll() {

        List<T> modules = new ArrayList<>();
        for(Object obj: modulesJson) {
            modules.add(getModuleFromJsonObject((JSONObject) obj));
        }

        return modules;
    }

    public abstract void delete(T module);

    public boolean exists(T module) {

        for(Object obj: modulesJson) {
            JSONObject objJson = (JSONObject) obj;
            if(objJson.containsValue(module.getId().toString()))
                return true;
        }

        return false;
    }

    @Override
    public void close() throws Exception {
        JsonFileHandler.closeJson();
    }

    protected JSONObject getJsonObjectByModule(T module) {
        for(Object obj: modulesJson) {
            JSONObject moduleJson = (JSONObject) obj;
            if(moduleJson.containsValue(module.getId().toString())) {
                return moduleJson;
            }
        }

        return null;
    }

    protected abstract T getModuleFromJsonObject(JSONObject obj);

}
