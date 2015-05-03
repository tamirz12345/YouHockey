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
	
	private boolean checkType(String type , String[] parts) {
		switch (type)
		{
		case "660":
			return (parts.length == 1 );
		case "303":
			return (parts.length == 1 );
		case "661":
			return (parts.length == 2);
		case "301":
			return (parts.length == 3);
		case "300":
			return (parts.length == 1);
		case "900":
			return (parts.length == 2);
		default:
			return false;
			
			
		
		}
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String[] getParameters() {
		
		return params.clone();
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
