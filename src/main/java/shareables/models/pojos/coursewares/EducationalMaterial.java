package shareables.models.pojos.coursewares;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.media.MediaFile;

import java.util.ArrayList;
import java.util.List;

public class EducationalMaterial extends IdentifiableWithTime {
    private String title;
    private List<EducationalItem> educationalItems;

    public EducationalMaterial() {
        educationalItems = new ArrayList<>();
        initializeId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addToEducationalItems(EducationalItem educationalItem) {
        educationalItems.add(educationalItem);
    }

    public void removeFromEducationalItems(String educationalItemId) {
        educationalItems.removeIf(e -> e.getId().equals(educationalItemId));
    }

    public List<EducationalItem> getEducationalItems() {
        return educationalItems;
    }

    public void setEducationalItems(List<EducationalItem> educationalItems) {
        this.educationalItems = educationalItems;
    }
}
