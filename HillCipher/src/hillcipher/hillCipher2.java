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
import java.util.Scanner;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.ModuloInteger;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;

/**
 *
 * @author norash1995
 */
public class hillCipher2 {
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
        
        //reading the keyfile
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
        //matrix with keyfile
        DenseMatrix<ModuloInteger> matrix = DenseMatrix.valueOf(listOfVectors);
        System.out.println("matrix: "+matrix.toString());
        
        //Reading the plain text
        File textEnc = new File("//Users//norash1995//NetBeansProjects//HillCipher//"+plainfile);
        sc = new Scanner(textEnc);
        
        String s= sc.nextLine();
        System.out.println(s);
        
        //string length
        int sLength= s.length();
        
        
        //calculating how many padding are needed
        int inputRest= sLength%blockSize;
        int nrOfpadding = blockSize - inputRest;
        
        //nr of blocks the string will form 
        int inputMul= (nrOfpadding+s.length())/3;
        System.out.println("input multiple: "+ inputMul);
        
        //variables helping to read the input
        int c= 0;
        int m = blockSize;
        int a;
        //length of string after padding
        int paddedStringLength= inputMul*blockSize; 
        //convert the plain text to list of vectors of modulointegers
        List<ModuloInteger> smsCharList = new ArrayList<>();
        DenseVector <ModuloInteger> vectorMul;
        List <DenseVector<ModuloInteger>> listOfMulVectors = new ArrayList<>();
        
        //if s.length()%blockSize = 0 pad extra block with N bytes (blockSize)
        //this helps for deciphering to distinguish between the pad bytes or last message byte
        if(nrOfpadding==0){
            inputMul +=1;
            paddedStringLength= inputMul*blockSize;
            nrOfpadding=blockSize;
            for(int j = 0;j<inputMul;j++){
                for(int i = c; i< m;i++){
                    
                     //converting ascii to start at 0-97
                    try{
                        a= (int) s.charAt(i);
                        smsCharList.add(ModuloInteger.valueOf(LargeInteger.valueOf(a)));
                    }
                    catch(Exception e){

                        if((paddedStringLength-i)==nrOfpadding){
                            a = nrOfpadding;
                            smsCharList.add(ModuloInteger.valueOf(LargeInteger.valueOf(a)));
                        }
                    }

                    
                }
                vectorMul= DenseVector.valueOf(smsCharList);
                listOfMulVectors.add(vectorMul);
                smsCharList.clear();
                c= blockSize+c;
                m=m+blockSize;
            }
        }
        else{
            for(int j = 0;j<inputMul;j++){
                for(int i = c; i< m;i++){
                    
                     //converting ascii to start at 0-97
                    try{
                        a= (int) s.charAt(i);
                        smsCharList.add(ModuloInteger.valueOf(LargeInteger.valueOf(a)));
                    }
                    catch(Exception e){

                        if((paddedStringLength-i)==nrOfpadding){
                            a = nrOfpadding;
                            smsCharList.add(ModuloInteger.valueOf(LargeInteger.valueOf(a)));
                        }
                    }

                    
                }
                vectorMul= DenseVector.valueOf(smsCharList);
                listOfMulVectors.add(vectorMul);
                smsCharList.clear();
                c= blockSize+c;
                m=m+blockSize;
            }
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
                int enc = finalMatrix.get(i, j).intValue();
                
                char encChar = (char) enc;
                outputWriterInv.write(String.valueOf(encChar));
                System.out.println("encryption code: "+encChar);
            }
        } 
        
        
        outputWriterInv.flush();  
        outputWriterInv.close();
    }
}
