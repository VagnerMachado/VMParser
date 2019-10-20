package pack;

public class Goto extends Instruction
{
	private int jump;
	
	public Goto(int j)
	{
		jump = j;
	}
	
	public String print()
	{
		return "goto "+ VM.jumpMap.get(jump);
	}
}
