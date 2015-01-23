package corporationmodules.collaborators;

import java.io.Serializable;
import java.util.UUID;

public class Worker extends Employee implements Serializable {

    public Worker(String name) {
        super(name);
    }

    public Worker(UUID id, String name) {
        super(id, name);
    }
}
