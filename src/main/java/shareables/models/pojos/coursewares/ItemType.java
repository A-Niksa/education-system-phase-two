package shareables.models.pojos.coursewares;

public enum ItemType {
    TEXT("Text"),
    MEDIA_FILE("Media File");

    private String itemTypeString;

    ItemType(String itemTypeString) {
        this.itemTypeString = itemTypeString;
    }

    @Override
    public String toString() {
        return itemTypeString;
    }
}
