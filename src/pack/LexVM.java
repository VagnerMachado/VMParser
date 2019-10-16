package pack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/************************************************************************************
 * LexVM - This class provides the driver for a lexical analyzer for tokens defined
 * in the following grammar:
 * 
 * <digit> 		  -> 0 | 1 | ... | 9
 * <unsigned int> -> { <digit> }+
 * <signed int>	  -> (+|-) { <digit> }+
 * <float>		  -> [+|-] ( { <digit> }+ "." { <digit> } | "." { <digit> }+ )
 * <floatE>		  -> <float> (e|E) [+|-] { <digit> }+
 * <instruction>  -> "iconst" | "iload" | "istore" | "fconst" | "fload" | "fstore" |
 *                  "iadd" | "isub" | "imul" | "idiv" | "fadd" | "fsub" | "fmul" | "fdiv" |
 *                  "intToFloat" | "icmpeq" | "icmpne" | "icmplt" | "icmple" | "icmpgt" |
 *                  "icmpge" |  "fcmpeq" | "fcmpne" | "fcmplt" | "fcmple" | "fcmpgt" | "fcmpge" | 
 *                  "goto" | "invoke" | "return" | "ireturn" | "freturn" | "print"
 * <colon> -> ":"
 * <comma> -> ","
 * 
 *  The lexical analyzer is implemented through a DFA that will accept the above tokens.
 *	The DFA states are represented by the Enum type State, located in file State.java
 *
 *  *** The DFA has the following states represented by enum-type literals ***
 *  
 *  A) Accepted states 
 *		STATE			ACCEPTS
 *		Colon			:
 *		Comma			,
 *		Id				Letters: a-z, A-Z
 *		SignedInt		Signed Integers
 *		UnsignedInt		Unsigned Integers
 *		Float			Float numbers without exponentiation
 *		FloatE			Float numbers with exponentiation
 *	
 *	A.1) If accept state is Id, then it is checked for following operator
 *		 states, also represented as enum-type literals:
 *		STATE			OPERATION
 *		iconst  		push integer constant
 *		iload  			push integer value from memory
 *		istore          pop value to memory
 *		fconst          push float constant
 *		fload  		    push float value from memory
 *		fstore          pop float value to memory
 *      iadd            addition of integers
 *      isub  			subtraction of integers
 *      imul  			multiplication of integers
 *      idiv  			division of integers
 *      fadd            addition of floats
 *      fsub  			subtraction of floats
 *      fmul            multiplication of floats
 *      fdiv            division of floats
 *      intToFloat      parse integer to float
 *      icmpeq          integer comparison for ==
 *      icmpne  		integer comparison for !=
 *      icmplt          integer comparison for <
 *      icmple  		integer comparison for <=
 *      icmpgt 			integer comparison for >
 *      icmpge   		integer comparison for >=
 *      fcmpeq  		float comparison for ==
 *      fcmpne  		float comparison for !=
 *      fcmplt  		float comparison for <
 *      fcmple  		float comparison for <=
 *      fcmpgt  		float comparison for >
 *      fcmpge  		float comparison for >=
 *      goto  			unconditional jump
 *      invoke  		method call
 *      return  	    void return
 *      ireturn         integer return
 *      freturn         float return
 *      print           print value
 *	
 *  B) Non-final states:
 *  	STATE			TOKEN
 *  	Start      		the empty string
		Period     		a float that starts with period
		E          		float ending with E or e
		EPlusMinus 		float ending with + or - in exponentiation part
		Plus			Signed float or integer starting with +
 *  	Minus			Signed float or integer starting with -
 *  
 *  C) The DFA has a undefined state denoted as State.UNDEF which signals that
 *     the current token generated an undefined transition.
 *		
 *
 *	Javadocs for the methods in LexVM.java and other classes can be found 
 *  on top of the class/enum declaration and over their method signatures
 *  
 *  ***************************** PLEASE READ ***************************************
 *  
 *  IMPORTANT: In order to allow for extra functionality when parsing, please
 *  read the comment on line 160 of State.java. Commenting out the mentioned block
 *  will allow for parsing of consecutive valid operators not separated by spaces.
 *  i.e iconstimulreturninvoke would be parsed as iconst imul return invoke. 
 *  Commenting out the block in State.java would, however, fail to pass the test
 *  for input file #2 provided because it has 'fconstt' as input. Commenting off
 *  the said block would parse this as Fconst for 'fconst' and invalid token for the last 't'.
 *  Since this does not match the expected output for input 2, the block was 
 *  commented off by default. To pass the "ultimate test" provided in file 
 *  input8.txt, please comment off the block on line 160 of State.java and compare it
 *  to expected8.txt file. 
 *  
 *      Parse safely :)          
 *      -Vagner
 *  
 *  ***************************** INPUT / OUTPUT ***********************************
 *  
 *  This program requires input as command line arguments in order to run properly:
 *  
 *  argv[0] - must be a valid file name with input to be parsed
 *  argv[1] - a valid file name for the parsed output to be written to
 *  argv[2] - OPTIONAL: a file with expected output to be compared to generated output
 *            for details, check the Javadocs for comapareOutput() in LexVM.java
 *
 *
 * **********************************************************************************
 *
 *   @author ProfessorYukawa  -  Edited by VagnerMachado - QCID 23651127 - Fall 2019
 * 
 ************************************************************************************
 */
