package hillcipher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Scanner;
import org.jscience.mathematics.number.LargeInteger;
import org.jscience.mathematics.number.ModuloInteger;
import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.DenseVector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author norash1995
 */
public class SmsDecipher {
     public static void main(String[] args) throws FileNotFoundException, IOException{
        Scanner sc= new Scanner(System.in);
        System.out.println("radix: ");
        int radix = sc.nextInt();
        if(radix != 26){
            throw new EmptyStackException();
        }
        
        System.out.println("blockSize: ");
        int blockSize = sc.nextInt();
        if(blockSize!= 3){
            throw new EmptyStackException();
        }
        
        //printing current working directory
        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));
        File fileKey = new File("//Users//norash1995//NetBeansProjects//HillCipher//sms-invkey.txt");
        sc = new Scanner(fileKey);
        //sc.useDelimiter(Pattern.compile("\\n"));
        
        
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
            
            //System.out.println("vector:"+vector.get(1));
        }
        sc.close();
        
        
        
        
        DenseMatrix<ModuloInteger> matrix = DenseMatrix.valueOf(listOfVectors);
        
        System.out.println("matrix: "+matrix.toString());

        File textEnc = new File("//Users//norash1995//NetBeansProjects//HillCipher//sms-cipher.txt");
        sc = new Scanner(textEnc);
        List<ModuloInteger> smsCharList = new ArrayList<>();
        int c= 0;
        int m = blockSize;
        //System.out.println("length of s: "+sc.nextLine().length());
        DenseVector <ModuloInteger> vectorMul;
        List <DenseVector<ModuloInteger>> listOfMulVectors = new ArrayList<>();
        while(sc.hasNext()){
            String s = sc.nextLine();
            for(int j = 0;j<s.length()/blockSize;j++){
                for(int i = c; i< m;i++){
                
                    System.out.println("letters: "+s.charAt(i)+" ");
                    //conversion of letters to 0-26
                    int a= (int) s.charAt(i)-65;
                    smsCharList.add(ModuloInteger.valueOf(LargeInteger.valueOf(a)));
                    
                }
                vectorMul= DenseVector.valueOf(smsCharList);
                listOfMulVectors.add(vectorMul);
                smsCharList.clear();
                c= blockSize+c;
                m=m+blockSize;
            }
            
        }
        ModuloInteger.setModulus(LargeInteger.valueOf(radix));
        DenseMatrix <ModuloInteger> matrixMul= DenseMatrix.valueOf(listOfMulVectors);
        ArrayList <DenseVector<ModuloInteger>> listOfFinalVectors = new ArrayList<>();
        for(int i = 0;i< matrixMul.getNumberOfRows();i++){
            DenseVector <ModuloInteger>productVector = DenseVector.valueOf(matrix.times(matrixMul.getRow(i)));
            listOfFinalVectors.add(productVector);
        }
        for(int i = 0; i< listOfFinalVectors.size();i++){
            System.out.println("productVector: "+listOfFinalVectors.get(i));
        }
        DenseMatrix<ModuloInteger> finalMatrix = DenseMatrix.valueOf(listOfFinalVectors);
        for(int i = 0; i < finalMatrix.getNumberOfRows();i++){
            for(int j = 0; j< finalMatrix.getNumberOfColumns();j++){
                int enc = finalMatrix.get(i, j).intValue()+65;
                
                char encChar = (char) enc;
                System.out.println("encryption code: "+encChar);
            }
        } 
    }
    
}
