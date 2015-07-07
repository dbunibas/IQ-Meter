package it.unibas.iqmeter.model;

public class EffortRecording {
    
    private int clickRecorded;
    private int keyboardRecorded;

    public EffortRecording(int clickRecorded, int keyboardRecorded) {
        this.clickRecorded = clickRecorded;
        this.keyboardRecorded = keyboardRecorded;
    }

    public int getClickRecorded() {
        return clickRecorded;
    }

    public void setClickRecorded(int clickRecorded) {
        this.clickRecorded = clickRecorded;
    }

    public int getKeyboardRecorded() {
        return keyboardRecorded;
    }

    public void setKeyboardRecorded(int keyboardRecorded) {
        this.keyboardRecorded = keyboardRecorded;
    }
    
    public int getTotalInteraction() {
        return this.clickRecorded + this.keyboardRecorded;
    }
    
    public String toString() {
        return "Recorded operations: "+getTotalInteraction();
    }
    
    public String toLongString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recorded operations: ").append(getTotalInteraction()).append("\n");
        sb.append("\tMouse: ").append(clickRecorded).append(" - Keyboard: ").append(keyboardRecorded);
        return sb.toString();
    }
    
}
