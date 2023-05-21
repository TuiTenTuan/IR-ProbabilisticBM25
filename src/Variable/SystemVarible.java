package Variable;

public class SystemVarible {
    public static String GetBaseApplicationPath()
    {
        return System.getProperty("user.dir");
    }

    public static String GetNewLineChar()
    {
        return System.getProperty("line.separator");
    }

}
