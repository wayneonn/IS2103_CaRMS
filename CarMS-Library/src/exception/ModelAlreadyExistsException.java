package exception;



public class ModelAlreadyExistsException extends Exception 
{
    public ModelAlreadyExistsException() 
    {
    }

    
    
    public ModelAlreadyExistsException(String msg) 
    {
        super(msg);
    }
}
