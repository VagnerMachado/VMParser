package pack;

public class Icmpeq extends Compare
{
	private int jump;

	public Icmpeq(int j)
	{
		jump = j;
	}
	
	public String print()
	{
		return "icmpeq " + VM.jumpMap.get(jump);
	}
}
