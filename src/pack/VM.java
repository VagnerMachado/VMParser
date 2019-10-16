package pack;

import java.util.HashMap;

public class VM extends LexVM
{
protected static HashMap<Integer, Integer> jumpMap = new HashMap<Integer, Integer>();
protected static Instruction [] instructionArray = new Instruction[1000];

	public static void main(String[] args) 
	{
		Stream.setStream(args[0], args[1]);

		String val = getToken();
		int i = 0;
		while(val != null)
		{
			Stream.displayln(i++ + ": " + val);
			val = getToken();
		}
		Stream.close();
	}
	
	public static void printInstructionArray()
	{ 
		int place = 0;
		for (Instruction instruct : instructionArray)
			Stream.displayln(place++ + ": " + instruct.print());
	}

}
