package Network;

public class ServerChat implements Runnable {
	boolean flag = true;
	
	@Override
	public void run() {
		int i  =0;
		while(flag)
		{
			if (i++ % 10000 == 0)
			{
				System.out.println(i - 1);
			}
		}
		
	}
	public void stop()
	{
		System.out.println("stoped");
		flag = false;
	}

}
