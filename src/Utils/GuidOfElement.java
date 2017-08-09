package Utils;

public class GuidOfElement {
    private int guid;

    private int startGuid;

    public GuidOfElement(int numberOfStartGuid) {
        this.startGuid = numberOfStartGuid;
        this.guid = numberOfStartGuid-1;
    }

    public int getGuid() {
        return this.guid;
    }

    public int next() {
        this.guid++;
        return getGuid();
    }

    public int getStartGuid() {
        return startGuid;
    }
}
