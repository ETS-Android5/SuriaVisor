package uci.suriavisor.logic;

/**
 * Created by Miguel on 08/03/2017.
 */

//AVALIBLE = 0, UNAVALIBLE = 1, RECORDING = 2, STREAMING = 3, LOCKED = 4
public enum Status
{
    AVAILABLE("AVAILABLE", 0),
    UNAVAILABLE("UNAVAILABLE", 1),
    RECORDING("RECORDING", 2),
    STREAMING("STREAMING", 3),
    LOCKED("LOCKED", 4);

    private String stringValue;
    private int intValue;
    Status(String toString, int value)
    {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString()
    {
        return stringValue;
    }
}
