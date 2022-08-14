package shareables.models.pojos.users.specialusers;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.models.pojos.users.UserIdentifier;
import shareables.utils.config.ConfigFileIdentifier;

@JsonTypeName("Mr. Mohseni")
public class MrMohseni extends SpecialUser {
    public MrMohseni() {
        super(UserIdentifier.MR_MOHSENI, ConfigFileIdentifier.MR_MOHSENI_INFO);
    }
}
