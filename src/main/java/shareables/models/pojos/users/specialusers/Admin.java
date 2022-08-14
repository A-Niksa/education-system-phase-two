package shareables.models.pojos.users.specialusers;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.models.pojos.users.UserIdentifier;
import shareables.utils.config.ConfigFileIdentifier;

@JsonTypeName("Admin")
public class Admin extends SpecialUser {
    public Admin() {
        super(UserIdentifier.ADMIN, ConfigFileIdentifier.ADMIN_INFO);
    }
}
