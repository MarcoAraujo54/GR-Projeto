public class Timestamp {

    private long currentTimeMillis;
    private long startTimeStamp;

    public Timestamp(long startTimeStamp) {
        this.currentTimeMillis = System.currentTimeMillis();
        this.startTimeStamp = startTimeStamp;
    }

    public long getCurrentTimeMillis() {
        return this.currentTimeMillis;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getElapsedSeconds() {
        return ((getCurrentTimeMillis() - startTimeStamp)/1000);
    }
}
