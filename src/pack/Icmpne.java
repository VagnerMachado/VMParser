package pack;

public class Icmpne extends Compare
{
	private int jump;

	public Icmpne(int j)
	{
		jump = j;
	}
	
	public String print()
	{
		return "icmpne " + VM.jumpMap.get(jump);
	}
}
