package pack;

public class Icmpgt extends Compare
{
	private int jump;

	public Icmpgt(int j)
	{
		jump = j;
	}
	
	public String print()
	{
		return "icmpgt " + VM.jumpMap.get(jump);
	}
}
