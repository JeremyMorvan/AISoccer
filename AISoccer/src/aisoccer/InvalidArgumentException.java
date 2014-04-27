package aisoccer;
/**
 * @author Sebastien Lentz
 *
 */
@SuppressWarnings("serial")
public class InvalidArgumentException extends Exception
{

    public InvalidArgumentException()
    {
    }

    public InvalidArgumentException(String message)
    {
        super(message);
    }

    public InvalidArgumentException(Throwable cause)
    {
        super(cause);
    }

    public InvalidArgumentException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
