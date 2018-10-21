/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hillcipher;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.List;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.vector.SparseMatrix;
import org.jscience.mathematics.number.ModuloInteger;
import org.jscience.mathematics.number.Rational;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import org.jscience.mathematics.vector.Vector;
/**
 *
 * @author norash1995
 */
public class HillKeys{
    
    int b[][] = new int[n][n];
    // Creates a dense matrix (2x2) of rational numbers.
    //modulo nr
    public static int radix;
    //blockSize
    public static int n;
    public static int [][]a;
   

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {

        List <ModuloInteger> list = new ArrayList<>();
        //DenseMatrix<ModuloInteger> matrix = DenseMatrix.valueOf(list);
        ModuloInteger [][]c= new ModuloInteger[n][n];
        
        DenseVector<ModuloInteger> vector = DenseVector.valueOf();


        
        
        Scanner sc = new Scanner(System.in);
        System.out.println("radix:");
        /**modulo of the values**/
        radix = sc.nextInt();
        
       
        /**matrix size nxn**/
        System.out.println("matrix size:");
        n = sc.nextInt();
        
        a = new int[n][n];
        
        //initializing the matrix = K
        for(int i= 0; i<n;i++){
            System.out.print("[ ");
            for(int j = n-1; j>=0; j--){
                Random rand = new Random();
                int value = rand.nextInt(radix);
                
                a[i][j] = value;
                //debugging
                System.out.print(a[i][j]+" ");
            }
            System.out.print("]\n");
        }
        
        
        
        
        
        //decryption
        int det = findDeterminant(n);
        int mInverse= findMultiplicativeInverse(findDeterminant(n), radix);
        System.out.println("multiplicative inverse:"+mInverse);
        
            
    }
    //given blockSize find the determinant of the matrix
    public static int findDeterminant(int blockSize){
        //Finding determinant
        int det;
        
        if(blockSize==2){
            
            det = a[0][1]*a[1][0]-a[1][1]*a[0][0];
            System.out.println("det:"+det);
            
        }
        else{
            det = 0;
        }
        return detModulo(det);
    }
    
    //given determinant find the modulo of a number
    public static int detModulo(int det){
        //taking modulo of det
            if(det<0){
                
                det = det-radix*(det/radix)+radix;
                System.out.println("less than zero det:"+det);
                
            }
            else if(det>=radix){
                
                det = det-radix*(det/radix);
                System.out.println("det:"+det);
                
            }
            else{
                System.out.println("determinant is within modulo");
                
            }
            return det;
    }
    
    public static int findMultiplicativeInverse(int a, int m){
        int i;
        for(i = 0; i<m;i++){
            if((a*i)%m==1){
                return i;
            }
        }
        return i;
    }
}
    
    
