package pack;

public class Goto extends Instruction
{
	private int location;
	
	public Goto(int l)
	{
		location = l;
	}
	
	public String print()
	{
		return "goto" + location;
	}
}
