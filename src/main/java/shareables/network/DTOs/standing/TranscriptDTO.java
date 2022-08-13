package shareables.network.DTOs.standing;

public class TranscriptDTO {
    private int numberOfPassedCredits;
    private String GPAString;

    public TranscriptDTO() {
    }

    public int getNumberOfPassedCredits() {
        return numberOfPassedCredits;
    }

    public void setNumberOfPassedCredits(int numberOfPassedCredits) {
        this.numberOfPassedCredits = numberOfPassedCredits;
    }

    public String getGPAString() {
        return GPAString;
    }

    public void setGPAString(String GPAString) {
        this.GPAString = GPAString;
    }
}
