package corporationmodules.collaborators;

import java.util.UUID;

public class Director extends Employee {


    public Director(String name) {
        super(name);
    }

    public Director(UUID id, String name) {
        super(id, name);
    }

}
