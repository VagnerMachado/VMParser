package pack;

public class Icmple extends Compare
{
	private int jump;

	public Icmple(int j)
	{
		jump = j;
	}
	
	public String print()
	{
		return "icmple " + VM.jumpMap.get(jump);
	}
}
