package server.database.datamodels.abstractions;

import server.database.datamodels.users.Student;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

public class Department {
    @Id
    private String id;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Student> students;

    public String getId() {
        return id;
    }
}
