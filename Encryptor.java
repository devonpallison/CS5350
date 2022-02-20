import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
 
import java.lang.Math;

public class Encryptor {
   private static final float r = 3;//r is the control parameter in the range [0,4]

   private enum Algorithm {
      AES,
      ASLBTM,
      CTM
    }
    
    public static void main(String[] args)
        throws FileNotFoundException, IOException
    {
        Algorithm chosenAlg = Algorithm.ASLBTM;
                            
        // Selecting a Image for operation
        FileInputStream fis = new FileInputStream("C:\\projects\\wsEclipse\\bleh\\Lenna.png");
                            
        // Converting Image into byte array, create a
        // array of same size as Image size
                            
        byte data[] = new byte[fis.available()];
                            
        // Read the array
        fis.read(data);
        int i = 0;
        

        int key = 0;
                            
        // Performing an XOR operation on each value of
        // byte array due to which every value of Image
        // will change.
        for (byte b : data) {
           if (chosenAlg == Algorithm.ASLBTM){
               key = ASLBTM(b);
           } else if (chosenAlg == Algorithm.CTM) {
               key = CTM(b);
           } else {
              key = 1;//TODO AES
           }
           
           data[i] = (byte)(b ^ key);
           i++;
        }
                            
        // Opening a file for writing purpose
        FileOutputStream fos = new FileOutputStream("C:\\projects\\wsEclipse\\bleh\\LennaOut.png");
                            
        // Writing new byte array value to image which
        // will Encrypt it.
                            
        fos.write(data);
                            
        // Closing file
        fos.close();
        fis.close();
        System.out.println("Encryption Done...");
    }

    private static int ASLBTM(byte b) {
      int ret;
      if (b < 0.5){
         ret = (int) (4-r/4*Math.sin(3.14159*b)+ r/2*b);
      } else {
         ret = (int) ((4-r)*b*(1-b)+r/2*(1-b));
      }
      return ret;
    }

    private static int CTM(byte b) {
      int ret;
      if (b < 0.5){
         ret = (int) (4-3/4*r*b*(1-b^2)+r/2*b%1);
      } else {
         ret = (int) (4-3/4*r*b*(1-b^2)+r/2*(1-b)%1);
      }
      return ret;
    }
}