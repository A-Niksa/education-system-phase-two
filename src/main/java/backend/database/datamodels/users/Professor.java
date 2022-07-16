package backend.database.datamodels.users;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

public class Professor {
    @OneToMany(mappedBy = "advisingProfessor", fetch = FetchType.LAZY)
    private Set<Student> studentsUnderAdvice;
}
