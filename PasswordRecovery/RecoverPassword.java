//------------------------------------------------------------------
// University of Central Florida
// CIS3360 - Fall 2017
// Program Author: William Askew
//------------------------------------------------------------------

import java.io.*;
import java.util.*;

public class RecoverPassword
{
  public static void main(String[] args)
  {
    File in = new File(args[0]);
    String temp;
    String filename = args[0];
    String saltedValue = args[1];
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(in));

      ArrayList<String> dictionary = new ArrayList<String>();

      while ((temp = br.readLine()) != null)
        dictionary.add(temp);

      ArrayList<String> ascii = new ArrayList<String>();
      ascii = get_Ascii_Value(dictionary);

      header(dictionary, ascii, filename, saltedValue);
    }
    catch (Exception e) { e.printStackTrace(); }
  }

  public static ArrayList<String> get_Ascii_Value(ArrayList<String> dict)
  {
    ArrayList<String> asciiVal = new ArrayList<String>();

    for(String s : dict)
    {
      String asciiEquiv = "";

      for(int i = 0; i < s.length(); i++)
        asciiEquiv += (int)s.charAt(i);
      asciiVal.add(asciiEquiv);
    }
    return asciiVal;
  }

  public static void findPassword(ArrayList<String> dict, ArrayList<String> ascii, String targetHash)
  {
    ArrayList<String> temp = new ArrayList<String>(ascii);
    long target = Long.parseLong(targetHash);
    int n = 0;
    int x = 0;
    long hash = 0;
    String salt = "";
    String tempString = "";

    for(String s : temp)
    {
      for(int i = 0; i < 1000; i++)
      {
        n++;
        salt = String.format("%03d", i);
        tempString = salt + s;

        hash = getHash(tempString);

        if(Long.compare(hash, target) == 0)
        {
          System.out.println("\nPassword recovered:");
          System.out.println("   Password            : " + dict.get(x));
          System.out.println("   ASCII value         : " + ascii.get(x));
          System.out.println("   Salt value          : " + salt);
          System.out.println("   Combinations tested : " + n);
          return;
        }
        tempString = s;
      }
      x++;
    }

    System.out.println("\nPassword not found in dictionary\n");

    System.out.println("Combinations tested: " + n);
  }

  public static Long getHash(String s)
  {
    long left = Long.parseLong(s.substring(0,7));
    long right = Long.parseLong(s.substring(7,15));
    long hash = ((243*left) + right) % 85767489;

    return hash;
  }

  public static void header(ArrayList<String> dict, ArrayList<String> ascii, String file, String hash)
  {
    int n = 1;
    for(int i = 0; i < 41; i++)
      System.out.print("-");
    System.out.println("\n");

    System.out.println("CIS3360 Password Recovery by William Askew");

    System.out.println("\n   Dictionary file name       : " + file);
    System.out.println("   Salted password hash value : " + hash);

    System.out.println("\nIndex   Word   Unsalted ASCII equivalent");
    System.out.println();

    while(n <= dict.size() && dict.iterator().hasNext() && ascii.iterator().hasNext())
    {
    System.out.printf("%4s :  %s %s\n", n, dict.get(n-1), ascii.get(n-1));
      n++;
    }

    findPassword(dict, ascii, hash);
  }
}
