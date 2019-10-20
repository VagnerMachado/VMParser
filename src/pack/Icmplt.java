package pack;

public class Icmplt extends Compare
{
	private int jump;

	public Icmplt(int j)
	{
		jump = j;
	}
	
	public String print()
	{
		return "icmplt " + VM.jumpMap.get(jump);
	}
}
