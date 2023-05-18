import processing.core.PApplet;

public class Timer {
    private PApplet p;
    //The start time
    private int startTime;
    //The end time
    private int endTime;

    //constructor for Timer class
    public Timer (PApplet p) {
        this.p = p;
        startTime = 0;
        endTime = -1;
    }
    //Returns the time
    public int getTime () {
        if (startTime == 0)
            return 0;
        if (endTime != 0)
            return endTime-startTime;
        return p.millis() - startTime;
    }
    //Starts the timer
    public void start () {
        startTime = p.millis();
    }
    //Ends the timer
    public void end () {
        endTime = p.millis();
    }
    //Resets the startTime and endTime to 0
    public void reset () {
        startTime = 0;
        endTime = 0;
    }
    //Returns the current endtime
    public int getEndTime() {
        return endTime;
    }
}
