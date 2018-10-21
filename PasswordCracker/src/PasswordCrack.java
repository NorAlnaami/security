
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import passwordcracker.jcrypt;




public class PasswordCrack{
    private static ArrayList <String> list = new ArrayList();    
    private static ArrayList <String> userList = new ArrayList();
    private static ArrayList <String> passwordsList = new ArrayList();
    private static ArrayList <String> dictList = new ArrayList();
    private static ArrayList <String> origDictList = new ArrayList();
    private static ArrayList <String> delList = new ArrayList();
    
    public static void main(String []args) throws FileNotFoundException{
        
        
        readPassFile(args[0]);
        
        dictRead(args[1]);
        //readPassFile("hello");
        //dictRead("hey");
        saltgo();
        //String test = mingle("len", 19);
        //System.out.println("test: "+test);
        
        
        
    }
    

    public static void saltgo(){
        for(int i = 0; i<list.size();i++){
            String entry = list.get(i);
            String namepass[] = entry.split(":");
            String salt = namepass[1].substring(0,2);
            newHash(salt);
        }
    }
    
    //Given salt and password find username
    public static void newHash(String salt){
        for(int i = 0; i< list.size();i++){
            String entry = list.get(i);
            String namepass[] = entry.split(":"); 
            String saltFound = namepass[1].substring(0, 2);
            
            
            if(saltFound.compareTo(salt)==0){
                
                
                
                Boolean found= false;
                
                String hintUser=namepass[4].replaceAll("\\s", "");
                //System.out.println("hint: "+hintUser);
                String password = namepass[1];//.substring(2, namepass[1].length());
                
                String mangledhintUser = hintUser;
                int m= 0;
                while(found != true){
                    if(m>20){
                        found = true;
                    }
                    System.out.println("hintUser: "+hintUser);
                    mangledhintUser=mingle(hintUser, m);
                    //System.out.println("going throught not found: "+mangledhintUser);
                //cracking password with all dict words
                for(int j=0; j<dictList.size();j++){
                    String crypt = jcrypt.crypt(salt,dictList.get(j));
                    if(crypt.compareTo(password)==0){
                        //System.out.println("HIIIT");
                        System.out.println("password string is: "+ dictList.get(j));
                        found = true;
                        break;
                    }
                }
                
                if(found!= true){
                    String crypt = jcrypt.crypt(salt,mangledhintUser);
                    if(crypt.compareTo(password)==0){
                        //System.out.println("It went throught hints");
                        System.out.println("password string is: "+ hintUser);
                        found = true;
                    }
                }
                
                m++;
                //System.out.println("m: "+m);
                
                
                }
            }
            
        }
    }
    