public abstract class LexVM 
{
	public static String tokens; //holds current concatenated tokens
	public static State state;   //holds current state of DFA

	public static void extract()
	{		
		// argv[0]: input file containing tokens defined above
		// argv[1]: output file displaying a list of the tokens and categories
		//initialize a Stream with files passed as arguments
		/*if(argv.length < 2)
		{
			System.out.println("\n\n ******************************* INPUT / OUTPUT ERROR ***********************************\n\n" + 
					" This program requires input as command line arguments in order to run properly:\n" +
					" argv[0] - must be a valid file name with input to be parsed\r\n" + 
					" argv[1] - a valid file name for the parsed output to be written to\r\n" + 
					" argv[2] - OPTIONAL: a file with expected output to be compared to generated output.\r\n" + 
					"                     For details, check the Javadocs for compareOutput() in LexVM.java\n\n" +
					" ** If running project in Eclipse, plese import project as described here: \n" +
					" \t https://www.codejava.net/ides/eclipse/import-existing-projects-into-eclipse-workspace \n" +
					" \t For the Eclipse option, the input files and matching expected output files are zipped in.\n\n"+
					" ** If running on command line, enter the following commands inside folder 'pack' **\n" +
					" \t javac *.java <enter>\n\t cd .. <enter>\n\t java pack.LexVM <inputFile.txt> <outputFile.txt> " +
					"<expectedOutput.txt> <enter> \n  \n\t\t******* SUBSTITUTE THE FILE NAMES ACCORDINGLY ********\n\n" +
					"NOTE: For command line, the parameter files, MUST be at the same level as 'pack' not inside it\n\n" +
					" *****************************************************************************************");
			return;
		}
		Stream st = new Stream(argv[0], argv[1]);*/
		int i;

		while ( Stream.intToken != -1 ) // while is not end-of-stream
		{
			i = driver();    // extract the next token
			if (i == 1)	     //valid category found                 		
				Stream.displayln( tokens.trim() + "\t  : " + state.toString().substring(0,1).toUpperCase()
						+ state.toString().substring(1));
			else if (i == 0) //invalid token, lexical error
				Stream.displayln( tokens.trim() + "\t  : Lexical Error, invalid token");
		} 
		//close files in stream


		//optional method that will run if an expected output file is passed as parameter
		//if(argv.length == 3)
		//	compareOutputs(argv[1], argv[2]);
	}

