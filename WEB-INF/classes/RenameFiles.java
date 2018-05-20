/*
 * RenameFiles.java
 *
 * Created on May 10, 2012, 2:23 PM
 */

/**
 *
 * @author  455953
 */
import java.io.*;
public class RenameFiles {
    
    /** Creates a new instance of RenameFiles */
    public RenameFiles() {
    }
    
   public static void main(String[] args) throws IOException{
		File myDir = new File("C:/Documents and Settings/455953/My Documents/455953_docs/project/dailystatus/Sept/28thsept/Baseline_SP/walmartprod");
            File[] myFiles = myDir.listFiles();
            for (int i = 0; i < myFiles.length; i++)
            {      
                System.out.println("processing file: " + myDir.getAbsolutePath()                     + "\\" + myFiles[i]);
                File oldFile = myFiles[i];    
                System.out.println(oldFile.toString().substring(120));
                String newName=oldFile.toString().substring(120);
                System.out.println("newname   "+newName);
                String n1=newName.replaceAll(".src",".sql");
                System.out.println("n1   "+n1);
                String n="C:/Documents and Settings/455953/My Documents/455953_docs/project/dailystatus/Sept/28thsept/Baseline_SP/walmartprodRenamed/"+n1;
                File newFile = new File(n);    
                System.out.println("......The file will be renamed to " + n1);  
                boolean isFileRenamed = oldFile.renameTo(newFile);  
                if (isFileRenamed)      
                    System.out.println("......File has been renamed");    
                else      
                    System.out.println("......Error renaming the file"); 
         
                
            }
	}


    
}
