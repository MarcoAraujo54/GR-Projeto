/**
* Class to handle timestamps
*
* @author Gustavo Oliveira
* @author José Pedro Fernandes Peleja
* @author Marco Araújo
*
*/
public class Timestamp {

    private long startTimeStamp;

    public Timestamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }
    
    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getElapsedSeconds() {
        return ((System.currentTimeMillis() - this.startTimeStamp)/1000);
    }
}
