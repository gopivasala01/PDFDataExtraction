package GenericLibrary;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import ExtractData.DatabaseClass;


public class GenericMethods {

	public static WebDriverWait wait;
	public static Actions actions;
	public static JavascriptExecutor js;
	public static String currentTime;
	public static String downloadFilePath ="";
	public static String failedReason="";
	
	public static boolean login(WebDriver driver,String failReason)
	{
		try
		{
			js = (JavascriptExecutor) driver;
			actions = new Actions(driver);
			wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		downloadFilePath = ExtractData.Locators.downloadFilePath;
		Map<String, Object> prefs = new HashMap<String, Object>();
	    // Use File.separator as it will work on any OS
	    prefs.put("download.default_directory",
	    		downloadFilePath);
        // Adding cpabilities to ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");
		//WebDriverManager.chromedriver().setup();
		WebDriverManager.chromedriver().clearDriverCache().setup();
        driver= new ChromeDriver(options);
		driver.manage().window().maximize();
        driver.get(ExtractData.Locators.URL);
        driver.findElement(ExtractData.Locators.usernameXpath).sendKeys(ExtractData.Locators.username); 
        driver.findElement(ExtractData.Locators.passwordXpath).sendKeys(ExtractData.Locators.password);
        driver.findElement(ExtractData.Locators.signInButton).click();
        actions = new Actions(driver);
        js = (JavascriptExecutor)driver;
        driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        try
        {
        if(driver.findElement(ExtractData.Locators.loginError).isDisplayed())
        {
        	System.out.println("Login failed");
        	failReason = failedReason+","+ "Login failed";
			return false;
        }
        }
        catch(Exception e) {}
        driver.manage().timeouts().implicitlyWait(100,TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Login failed");
			failReason = failedReason+","+ "Login failed";
			return false;
		}
	}
	
	public static boolean closeDriver(WebDriver driver) {
		 try
		  {
			  driver.quit();
		  }
		  catch(Exception e1) {
			  e1.printStackTrace();
			  return false;
		  }
		return true;
	}
	
	
	public static File getLastModified() throws Exception
	{
		
	    File directory = new File(downloadFilePath);
	    File[] files = directory.listFiles(File::isFile);
	    long lastModifiedTime = Long.MIN_VALUE;
	    File chosenFile = null;

	    if (files != null)
	    { 
	        for (File file : files)
	        {
	            if (file.lastModified() > lastModifiedTime)
	            {
	                chosenFile = file;
	                lastModifiedTime = file.lastModified();
	            }
	        }
	    }

	    return chosenFile;
	}
	
	
	
	public static boolean downloadLeaseAgreement(WebDriver driver,String failReason) {
		try {
			DatabaseClass.intermittentPopUp(driver);
			driver.manage().timeouts().implicitlyWait(5,TimeUnit.SECONDS);
	        wait = new WebDriverWait(driver, Duration.ofSeconds(2));
			driver.findElement(ExtractData.Locators.notesAndDocs).click();
			Thread.sleep(2000);
			List<WebElement> documents = driver.findElements(ExtractData.Locators.documentsList);
			boolean checkLeaseAgreementAvailable = false;
			 
			for(int i =0;i<documents.size();i++)
			{
				for(int j=0;j<ExtractData.Locators.LeaseAgreementFileNames.length;j++)
				{
				 if(documents.get(i).getText().startsWith(ExtractData.Locators.LeaseAgreementFileNames[j])&&!documents.get(i).getText().contains("Termination")&&!documents.get(i).getText().contains("_Mod"))//&&documents.get(i).getText().contains(AppConfig.getCompanyCode(RunnerClass.company)))
				 {
				 	documents.get(i).click();
				 	System.out.println(documents.get(i).getText());
					checkLeaseAgreementAvailable = true;
					break;
				 }
				}
				if(checkLeaseAgreementAvailable == true)
					break;
			}
			if(checkLeaseAgreementAvailable==false)
			{
				System.out.println("Unable to download Lease Agreement");
				failReason =  failedReason+","+ "Unable to download Lease Agreement";
				return false;
			}
		 
		}
		catch(Exception e) {
			System.out.println("Unable to download Lease Agreement");
			failReason =  failedReason+","+ "Unable to download Lease Agreement";
			return false;
		}
		
			 
		
		return true;
		
	}
	
	
	
	
	public static String convertDate(String dateRaw) throws Exception
	{
		try
		{
		SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd, yyyy");
	    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
	    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
	    System.out.println(format2.format(date));
		return format2.format(date).toString();
		}
		catch(Exception e)
		{
			try
			{
			SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd yyyy");
		    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
		    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
		    System.out.println(format2.format(date));
			return format2.format(date).toString();
			}
			catch(Exception e2)
			{
			  if(dateRaw.trim().replaceAll(" +", " ").split(" ")[1].contains("st")||dateRaw.trim().replaceAll(" +", " ").split(" ")[1].contains("nd")||dateRaw.trim().replaceAll(" +", " ").split(" ")[1].contains("th"))
				  dateRaw = dateRaw.trim().replaceAll(" +", " ").replace("st", "").replace("nd", "").replace("th", "");
			  try
				{
				SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd yyyy");
			    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
			    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
			    System.out.println(format2.format(date));
				return format2.format(date).toString();
				}
				catch(Exception e3)
				{
					try
					{
					SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd,yyyy");
				    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
				    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
				    System.out.println(format2.format(date));
					return format2.format(date).toString();
					}
					catch(Exception e4)
					{
						try
						{
						SimpleDateFormat format1 = new SimpleDateFormat("MMMM dd.yyyy");
					    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
					    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
					    System.out.println(format2.format(date));
						return format2.format(date).toString();
						}
						catch(Exception e5)
						{
							try
							{
							SimpleDateFormat format1 = new SimpleDateFormat("M/dd/yyyy");
						    SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy");
						    Date date = format1.parse(dateRaw.trim().replaceAll(" +", " "));
						    System.out.println(format2.format(date));
							return format2.format(date).toString();
							}
							catch(Exception e6)
							{
							
					return "";
							}
					}
				}
			}
		}
	}
	} 
	
	    public static String firstDayOfMonth(String date,int month) throws Exception 
	    {
	    	//String string = "02/05/2014"; //assuming input
	        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	        Date dt = sdf .parse(date);
	        Calendar c = Calendar.getInstance();
	        c.setTime(dt);
	        //if(portfolioType=="MCH")
	        c.add(Calendar.MONTH, month);  //adding a month directly - gives the start of next month.
	        //else c.add(Calendar.MONTH, 2);
	        c.set(Calendar.DAY_OF_MONTH, 01);
	        String firstDate = sdf.format(c.getTime());
	        System.out.println(firstDate);
	        return firstDate;
	    }
	    public static String getCurrentDateTime()
	    {
	    	currentTime ="";
	    	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  
			 LocalDateTime now = LocalDateTime.now();  
			// System.out.println(dtf.format(now));
			 currentTime = dtf.format(now);
			 return currentTime;
	    }
	    public static String lastDateOfTheMonth(String date) throws Exception
	    {
	    	//String date =RunnerClass.convertDate("January 1, 2023");
	    	LocalDate lastDayOfMonth = LocalDate.parse(date, DateTimeFormatter.ofPattern("M/dd/yyyy"))
	    	       .with(TemporalAdjusters.lastDayOfMonth());
	    	String newDate = new SimpleDateFormat("MM/dd/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(lastDayOfMonth.toString()));
	    	System.out.println(newDate);
	    	return newDate;
	    }
	    public static String monthDifference(String date1, String date2) throws Exception
	    {
	    	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
	        Date firstDate = sdf.parse(date1);
	        Date secondDate = sdf.parse(date2);

	        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
	                .appendPattern("MM/dd/yyyy")
	                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
	                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
	               // .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
	                .toFormatter();
	        
           String x =  Duration.between( LocalDate.parse(date1,formatter),  LocalDate.parse(date2,formatter)).toString();
			return "";
	    }
	    
		public static boolean compareBeforeDates(String date1, String date2)
		{
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				System.out.println(sdf.parse(date1).before(sdf.parse(date2)));
				if(sdf.parse(date1).before(sdf.parse(date2)))
					return true;
				else return false;
			}
			catch(Exception e)
			{
			return false;
			}
		}
	    
	    
	    public static String getCurrentDate()
	    {
	    	currentTime ="";
	    	 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");  
			 LocalDateTime now = LocalDateTime.now();  
			// System.out.println(dtf.format(now));
			 currentTime = dtf.format(now);
			 return currentTime;
	    }
	    public static boolean onlyDigits(String str)
	    {
			str = str.replace(",", "").replace(".", "").trim();
			if(str=="")
				return false;
			int numberCount =0;
	        for (int i = 0; i < str.length(); i++) 
	        {
	            if (Character.isDigit(str.charAt(i))) 
	            {
	            	numberCount++;
	            	//return true;
	            }
	        }
	        if(numberCount==str.length())
	        return true;
	        else
	        return false;
	    }

	    
	    public static void handleAlerts(WebDriver driver) throws InterruptedException {
	        try {
	        	 Thread.sleep(1000);
	            Alert alert = driver.switchTo().alert();
	            alert.accept();
	        } catch (NoAlertPresentException ignored) {
	            // Alert not present, continue with the rest of the code
	        }
	    }
	    
	    public static WebElement findElementWithWait(WebDriver driver,By locator) {
	        try {
	        	wait = new WebDriverWait(driver, Duration.ofSeconds(5));
	            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	        } catch (Exception e)
	        {
	            return null;
	        }
	    }
	
	
}
