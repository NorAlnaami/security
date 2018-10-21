package hillcipher;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.ModuloInteger;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;
import java.io.IOException;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author norash1995
 */
public class HillKeys2 {
    
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("radix:");
        /**modulo of the values**/
        int radix = sc.nextInt();
        
        
        /**matrix size nxn**/
        System.out.println("matrix size:");
        int n = sc.nextInt();
        
        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        
        
        //key matrix file
        System.out.println("keyfile:");
        String fileName= sc.next();
        
        //inverse key matrix file
        System.out.println("invKeyfile:");
        String fileNameInv= sc.next();
        
        
        
        //list of generated moduloIntegers
        List <ModuloInteger> list = new ArrayList<>();        

        //list of denseVectors
        List <DenseVector<ModuloInteger>> listOfVectors = new ArrayList<>(); 
        
        //generating a list of vectors with moduloInteger values
        //size of matrix is nxn
        for(int j = 0; j<n;j++){
            for(int i = 0; i<n; i++){
                //generating moduloIntegers and adding them to list 
                Random rand = new Random();
                int value = rand.nextInt(radix);
                list.add(i, ModuloInteger.valueOf(LargeInteger.valueOf(value)));
            }
            //creating a vector and adding the list of moduloInteger values to the list
            DenseVector<ModuloInteger> vector = DenseVector.valueOf(list);
            
            //clearing the list for next vector input
            list.clear();
            //adding vector to list of vectors
            listOfVectors.add(j,vector);
        }
        
        

        //matrix of moduloIntegers, input list of dense vectors
        //Key Matrix for encyption
        DenseMatrix<ModuloInteger> matrix = DenseMatrix.valueOf(listOfVectors);
        
        //initializing bufferedWriter and setting path for the file the key matrix will be written to 
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter("//Users//norash1995//NetBeansProjects//HillCipher//"+fileName));
        
        for(int i = 0;i<matrix.getNumberOfRows();i++){
            System.out.print("{");
            for(int j = 0; j< matrix.getNumberOfRows();j++){
                System.out.print(matrix.get(i, j)+",");
                //convert each matrix value to string
                //and write it to file
                String data = matrix.get(i, j).toString();
                outputWriter.write(data+" ");
                
            }
            //after each row, write a new line in file
            outputWriter.newLine();
            System.out.print("}\n");
            
        }
        //close and flush the bufferedWriter
        outputWriter.flush();  
        outputWriter.close();
        
        
        
        
        //K^-1= inv(det)*adj(K) where K=matrix
        
        ModuloInteger.setModulus(LargeInteger.valueOf(radix));
        
        //matrix key inverse
        DenseMatrix <ModuloInteger> invMatrix= matrix.inverse();
        
        //catch error of inverse matrix
        
        
        BufferedWriter outputWriterInv = null;
        outputWriterInv = new BufferedWriter(new FileWriter("//Users//norash1995//NetBeansProjects//HillCipher//"+fileNameInv));
        
        for(int i = 0;i<invMatrix.getNumberOfRows();i++){
            for(int j = 0;j<invMatrix.getNumberOfColumns();j++){
                String data = invMatrix.get(i, j).toString();
                outputWriterInv.write(data+" ");
            }
            outputWriterInv.newLine();
        }
        //close and flush the bufferedWriter
        outputWriterInv.flush();  
        outputWriterInv.close();
  
    }
}
