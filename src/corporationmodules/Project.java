package corporationmodules;

import corporationmodules.collaborators.Director;
import corporationmodules.Module;
import dao.IdDAO;
import dao.ModuleDAO;
import dao.daoImpl.jsonDaoUpd.EventJsonDAO;
import exceptions.DaoSystemException;
import exceptions.NoSuchEntityException;

import java.io.Serializable;
import java.util.UUID;

public class Project implements Serializable, Module {

    private final UUID projId;
    private final String name;
    private UUID directorId;

    public Project(String name) {
        this.projId = UUID.randomUUID();
        this.name = name;
    }

    public Project(UUID projId, String name) {
        this.projId = projId;
        this.name = name;

    }

    public Project(UUID projId, String name, Director director, IdDAO dao) {
        this.projId = projId;
        this.name = name;

        try {
            setDirector(director, dao);
        } catch (DaoSystemException e) {
            e.printStackTrace();
        } catch (NoSuchEntityException e) {
            e.printStackTrace();
        }
    }

    public UUID getId() {
        return projId;
    }

    public Director getDirector(ModuleDAO empDAO) throws DaoSystemException, NoSuchEntityException {

        return (Director) empDAO.get(directorId);
    }

    public void setDirector(Director director, IdDAO empProjDAO) throws DaoSystemException, NoSuchEntityException {

        empProjDAO.addValue(director.getId(), this.getId());

        EventJsonDAO.getInstance().addModuleEvent(director, "Director is set: ");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Project name: ");
        sb.append(name);
        sb.append(", Project id: ");
        sb.append(projId);
        return sb.toString();
    }
}
