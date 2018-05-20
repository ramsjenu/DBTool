import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;


public class RemoveLinesInFiles {

	public static void deleteIndex(String pIntputPath,String pOutputPath){
		String [] lLines=null;		
		File[] lFilesAndDirs = new File(pIntputPath).listFiles();
		FileWriter lFileWriter=null;
		System.out.println(pIntputPath+"::No of Files::"+lFilesAndDirs.length);
		int lDeletedLineCount=0;
		try {
			Pattern lChkIndexPattern=Pattern.compile("(?i)CREATE[\\s\\w\\W\\d]+INDEX[\\s\\w\\W\\d]*ON[\\s\\w\\W\\d]*\\(\\s*\\)");
			for (int i = 0; i < lFilesAndDirs.length; i++) {	
				
				if(lFilesAndDirs[i].isFile() && lFilesAndDirs[i].getName().toUpperCase().contains("_TABLE")){								
					
					lLines=getLines(lFilesAndDirs[i]);
					System.out.println("File Name::->"+ lFilesAndDirs[i].getName());
					lFileWriter=new FileWriter(pOutputPath+lFilesAndDirs[i].getName(),false);
					for (int j = 0; j < lLines.length; j++) {
						//CREATE INDEX billg_crrn_agarh_t ON billg_crrn_agarh_t ();
						if(!lChkIndexPattern.matcher(lLines[j].trim()).find()){
							lLines[j]=lLines[j].replaceAll("_DBT_SEM_COL_", ";");
							lLines[j]=lLines[j].replaceAll("_DBT_GO_", " GO ");
							lFileWriter.write(lLines[j]+"\r\n");
						}else{
							lDeletedLineCount++;
							//System.out.println("Matched Line:::->"+lLines[j]);
						}
					}
					lFileWriter.close();
					System.out.println("No of Lines Deleted::->"+lDeletedLineCount);
					lDeletedLineCount=0;
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String[] getLines(File file) {
		String[] lines = null;
		String [] lNewLines=null;
		try {
			InputStream is = new FileInputStream(file);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = is.read(b)) != -1;) {
				
				buffer.append(new String(b, 0, n));
			}
			
			String str = buffer.toString();
			str=str.replaceAll("\\s*;\\s*", "_DBT_SEM_COL_;");
			str=str.replaceAll("\\s+GO\\s+", "_DBT_GO_ GO ");
			Pattern p = Pattern.compile("\\s*;\\s*|\\s+GO\\s+");                        
			lines = p.split(str.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static void main(String [] args){
		String pFolderPath="C:\\Praveen\\Downloads\\New Folder\\Watson\\";
		String pOutputPath="C:\\Praveen\\Downloads\\New Folder\\Watson\\Output\\";
		deleteIndex( pFolderPath, pOutputPath);
	}
}
