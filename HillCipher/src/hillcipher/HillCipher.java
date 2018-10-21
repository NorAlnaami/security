/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hillcipher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
public class HillCipher {
    public static void main(String[] args) throws FileNotFoundException, IOException{
        Scanner sc = new Scanner(System.in);
        System.out.println("<radix>: <blocksize>: <keyfile>: <plainfile>: <cipherfile>: ");
        int radix = sc.nextInt();
        ModuloInteger.setModulus(LargeInteger.valueOf(radix));
        
        int blockSize = sc.nextInt();
        
        String keyfile = sc.next();
        String plainfile = sc.next();
        String cipherfile = sc.next();
        File fileKey = new File("//Users//norash1995//NetBeansProjects//HillCipher//"+keyfile);
        sc = new Scanner(fileKey);
        
        
        
        List<DenseVector<ModuloInteger>> listOfVectors = new ArrayList<>();
        
        while(sc.hasNextLine()){
            
            Scanner colReader = new Scanner(sc.nextLine());
            ArrayList<ModuloInteger> col = new ArrayList();
            while(colReader.hasNextInt())
            {
                col.add(ModuloInteger.valueOf(LargeInteger.valueOf(colReader.nextInt())));
            }
            DenseVector<ModuloInteger> vector = DenseVector.valueOf(col);
            listOfVectors.add(vector);
            
            
        }
        sc.close();

        
        DenseMatrix<ModuloInteger> matrix = DenseMatrix.valueOf(listOfVectors);
        
        System.out.println("matrix: "+matrix.toString());
        
        //Reading the plain text
        File textEnc = new File("//Users//norash1995//NetBeansProjects//HillCipher//"+plainfile);
        sc = new Scanner(textEnc);
        
        String s= sc.nextLine();
        System.out.println(s);
        
        int sLength= s.length();
        System.out.println("sLength: "+sLength);
        
        
        int inputRest= sLength%blockSize;
        System.out.println("inputRest: "+inputRest);
        
        
        int nrOfpadding = blockSize - inputRest;
        String concat= s;
        //Padding
        if(inputRest>0){
            for(int i = 0;i <nrOfpadding;i++){
                concat = concat.concat(String.valueOf(nrOfpadding));
            }
            System.out.println("stringPAdded: "+concat);
        }
        int inputMul= concat.length()/blockSize;
        System.out.println("input multiple: "+ inputMul);
        //variables helping to read the input
        int c= 0;
        int m = blockSize;
        System.out.println("concat char: "+ concat.charAt(0));
        ModuloInteger test = ModuloInteger.valueOf(LargeInteger.valueOf((int) concat.charAt(0)));
        System.out.println("concat: "+test);
        //convert the plain text to list of vectors of modulointegers
        List<ModuloInteger> smsCharList = new ArrayList<>();
        DenseVector <ModuloInteger> vectorMul;
        List <DenseVector<ModuloInteger>> listOfMulVectors = new ArrayList<>();
        for(int j = 0;j<inputMul;j++){
                for(int i = c; i< m;i++){
                
                    //converting ascii to start at 0-97
                    int a= (int) concat.charAt(i)-32;
                    
                    smsCharList.add(ModuloInteger.valueOf(LargeInteger.valueOf(a)));
                    
                }
                vectorMul= DenseVector.valueOf(smsCharList);
                listOfMulVectors.add(vectorMul);
                smsCharList.clear();
                c= blockSize+c;
                m=m+blockSize;
            }
        
        DenseMatrix <ModuloInteger> matrixMul= DenseMatrix.valueOf(listOfMulVectors);
        System.out.println("matrixMul: "+matrixMul.toString());
        
        //list of key matrix multiplied with each vector of the plain text
        ArrayList <DenseVector<ModuloInteger>> listOfFinalVectors = new ArrayList<>();
        for(int i = 0;i< matrixMul.getNumberOfRows();i++){
            DenseVector <ModuloInteger>productVector = DenseVector.valueOf(matrix.times(matrixMul.getRow(i)));
            listOfFinalVectors.add(productVector);
        }
        for(int i = 0; i< listOfFinalVectors.size();i++){
            System.out.println("productVector: "+listOfFinalVectors.get(i));
        }
        
        
        BufferedWriter outputWriterInv = null;
        outputWriterInv = new BufferedWriter(new FileWriter("//Users//norash1995//NetBeansProjects//HillCipher//"+cipherfile));
        DenseMatrix<ModuloInteger> finalMatrix = DenseMatrix.valueOf(listOfFinalVectors);
        for(int i = 0; i < finalMatrix.getNumberOfRows();i++){
            for(int j = 0; j< finalMatrix.getNumberOfColumns();j++){
                
                //converting back to Ascii chars
                int enc = finalMatrix.get(i, j).intValue()+32;
                
                char encChar = (char) enc;
                outputWriterInv.write(String.valueOf(encChar));
                System.out.println("encryption code: "+encChar);
            }
        } 
        
        
        outputWriterInv.flush();  
        outputWriterInv.close();
    }
    
}
