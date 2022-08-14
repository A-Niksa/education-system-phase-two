package shareables.models.pojos.users.specialusers;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shareables.models.pojos.media.PDF;
import shareables.models.pojos.media.Picture;
import shareables.models.pojos.media.SoundFile;
import shareables.models.pojos.media.Video;
import shareables.models.pojos.users.User;
import shareables.models.pojos.users.UserIdentifier;
import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class),
        @JsonSubTypes.Type(value = MrMohseni.class)
})
public abstract class SpecialUser extends User {
    private ConfigFileIdentifier configIdentifier;

    public SpecialUser() {
    }

    public SpecialUser(UserIdentifier userIdentifier, ConfigFileIdentifier configIdentifier) {
        super(userIdentifier);
        this.configIdentifier = configIdentifier;
        initializeSpecialUser();
    }

    protected void initializeSpecialUser() {
        firstName = ConfigManager.getString(configIdentifier, "firstName");
        lastName = ConfigManager.getString(configIdentifier, "lastName");
        phoneNumber = ConfigManager.getString(configIdentifier, "phoneNumber");
        emailAddress = ConfigManager.getString(configIdentifier, "emailAddress");
        nationalId = ConfigManager.getString(configIdentifier, "nationalId");
        password = ConfigManager.getString(configIdentifier, "password");

        initializeId();
        initializeMessenger(id);
        initializeNotificationsManager(id);
    }

    @Override
    protected void initializeId() {
        id = ConfigManager.getString(configIdentifier, "id");
    }
}
