package pack;

public class Fcmpgt extends Compare
{
	private int jump;

	public Fcmpgt(int j)
	{
		jump = j;
	}
	
	public String print() 
	{	
		return "fcmpgt " + VM.jumpMap.get(jump);
	}
}
