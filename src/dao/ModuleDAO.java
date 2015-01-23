package dao;


import corporationmodules.Module;

import java.util.List;
import java.util.UUID;

public interface ModuleDAO<T extends Module> extends AutoCloseable{

    public void add(T module);

    public T get(UUID id);

    public T get(String name);

    public List<T> getAll();

    public void delete(T module);

    public boolean exists(T module);
}
