package pack;

public class Fconst extends Instruction
{
	float constant; //must work for float and float E

	public Fconst(float c)
	{
		constant = c;
	}
	
	public String print() 
	{	
		return "fconst " + constant ;
	}

}