	/**
	 * This is the driver of the FA. 
	 * If a valid token is found, assigns it to "tokens" and returns 1.
	 * @return - If an invalid token is found:
	 * 		a) If current state is Id with invalid operator: assigns it to "tokens" and returns 0.
	 * 		b) If current state is Id with valid operator: return 0.
	 * @return - If state is Final, return 1
	 * @return - If end-of-stream is reached without finding any non-whitespace character, returns -1.
	 */
	private static int driver()
	{

		State nextSt; 	// the next state of the FA

		tokens = ""; 	//concatenated tokens
		state = State.Start;

		if (Character.isWhitespace((char) Stream.intToken) )
			Stream.intToken = Stream.getChar();       // get the next non-whitespace character
		if ( Stream.intToken == -1 )				  // end-of-stream is reached
			return -1;

		while ( Stream.intToken != -1 ) // do the body if "intToken" is not end-of-stream
		{
			Stream.charToken = (char) Stream.intToken;// parse int to char
			nextSt = State.nextState(state, Stream.charToken ); //get next state based on char
			if ( nextSt == State.UNDEF ) 			  // The current DFA operation will halt.
			{
				if (State.isFinal(state)) 			  // valid token extracted
					return 1; 
				else if(state.equals(State.UNDEF))    //invalid function name found in State checkState() i.e floAT90
					return 0;                      	  //just returning allows for 90 to be parsed as unsigned int

				else  		   	 // "charToken" is a unexpected character
				{
					tokens = tokens + Stream.charToken;
					Stream.intToken = Stream.getNextChar();
					return 0;	 // invalid token found
				}
			}
			else 				 // The DFA will go on.
			{
				state = nextSt;
				tokens = tokens + Stream.charToken;
				Stream.intToken = Stream.getNextChar();
			}
		}

		if (State.isFinal(state))  // end-of-stream is reached while a token is being extracted
			return 1; 			   // valid token extracted
		else
			return 0;              // invalid token found

	} // end driver

	/**
	 * getToken - Extracts the next token using the driver of the FA.
	 * If an invalid token is found, issue an error message.
	 * Seems it is not being used for this program as token extraction occurs in Stream class
	 */
	public static String getToken()
	{
		int i = driver();
		if (i == 0)
		{
			Stream.displayln(tokens + "\t: Lexical Error, invalid token");
			return null;
		}
		else if (i == -1)
			return null;
		else
			return tokens;
	}

	/**
	 * compareOutput - compares the expected and given outputs for program.
	 * It was used to compare the seven sample outputs with the outputs 
	 * given by program. It compares tokens delimited by spaces and halts
	 * on the fidiscrepancy and prints on console.
	 * 
	 * NOTE: in order for output based on input8.txt to match expected8.txt,
	 * the block on line 160 int State.java MUST be commented off. Refer to class
	 * comment on top of LexVM.java file  for more information.
	 * @param generated - the file name with generated lexical analysis
	 * @param expected - the file name with expected lexical analysis
	 */
	public static void compareOutputs(String generated, String expected)
	{
		File g = new File(generated);
		File e = new File(expected);

		Scanner sg = null;
		Scanner se = null;
		try
		{
			se = new Scanner(e);
			sg = new Scanner(g);
		} catch (FileNotFoundException e1) 
		{
			System.out.println("\n\n *** ERROR: The program was not able to open the file with expected output ***\n\n");
			e1.printStackTrace();
		}

		String gen = ""; String exp = "";
		while (se.hasNext() && sg.hasNext())
		{
			gen = sg.next();
			exp = se.next();

			if(!exp.equals(gen)) 
			{
				System.out.println("\n*** ERROR: \'" + gen + "\' in " + generated + " does not match \'" 
						+ exp + "\' in " + expected + " ***\n\n");
				return;
			}

		}
		if (sg.hasNext() && !se.hasNext())
		{
			gen = sg.nextLine();
			while(sg.hasNextLine())
				gen += "\n" + sg.nextLine();
			System.out.println("\n*** ERROR: " + "generated output has extra token(s) in the end ***\n\n" + gen );

		}
		else if (!sg.hasNext() && se.hasNext())
		{
			exp = se.next();
			while(se.hasNext())
				exp += "\n" + se.next();
			System.out.println(" *** ERROR: " + "expected output has extra token(s) in the end ***\n\n" + exp );
		}
		else
			System.out.println("\n *** Expected output in " + expected + " matches the program output in " + generated + " ***\n\n");
	}	
} 
