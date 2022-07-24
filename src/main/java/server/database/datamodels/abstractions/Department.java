package server.database.datamodels.abstractions;

import server.database.datamodels.users.Professor;
import server.database.datamodels.users.Student;

import javax.persistence.*;
import java.util.List;

public class Department {
    private static int sequentialId = 0;

    @Id
    private String id;
    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST)
    @JoinTable(name = "Department_Professor")
    private List<Professor> professors;
    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST)
    @JoinTable(name = "Department_Student")
    private List<Student> students;
    @OneToOne(mappedBy = "department", cascade = CascadeType.PERSIST)
    @JoinColumn(name = "dean_id")
    private Professor dean;
    @OneToOne(mappedBy = "department", cascade = CascadeType.PERSIST)
    @JoinColumn(name = "deputy_id")
    private Professor deputy;
    public String getId() {
        return id;
    }
}
