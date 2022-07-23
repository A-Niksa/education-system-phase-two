package server.database.datamodels.users;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Set;

@Entity
@Table(name = "PROFESSORS")
public class Professor {
    @Id
    private String id;
    @OneToMany(mappedBy = "advisingProfessor")
    private ArrayList<Student> studentsUnderAdvice;
}
