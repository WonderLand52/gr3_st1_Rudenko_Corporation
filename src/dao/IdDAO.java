package dao;

import java.util.List;
import java.util.UUID;

public interface IdDAO extends AutoCloseable {

    public void addValue(UUID keyId, UUID valueId);

    public List<UUID> getValues(UUID keyId);

    public List<UUID> getKeys(UUID valueId);

    public boolean deleteValue(UUID valueId);

    public boolean deleteKey(UUID keyId);

    public boolean keyExists(UUID keyId);
}
