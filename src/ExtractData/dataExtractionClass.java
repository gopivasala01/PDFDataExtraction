package ExtractData;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class dataExtractionClass {
	public static String [][] FieldValues;
	public static String databaseValue="";
	

	
	public static String getValues(String text,String names) 
	{
		String[] naming = names.split("\\@");
		for(int i=0;i<naming.length;i++)
		{
			String rent ="";
			String subStringValue  = naming[i].split("\\^")[0].toLowerCase();
			String priorText  = naming[i].split("\\^")[1].toLowerCase();
			String splitBy  = naming[i].split("\\^")[2].toLowerCase();
			try {
				String monthlyRent = text.substring(text.indexOf(subStringValue));
				rent = monthlyRent.substring(monthlyRent.indexOf(priorText)+priorText.length()).trim().split(splitBy)[0].trim();//.replaceAll("[a-ZA-Z,]", "");
				rent = rent.replaceAll("[^0-9a-zA-Z./]","");
				
				if(hasSpecialCharacters(rent) == true)
				{
					if(rent.equalsIgnoreCase("n/a")) {
						//System.out.println("Rent Value= n/a");
						return "n/a";
					}
					else {
						continue;
					}
					
				}
				else {
					//System.out.println("Rent Value= "+rent);
					return rent;
				}
			}
			catch(Exception e) {
				//continue;n/a
			}
				
		}
			
		return "Error";
	}
	
	
	public static String getDates(String text,String datevalue) 
	{
		try {
			String[] data = datevalue.split("\\@");
			for(int i=0;i<data.length;i++)
			{
				String date ="";
				String subStringValue  = data[i].split("\\^")[0].toLowerCase();
				String priorText  = data[i].split("\\^")[1].toLowerCase();
				String afterText  = data[i].split("\\^")[2].toLowerCase();
				//String splitBy  = data[i].split("\\^")[3].toLowerCase();
				try {
					String modifiedtext = text.substring(text.indexOf(subStringValue));
					date = modifiedtext.substring(modifiedtext.indexOf(priorText)+priorText.length()).trim();
					date = date.substring(0, date.indexOf(afterText)).trim();//.replaceAll("[a-ZA-Z,]", "");
			
					return date;
				}
				catch(Exception e) {
					continue;
					//continue;n/a
				}
					
			}
		}
		catch(Exception e) {
			return "Error";
		}
		
		return "Error";
			
		
	}
	
	
	public static String getValuesWithStartandEndText(String text,String datavalue) 
	{
		try {
			String[] data = datavalue.split("\\@");
			for(int i=0;i<data.length;i++)
			{
				String value ="";
				String subStringValue  = data[i].split("\\^")[0].toLowerCase();
				String priorText  = data[i].split("\\^")[1].toLowerCase();
				String afterText  = data[i].split("\\^")[2].toLowerCase();
				try {
					String modifiedtext = text.substring(text.indexOf(subStringValue));
					//value = modifiedtext.substring(modifiedtext.indexOf(priorText)+priorText.length()).trim();
					value = modifiedtext.substring(modifiedtext.indexOf(priorText), modifiedtext.indexOf(afterText)).trim();//.replaceAll("[a-ZA-Z,]", "");
					if(value.contains("$")) {
						value = value.substring(value.indexOf("$")+1).split(" ")[0].replaceAll("$","").trim();
						if(value.contains("/month")) {
							value = value.replaceAll("/month", "");
						}
						return value;
					}
					else if(value.contains("n/a")) {
						return "n/a";
					}
					else {
						return "Error";
					}
					
					
				}
				catch(Exception e) {
					continue;
					//continue;n/a
				}
					
			}
		}
		catch(Exception e) {
			return "Error";
		}
		
		return "Error";
			
		
	}
	
	
	
	public static boolean getFlags(String text,String getFlags) {
		
		String[] getChecks = getFlags.split("\\@");
		for(int i=0;i<getChecks.length;i++)
		{
			String subStringValue  = getChecks[i].toLowerCase();
			try {
				if(text.contains(subStringValue)) {
					
					return true;
				}
			}
			catch(Exception e) {
				return false;
			}
		}
		
		return false;
		
	}
	
	public static boolean hasSpecialCharacters(String inputString) 
    {
        // Define a regular expression pattern to match characters other than digits, dots, and commas
        Pattern pattern = Pattern.compile("[^0-9.,]");

        // Use a Matcher to find any match in the input string
        Matcher matcher = pattern.matcher(inputString);

        return matcher.find();
    }
	
	
	
	public static String getDataOf(String fieldName)
	{
	try
	{
	        Connection con = null;
	        Statement stmt = null;
	        ResultSet rs = null;
	            //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	            con = DriverManager.getConnection(DatabaseClass.connectionUrl);
	            String SQL = "Select Value from Automation.PDFDataExtractConfig where Name='" + fieldName +"' ";
	            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	           // stmt = con.createStatement();
	            stmt.setQueryTimeout(60);
	            rs = stmt.executeQuery(SQL);
	            int rows =0;
	            if (rs.last()) 
	            {
	            	rows = rs.getRow();
	            	// Move to beginning
	            	rs.beforeFirst();
	            }
	            //System.out.println("No of Rows = "+rows);
	            FieldValues = new String[rows][1];
	           int  i=0;
	            while(rs.next())
	            {
	  
	            	String 	value = rs.getObject(1).toString();
	            	
	              //stateCode
	                try 
	                {
	                	
	                	FieldValues[i][0] = value;
	                	databaseValue = FieldValues[i][0];
	                	
	                }
	                catch(Exception e)
	                {
	                	FieldValues[i][0] = "";
	                }
	            }
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return "";
	}
	return databaseValue;
}

}