    public static String mingle(String Username, int m){
        //reversed
        if(m==1){
            //System.out.println("mangling with reverse");
            dictList.clear();
            for(int i = 0; i< origDictList.size();i++){
                String reverse = new StringBuffer(origDictList.get(i)).reverse().toString();
                dictList.add(reverse);
            }
            String reverse = new StringBuffer(Username).reverse().toString();
            return reverse;
        }
        //duplicated
        if(m==2){
            //System.out.println("mangling with duplicated");
            dictList.clear();
            for(int i = 0; i<origDictList.size(); i++){
                String duplicated = new String(new char[2]).replace("\0", origDictList.get(0));
                dictList.add(duplicated);
            }
            String duplicated = new String(new char[2]).replace("\0", Username);
            return duplicated;
        }
        //reflected at one side
        if(m==3){
            //System.out.println("mangling with reflected 1");
            dictList.clear();
            for(int i = 0; i< origDictList.size();i++){
                String reverse = new StringBuffer(origDictList.get(i)).reverse().toString();
                String reflection = reverse.concat(origDictList.get(i));
                dictList.add(reflection);
            }
            String reverse = new StringBuffer(Username).reverse().toString();
            String reflection = reverse.concat(Username);
            return reflection;
        }
        //reflected
        if(m==4){
            //System.out.println("mangling with reflected 2");
            dictList.clear();
            for(int i = 0; i< origDictList.size();i++){
                String reverse = new StringBuffer(origDictList.get(i)).reverse().toString();
                String reflection = origDictList.get(i).concat(reverse);
                dictList.add(reflection);
            }
            String reverse = new StringBuffer(Username).reverse().toString();
            String reflection = Username.concat(reverse);
            return reflection;
        }
        //uppercase
        if(m==5){
            //System.out.println("mangling with uppercase");
            dictList.clear();
            for(int i = 0; i<origDictList.size(); i++){
                String uppercase = origDictList.get(i).toUpperCase();
                dictList.add(uppercase);
            }
            String uppercase = Username.toUpperCase();
            return uppercase;
        }
        //lowercase
        if(m==6){
            //System.out.println("mangling with lowercase");
            dictList.clear();
            for(int i = 0; i<origDictList.size(); i++){
                String lowerCase = origDictList.get(i).toLowerCase();
                dictList.add(lowerCase);
            }
            String lowerCase = Username.toLowerCase();
            return lowerCase;
        }
        //capitalize first letter
        if(m==7){
            //System.out.println("mangling with capitalizing first letter");
            dictList.clear();
            for(int i =0; i< origDictList.size(); i++){
                String capitalFirst = origDictList.get(i).substring(0, 1).toUpperCase() + origDictList.get(i).substring(1);
                dictList.add(capitalFirst);
            }
            String capitalFirst = Username.substring(0, 1).toUpperCase() + Username.substring(1);
            return capitalFirst;
        }
        //ncapitalize the string
        if(m==8){
            //System.out.println("mangling with capitalizing rest letters");
            dictList.clear();
            for(int i = 0; i< origDictList.size(); i++){
                String capitalRest = origDictList.get(i).substring(0, 1).toLowerCase() + origDictList.get(i).substring(1).toUpperCase();
                dictList.add(capitalRest);
            }
            String capitalRest = Username.substring(0, 1).toLowerCase() + Username.substring(1).toUpperCase();
            return capitalRest;
        }
        //delete1 the string
        if(m==9){
            //System.out.println("mangling with deleting first letters");
            dictList.clear();
            for(int i =0; i < origDictList.size();i++){
                String del = origDictList.get(i).substring(1, origDictList.get(i).length());
                
                dictList.add(del);
            }
            String del = Username.substring(1, Username.length());
            return del;
        }
        //delete2 the string
        if(m==10){
            dictList.clear();
            
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>2){
                    String del = origDictList.get(i).substring(1, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>2){
                String del = Username.substring(2, Username.length());
                return del;
            }
        }
        //delete3 string
        if(m==11){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>3){
                    String del = origDictList.get(i).substring(3, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>3){
                String del = Username.substring(3, Username.length());
                return del;
            }
            
        }
        //delete4 string
        if(m==12){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>4){
                    String del = origDictList.get(i).substring(4, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>4){
                String del = Username.substring(4, Username.length());
                return del;
            }
            
        }
        //delete5 string
        if(m==13){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>5){
                    String del = origDictList.get(i).substring(5, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>5){
                String del = Username.substring(5, Username.length());
                return del;
            }
            
        }
        //delete6 string
        if(m==14){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>6){
                    String del = origDictList.get(i).substring(6, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>6){
                String del = Username.substring(6, Username.length());
                return del;
            }
            
        }
        //delete7 string
        if(m==15){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>7){
                    String del = origDictList.get(i).substring(7, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>7){
                String del = Username.substring(7, Username.length());
                return del;
            }
            
        }
        //delete8 string
        if(m==16){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>8){
                    String del = origDictList.get(i).substring(8, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>8){
                String del = Username.substring(8, Username.length());
                return del;
            }
            
        }
        //delete9 string
        if(m==17){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>9){
                    String del = origDictList.get(i).substring(9, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>4){
                String del = Username.substring(9, Username.length());
                return del;
            }
            
        }
        //delete10 string
        if(m==18){
            dictList.clear();
            for(int i =0; i< origDictList.size();i++){
                if(origDictList.get(i).length()>10){
                    String del = origDictList.get(i).substring(10, origDictList.get(i).length());
                    dictList.add(del);
                }
            }
            if(Username.length()>10){
                String del = Username.substring(10, Username.length());
                return del;
            }
            
        }
        if(m==19){
            dictList.clear();
            StringBuilder result = new StringBuilder("");
            
            for(int i =0; i<origDictList.size();i++){
                for(int j=0; j<origDictList.get(i).length(); j++) {
                    Character c = origDictList.get(i).charAt(j);

                    if(j % 2 == 0){
                        result.append(c);
                    }
                    else{
                        result.append(Character.toUpperCase(c));
                    }
                }
                dictList.add(result.toString());
                result.delete(0, origDictList.get(i).length());
            }
            
            for(int x=0; x<Username.length(); x++) {
                Character c = Username.charAt(x);

                if(x % 2 == 0){
                    result.append(c);
                }
                else{
                    result.append(Character.toUpperCase(c));
                }
            }

                return result.toString();
            }
        
        
        
        return Username;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    // reads the password file and saves the password in a list
    // reads the whole line of password file and saves it in a list
    //reads the first name and save it in a list
    public static void readPassFile(String passFileInput) throws FileNotFoundException{
        //File passFile = new File("/Users/norash1995/NetBeansProjects/PasswordCracker/src/passwordcracker/passwd2.txt");
        File passFile = new File(passFileInput);
        Scanner sc = new Scanner(passFile);
        while(sc.hasNextLine()){
            String print = sc.nextLine();
            list.add(print);
            String namepass[] = print.split(":"); 
            String name = namepass[4];
            userList.add(name);
            String password = namepass[1];
            passwordsList.add(password);
            
        }
        sc.close();
    }
    
    
    
    //reads dictionary file and save it in a list
    public static void dictRead(String fileDict) throws FileNotFoundException{
        File passFile = new File(fileDict);
        //File passFile = new File("/Users/norash1995/NetBeansProjects/PasswordCracker/src/passwordcracker/dict.txt");
        Scanner sc = new Scanner(passFile);
        while(sc.hasNextLine()){
            dictList.add(sc.nextLine());
        }
        origDictList.addAll(dictList);
        sc.close();
    }
    
   
   
    
    
    
    
}