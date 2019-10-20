package pack;

public class Fcmplt extends Compare
{
	private int jump;

	public Fcmplt(int j)
	{
		jump = j;
	}
	
	public String print() 
	{	
		return "fcmplt "+ VM.jumpMap.get(jump);
	}
}
