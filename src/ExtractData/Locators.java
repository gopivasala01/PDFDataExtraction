package ExtractData;

import org.openqa.selenium.By;

public class Locators {
	
	
    public static String downloadFilePath = "C:\\Users\\gopi\\Documents\\BaseRent Update Files\\New folder";
	public static String username= "mds0418@gmail.com";
	public static String password="KRm#V39fecMDGg#";
	public static String URL="https://app.propertyware.com/pw/login.jsp";
	public static String[] LeaseAgreementFileNames = {"RT Renewal Signed","RT - RENEWAL","RT_Full_Lease","Full Lease -","RENEWAL","renewal_","Renewal","Full_Lease","Full"};
	

	
	
	public static By usernameXpath= By.id("loginEmail");
	public static By passwordXpath= By.name("password");
	public static By signInButton= By.xpath("//*[@class=\"button login-button\"]");
	public static By loginError = By.xpath("//*[@class='toast toast-error']");
	
	
	
	public static By searchbox = By.name("eqsSearchText");
	public static By dashboardsTab = By.linkText("Dashboards");
	public static By searchingLoader = By.xpath("//*[@id='eqsResult']/h1");
	public static By noItemsFoundMessagewhenLeaseNotFound = By.xpath("//*[text()='No Items Found']");
	public static By selectSearchedLease = By.xpath("//*[@class='results']/descendant::li/a");
	public static By getLeaseCDEType = By.xpath("//*[@id='summary']/table[1]/tbody/tr[3]/td");
    public static By leasesTab = By.xpath("//*[@class='tabbedSection']/a[4]");	
    public static By leasesTab2 = By.xpath("(//a[text()='Leases'])[2]");
    public static By searchedLeaseCompanyHeadings = By.xpath("//*[@id='eqsResult']/div/div/h1");
    public static By notesAndDocs = By.id("notesAndDocuments");
    public static By documentsList = By.xpath("//*[@id='documentHolderBody']/tr/td[1]/a"); 
    
    public static By marketDropdown = By.id("switchAccountSelect");
    
    public static By popUpAfterClickingLeaseName = By.xpath("//*[@id='viewStickyNoteForm']");
    public static By popupClose = By.xpath("//*[@id='editStickyBtnDiv']/input[2]");
    public static By scheduledMaintanancePopUp = By.xpath("//*[text()='Scheduled Maintenance Notification']");
    public static By scheduledMaintanancePopUpOkButton = By.id("alertDoNotShow");
}
