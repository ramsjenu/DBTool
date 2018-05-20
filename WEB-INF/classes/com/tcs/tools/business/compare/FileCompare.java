package com.tcs.tools.business.compare;

import com.tcs.tools.business.compare.dao.FileCompareDAO;

public class FileCompare {

	//String pDbMigrationType ="SYSBASE_TO_DB2";
	//String pDbMigrationType="SYBASE_TO_SQL";
	FileCompareDAO lFileCompareDAO = new FileCompareDAO();
	/*public static void main(String[] args) {
		System.out.println(":::::inside main:::::");
		String pRunIdSybase ="617";//"588"
		String pRunIdDb2 ="618";//"589"
		String pDbMigrationType ="SYSBASE_TO_DB2";
		FileCompare lFileCompare = new FileCompare();
		lFileCompare.compareTwoProcs(pRunIdSybase, pRunIdDb2, pDbMigrationType);

	}*/
	public String compareTwoProcs(String pRunIdSybase,String pRunIdDb2,String pDbMigrationType,String pProjectId,String pProjectRunSeq){
		return lFileCompareDAO.prepareListsToCompare(pRunIdSybase, pRunIdDb2, pDbMigrationType,pProjectId);
		//return "";
	}
	public String compareTwoProcsSybaseToOracle(String pRunIdSybase,String pRunIdOracle,String pDbMigrationType,String pProjectId,String pProjectRunSeq){
		return lFileCompareDAO.prepareListsToCompareSybaseToOracle(pRunIdSybase, pRunIdOracle, pDbMigrationType,pProjectId);
		//return lFileCompareDAO.prepareListsToCompare(pRunIdSybase, pRunIdOracle, pDbMigrationType,pProjectId);
		//return "";
	}
	public static void main(String[] args){
		String pRunIdSybase="PR001_SOURCE";
		String pRunIdDb2="PR001_TARGET";
		String pDbMigrationType="SYSBASE_TO_DB2";
		String pProjectId="PR001";
		FileCompare lFileCompare=new FileCompare();
		lFileCompare.compareTwoProcs( pRunIdSybase, pRunIdDb2, pDbMigrationType, pProjectId,"");
	}

}
