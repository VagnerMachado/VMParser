package pack;

public class Fcmpge extends Compare
{
	private int jump;

	public Fcmpge(int j)
	{
		jump = j;
	}
	
	public String print() 
	{	
		return "fcmpge " + VM.jumpMap.get(jump) ;
	}
}
