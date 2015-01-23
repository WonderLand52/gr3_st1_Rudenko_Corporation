package dao.daoImpl.jsonDaoUpd;

import corporationmodules.Project;
import org.json.simple.JSONObject;

import java.util.UUID;

public class ProjectJsonDAO extends ModuleJsonDAO<Project>{

    protected static final String FILE_NAME_PROJECT = "projects.json";
    private static final String KEY_PROJECT_ID = "projId";
    private static final String KEY_PROJECT_NAME = "projName";

    protected static boolean projChanged = false;

    private static ProjectJsonDAO instance;

    private ProjectJsonDAO() {
        super(FILE_NAME_PROJECT, KEY_PROJECT_ID, KEY_PROJECT_NAME);
    }

    public static ProjectJsonDAO getInstance() {
        if(instance == null){
            instance = new ProjectJsonDAO();
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
