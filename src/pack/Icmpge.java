package pack;

public class Icmpge extends Compare
{
	private int jump;

	public Icmpge(int j)
	{
		jump = j;
	}
	
	public String print()
	{
		return "icmpge " + VM.jumpMap.get(jump);
	}
}
