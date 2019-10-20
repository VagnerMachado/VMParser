package pack;

public class Invoke extends Instruction
{
	private int a, b, c;
	
	public Invoke(int i, int j, int k)
	{
		a = i;
		b = j;
		c = k;
	}
	
	public String print()
	{
		return "invoke " + VM.jumpMap.get(a) + ", " + b + ", " + c;
	}
}
