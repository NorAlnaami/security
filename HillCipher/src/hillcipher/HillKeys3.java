/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hillcipher;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.ModuloInteger;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;

/**
 *
 * @author norash1995
 */
public class HillKeys3 {
    
    public static List <ModuloInteger> list;
    public static List <DenseVector<ModuloInteger>> listOfVectors;
    public static int radix;
    public static int n;
    private static String fileName;
    private static String fileNameInv;
    private static DenseMatrix<ModuloInteger> matrix;
    private static DenseMatrix <ModuloInteger> invMatrix;
    
     public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("radix:  matrix size:    keyfile:    invkeyfile:");
        /**modulo of the values**/
        radix = sc.nextInt();
        if(radix>256){
            throw new IllegalArgumentException("maximum radix is 256");
        }
        /**matrix size nxn**/
        n = sc.nextInt();
        if(n>8){
            throw new IllegalArgumentException("maximum blockSize is 8");
        }
        /**System.out.println("Working Directory = " +
              System.getProperty("user.dir"));**/
        
        
        //key matrix file
        fileName= sc.next();
        
        //inverse key matrix file
        fileNameInv= sc.next();
        
        
        
        //list of generated moduloIntegers
        list = new ArrayList<>();
        
        //list of denseVectors
        listOfVectors = new ArrayList<>(); 
        
        //generate a list of vectors and store them in listOfVectors
        generator();
        
        inputFile(fileName,matrix);
        
        
        
        
        //K^-1= inv(det)*adj(K) where K=matrix
        // set Modulus of inverse
        ModuloInteger.setModulus(LargeInteger.valueOf(radix));
        
        boolean success = false;
        
        //catch error of inverse matrix
        while(!success){
            try{
                //matrix key inverse
                invMatrix= matrix.inverse();
                inputFile(fileNameInv, invMatrix);
                success = true;
                
            }
            catch(Exception e){
                generator();
                inputFile(fileName, matrix);
                
            }
            
        
        }
        System.out.println(matrix.toString()+"\n");
        System.out.println(invMatrix.toString());
        
  
    }
     
     public static void generator(){
         
         
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
        matrix = DenseMatrix.valueOf(listOfVectors);
        listOfVectors.clear();
        
        
     }
     
     public static void inputFile(String filename, DenseMatrix<ModuloInteger> matrixTest) throws IOException{
         //initializing bufferedWriter and setting path for the file the key matrix will be written to 
        BufferedWriter outputWriter = null;
        outputWriter = new BufferedWriter(new FileWriter("//Users//norash1995//NetBeansProjects//HillCipher//"+filename));
        
        for(int i = 0;i<matrixTest.getNumberOfRows();i++){
            //System.out.print("{");
            for(int j = 0; j< matrixTest.getNumberOfRows();j++){
                //System.out.print(matrixTest.get(i, j)+",");
                //convert each matrix value to string
                //and write it to file
                String data = matrixTest.get(i, j).toString();
                outputWriter.write(data+" ");
                
            }
            //after each row, write a new line in file
            outputWriter.newLine();
            //System.out.print("}\n");
            
        }
        //close and flush the bufferedWriter
        outputWriter.flush();  
        outputWriter.close();
        System.out.print("\n");
     }
}
