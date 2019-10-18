package pack;

import java.util.ArrayList;
import java.util.HashMap;

public class VM extends LexVM
{
	protected static HashMap<Integer, Integer> jumpMap = new HashMap<Integer, Integer>();
	//protected static ArrayList<Instruction> instructionArray = new ArrayList<Instruction>();
	protected static Instruction [] instructionArray = new Instruction[1000];
	protected static int arrayLocation = 0; //for printing the array


	public static void main(String[] args) 
	{

		Stream.setStream(args[0], args[1]);
		String val = getToken();
		String colon = "";
		while(val != null)
		{
			switch(state)
			{
			case SignedInt:  //both signed unsigned can be dealt in same case
			case UnsignedInt:
			{
				int target = Integer.parseInt(val);
				if (target < 0)
				{
					Stream.displayln("Syntax Error: Jump target cannot be negative, parser extracted \"" + target + "\"\n");
					Stream.close();
					return;
				}
				else
				{
					colon = getToken();
					if(colon == null || !LexVM.state.equals(State.Colon) )
					{
						Stream.displayln("Syntax Error: Jump expects colon, parser extracted \"" + colon + "\"\n");
						Stream.close();
						return;
					}
					else
					{
						if(jumpMap.containsKey(target))
						{
							Stream.displayln("Syntax Error: The jump target " + val + " appears more than once\n");
							Stream.close();
							return;
						}
						jumpMap.put(target, arrayLocation);
					}
				}
				break;
			}
			case iconst:
			{
				val = getToken();
				if(state.equals(State.SignedInt) || state.equals(State.UnsignedInt))
				{
					instructionArray[arrayLocation++] = new Iconst(Integer.parseInt(val));
				}
				else if(state.equals(State.FloatE) || state.equals(State.Float))
				{
					instructionArray[arrayLocation++] = new Iconst((int)(Double.parseDouble(val)));
				}
				else
				{
					Stream.displayln("Syntax Error: iconst expects an integer, parser extracted \"" + val + "\"");
					Stream.close();
					return;
				}
				break;
			}
			case iload:
			{
				val = getToken();
				if(state.equals(State.SignedInt) || state.equals(State.UnsignedInt))
				{
					if(Integer.parseInt(val) >= 0)
						instructionArray[arrayLocation++] = new Iload(Integer.parseInt(val));
					else
					{
						Stream.displayln("Syntax Error: iload expects an non negative integer, parser extracted \"" + val + "\"");
						Stream.close();
						return;
					}
				}
				else
				{
					Stream.displayln("Syntax Error: iload expects an non negative integer, parser extracted \"" + val + "\"");
					Stream.close();
					return;
				}
				break;
			}
			case istore:
			{
				val = getToken();
				if(state.equals(State.SignedInt) || state.equals(State.UnsignedInt))
				{
					if(Integer.parseInt(val) >= 0)
						instructionArray[arrayLocation++] = new Istore(Integer.parseInt(val));
					else
					{
						Stream.displayln("Syntax Error: istore expects an non negative integer, parser extracted \"" + val + "\"");
						Stream.close();
						return;
					}
				}
				else
				{
					Stream.displayln("Syntax Error: istore expects an non negative integer, parser extracted \"" + val + "\"");
					Stream.close();
					return;
				}
				break;
			}
			case fconst:
			{
				val = getToken();
				if(state.equals(State.FloatE) || state.equals(State.Float))
				{
					instructionArray[arrayLocation++] = new Fconst(Double.parseDouble(val));
				}
				else if(state.equals(State.SignedInt) || state.equals(State.UnsignedInt))
				{
					instructionArray[arrayLocation++] = new Fconst((double)Integer.parseInt(val));
				}
				else
				{
					Stream.displayln("Syntax Error: fconst expects an float, parser extracted \"" + val + "\"");
					Stream.close();
					return;
				}
				break;
			}
			case fload:
			{
				val = getToken();
				if(state.equals(State.SignedInt) || state.equals(State.UnsignedInt))
				{
					if(Integer.parseInt(val) >= 0)
						instructionArray[arrayLocation++] = new Fload(Integer.parseInt(val));
					else
					{
						Stream.displayln("Syntax Error: fload expects an non negative integer, parser extracted \"" + val + "\"");
						Stream.close();
						return;
					}
				}
				else
				{
					Stream.displayln("Syntax Error: fload expects an non negative integer, parser extracted \"" + val + "\"");
					Stream.close();
					return;
				}
				break;
			}
			case fstore:
			{
				val = getToken();
				if(state.equals(State.SignedInt) || state.equals(State.UnsignedInt))
				{
					if(Integer.parseInt(val) >= 0)
						instructionArray[arrayLocation++] = new Fstore(Integer.parseInt(val));
					else
					{
						Stream.displayln("Syntax Error: fstore expects an non negative integer, parser extracted \"" + val + "\"");
						Stream.close();
						return;
					}
				}
				else
				{
					Stream.displayln("Syntax Error: fstore expects an non negative integer, parser extracted \"" + val + "\"");
					Stream.close();
					return;
				}
				break;
			}
			case iadd:
			{
				instructionArray[arrayLocation++] = new Iadd();
				break;
			}
			case isub:
			{
				instructionArray[arrayLocation++] = new Isub();
				break;
			}
			case imul:
			{
				instructionArray[arrayLocation++] = new Imul();
				break;
			}
			case idiv:
			{
				instructionArray[arrayLocation++] = new Idiv();
				break;
			}
			case fadd:
			{
				instructionArray[arrayLocation++] = new Iadd();
				break;
			}
			case fsub:
			{
				instructionArray[arrayLocation++] = new Fsub();
				break;
			}
			case fmul:
			{
				instructionArray[arrayLocation++] = new Fmul();
				break;
			}
			case fdiv:
			{
				instructionArray[arrayLocation++] = new Fdiv();
				break;
			}
			case intToFloat:
			{
				instructionArray[arrayLocation++] = new IntToFloat();
				break;
			}
			case ireturn:
			{
				instructionArray[arrayLocation++] = new Ireturn();
				break;
			}
			case Return:
			{
				instructionArray[arrayLocation++] = new Return();
				break;
			}
			case freturn:
			{
				instructionArray[arrayLocation++] = new Freturn();
				break;
			}
			default:
				Stream.displayln("Syntax Error:  Unexpected state \"" + state.toString() +"\" reached while parsing \"" +  val + "\"");
				Stream.close();
				return;
			}

			val = getToken();
		}
		System.out.println("Printing Map to console");
		for (HashMap.Entry<Integer,Integer> entry : jumpMap.entrySet())  
			System.out.println("Key = " + entry.getKey() + 
					", Value = " + entry.getValue()); 

		System.out.println("Printing the array to file");
		printInstructionArray();
		Stream.close();
	}




	public static void printInstructionArray()
	{ 
		int place = 0;
		for (int i = 0; i < arrayLocation; i++)
			Stream.displayln(place++ + ": " + instructionArray[i].print());

	}

}
