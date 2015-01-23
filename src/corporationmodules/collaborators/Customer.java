package corporationmodules.collaborators;

import java.util.UUID;

public class Customer extends Employee {

    public Customer(String name) {
        super(name);
    }

    public Customer(UUID id, String name) {
        super(id, name);
    }

}
