import java.util.List;

public interface IMessage {
	public String toString();
	public String getType(); // returns the number of the command
	public List<String> getParameters(); 
}
