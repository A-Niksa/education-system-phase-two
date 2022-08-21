package client.locallogic.menus.coursewares;

import shareables.models.pojos.coursewares.EducationalItem;
import shareables.models.pojos.coursewares.ItemType;
import shareables.models.pojos.media.MediaFile;

public class ItemGenerator {
    public EducationalItem generateTextItem(String text) {
        EducationalItem educationalItem = new EducationalItem();
        educationalItem.setText(text);
        educationalItem.setItemType(ItemType.TEXT);
        return educationalItem;
    }

    public EducationalItem generateMediaItem(MediaFile mediaFile) {
        EducationalItem educationalItem = new EducationalItem();
        educationalItem.setMediaFile(mediaFile);
        educationalItem.setItemType(ItemType.MEDIA_FILE);
        return educationalItem;
    }
}
