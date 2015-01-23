package dao.daoImpl.jsonDaoUpd;

import org.json.simple.JSONObject;
import corporationmodules.Project;

import java.util.UUID;

public class ProjJsonDAO extends ModuleJsonDAO<Project>{

    protected static final String FILE_NAME_PROJECT = "projects.json";
    private static final String KEY_PROJECT_ID = "projId";
    private static final String KEY_PROJECT_NAME = "projName";

    protected static boolean projChanged = false;

    private static ProjJsonDAO instance;

    private ProjJsonDAO() {
        super(FILE_NAME_PROJECT, KEY_PROJECT_ID, KEY_PROJECT_NAME);
    }

    public static ProjJsonDAO getInstance() {
        if(instance == null){
            instance = new ProjJsonDAO();
            return instance;
        }

        return instance;
    }

    @Override
    public void add(Project proj) {
        if(!exists(proj)) {
            //create new Json object
            JSONObject newProj = new JSONObject();

            newProj.put(KEY_PROJECT_ID, proj.getId().toString());
            newProj.put(KEY_PROJECT_NAME, proj.getName());

            modulesJson.add(newProj);

            projChanged = true;
        }
    }

    @Override
    public void delete(Project proj) {
        JSONObject projJson = getJsonObjectByModule(proj);
        if(projJson != null) {
            modulesJson.remove(projJson);
            EmpProjJsonDAO.getInstance().deleteValue(proj.getId());

            projChanged = true;
        }
    }

    @Override
    protected Project getModuleFromJsonObject(JSONObject obj) {

        return new Project(UUID.fromString((String) obj.get(KEY_PROJECT_ID)),
                (String) obj.get(KEY_PROJECT_NAME));
    }
}
