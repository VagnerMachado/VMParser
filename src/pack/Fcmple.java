package pack;

public class Fcmple extends Compare
{
	private int jump;

	public Fcmple(int j)
	{
		jump = j;
	}
	
	public String print() 
	{	
		return "fcmple "+ VM.jumpMap.get(jump);
	}
}
