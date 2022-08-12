package shareables.models.pojos.media;

public enum MediaFileIdentifier {
    SOUND_FILE("Sound File"),
    PDF("PDF File"),
    PICTURE("Picture"),
    VIDEO("Video");

    private String mediaFileIdentifierString;

    MediaFileIdentifier(String mediaFileIdentifierString) {
        this.mediaFileIdentifierString = mediaFileIdentifierString;
    }

    @Override
    public String toString() {
        return mediaFileIdentifierString;
    }
}
