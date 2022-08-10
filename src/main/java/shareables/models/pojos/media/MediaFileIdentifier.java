package shareables.models.pojos.media;

public enum MediaFileIdentifier {
    MP3("MP3 File"),
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
