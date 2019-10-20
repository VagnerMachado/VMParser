package pack;

public class Fcmpne extends Compare
{
	private int jump;

	public Fcmpne(int j)
	{
		jump = j;
	}
	
	public String print() 
	{	
		return "fcmpne "+ VM.jumpMap.get(jump);
	}
}
