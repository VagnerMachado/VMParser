package pack;

public class Fcmpeq extends Compare
{
	private int jump;

	public Fcmpeq(int j)
	{
		jump = j;
	}
	
	public String print() 
	{	
		return "fcmpeq " + VM.jumpMap.get(jump) ;
	}
}
