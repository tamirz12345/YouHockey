package Network;

public class Message implements IMessage {
	private String type;
	private String[] params;
	public Message(String msg)
	{
		String[] temp = msg.split("-");
		if (! checkType(temp[0] , temp) || temp.length == 0)
			return;//Unvalid msg
		type = temp[0];
		int num = temp.length - 1 ;
		params = new String[num];
		for (int i =  1  ; i < temp.length ; i++)
			params[i - 1] = temp[i];
		
	}
	
	private boolean checkType(String string , String[] parts) {
		
		return true;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String[] getParameters() {
		
		return params;
	}
	
	
	
	public String toString()
	{
		String str = type + "-";
		for (String p : params)
		{
			str += p + "-";
		}
		return str;
	}

}
