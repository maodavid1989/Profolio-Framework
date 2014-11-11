package profolio.base;

public class ExceptionUtil {
    
    public static String toString(Exception e){
	String tmp = e.getMessage();//e.getLocalizedMessage();
	tmp = tmp +"\n"+ getStackTrace(e.getStackTrace());
	return tmp;
    } 
    
    public static String getStackTrace(StackTraceElement[] ste) {
	StringBuilder s = new StringBuilder();

	for (StackTraceElement e : ste) {
	    s.append(e + "\n");
	}
	return s.toString();
    }
}
