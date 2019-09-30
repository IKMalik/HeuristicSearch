import java.util.*;
import java.io.*;


public class HelloWorld{

     public static void main(String []args)
     {
        ArrayList<Double> test = new ArrayList<Double>();
        test.add(1.0);
        test.add(2.0);
        test.add(3.0);
        test.add(4.0);
        test.add(10.0);

        ArrayList<Character> result = HelloWorld(test, 50);
        System.out.println(Arrays.toString(result.toArray()));
     }
     
     public static ArrayList<Character> HelloWorld(ArrayList<Double> weights, int iter)
     {
         if (weights == null || iter < 1)
         {
             return null;
         }
         
         ScalesSolution s1 = new ScalesSolution(weights.size());
         s1 = s1.RMHC(weights, weights.size(), iter);
         
         String result = new String();
         
         ArrayList<Character> returnArray = new ArrayList<Character>();
         result = s1.GetSol();
         
         for (char ch: result.toCharArray())
         {
             returnArray.add(ch);
         }
         
         return returnArray;
         
     }

}     
class ScalesSolution
{     
	private String scasol;
	
	public static ScalesSolution RMHC(ArrayList<Double> weights, int n, int iter)
	{
	    ScalesSolution sol = new ScalesSolution(n);
	    
	    for(int i=0;i<iter;i++)
	    {
	        
	        String random_string = sol.SmallChange();
	        Double oldsol = sol.ScalesFitness(weights);
	        String oldstr = sol.scasol;
	        
	        sol.scasol = random_string;
	        
	        Double newsol = sol.ScalesFitness(weights);
	        
	        if (newsol > oldsol)
	        {
	            sol.scasol = oldstr;
	        }
	 
	    }
	    return sol;
	}
	
	public String GetSol()
	{
	    return scasol;
	}
	
	public String SmallChange()
	{
	    
	    int random_val = CS2004.UI(0,scasol.length()-1);
	    
	    String x = new String();
	    
	    x += scasol.substring(0, random_val);
	    
	    if (scasol.charAt(random_val) == '0')
	    {
	        x += '1';
	    }
	    else
	    {
	        x += '0';
	    }
	    
	    x += scasol.substring(random_val+1, scasol.length());
	    
	    return x;
	    
	}
	
	
	
	//Creates a new scales solution based on a string parameter
	//The string parameter is checked to see if it contains all zeros and ones
	//Otherwise the random binary string generator is used (n = length of parameter)
	
	public ScalesSolution(String s)
	{
		boolean ok = true;
		int n = s.length();
		for(int i=0;i<n;++i)
		{
			char si = s.charAt(i);
			if (si != '0' && si != '1') ok = false;
		}
		if (ok)
		{
			scasol = s;
		}
		else
		{
			scasol = RandomBinaryString(n);
		}
	}
	private static String RandomBinaryString(int n)
	{
		String s = new String();
		for(int i=0;i<n;i++)
		{
		    s += CS2004.UI(0,1);
		}
		
		return(s);
	}
	public ScalesSolution(int n) 
	{
		scasol = RandomBinaryString(n);	
	}
	//This is the fitness function for the Scales problem
	//This function returns -1 if the number of weights is less than
	//the size of the current solution
	public double ScalesFitness(ArrayList<Double> weights)
	{
		if (scasol.length() > weights.size()) return(-1);
		double lhs = 0.0,rhs = 0.0;
		int n = scasol.length();
		
		for (int i=0;i<n;i++)
		{
		    if (scasol.charAt(i) == '0')
		    {
		        lhs += weights.get(i);
		    }
		    else
		    {
		        rhs += weights.get(i);
		    }
		}
        
		return(Math.abs(lhs-rhs));
	}
	//Display the string without a new line
	public void print()
	{
		System.out.print(scasol);
	}
	//Display the string with a new line
	public void println()
	{
		print();
		System.out.println();
	}
}

class CS2004 
{
	//Shared random object
	static private Random rand;
	//Create a uniformly distributed random integer between aa and bb inclusive
	static public int UI(int aa,int bb)
	{
		int a = Math.min(aa,bb);
		int b = Math.max(aa,bb);
		if (rand == null) 
		{
			rand = new Random();
			rand.setSeed(System.nanoTime());
		}
		int d = b - a + 1;
		int x = rand.nextInt(d) + a;
		return(x);
	}
	//Create a uniformly distributed random double between a and b inclusive
	static public double UR(double a,double b)
	{
		if (rand == null) 
		{
			rand = new Random();
			rand.setSeed(System.nanoTime());
		}
		return((b-a)*rand.nextDouble()+a);
	}
	//This method reads in a text file and parses all of the numbers in it
	//This code is not very good and can be improved!
	//But it should work!!!
	//It takes in as input a string filename and returns an array list of Doubles
	static public ArrayList<Double> ReadNumberFile(String filename)
	{
		ArrayList<Double> res = new ArrayList<Double>();
		Reader r;
		try
		{
			r = new BufferedReader(new FileReader(filename));
			StreamTokenizer stok = new StreamTokenizer(r);
			stok.parseNumbers();
			stok.nextToken();
			while (stok.ttype != StreamTokenizer.TT_EOF) 
			{
				if (stok.ttype == StreamTokenizer.TT_NUMBER)
				{
					res.add(stok.nval);
				}
				stok.nextToken();
			}
		}
		catch(Exception E)
		{
			System.out.println("+++ReadFile: "+E.getMessage());
		}
	    return(res);
	}
}
