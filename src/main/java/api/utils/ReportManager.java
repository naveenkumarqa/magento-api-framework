package api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ReportManager implements ITestListener {
	
	public ExtentSparkReporter sparkArchive;
	public ExtentSparkReporter sparkStatic;
	public ExtentReports report;
	public ExtentTest extentTest;
	
	String repName;

	public void onStart(ITestContext context) {
		//Timestamped report (for archive)
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		repName = "Test-Report-"+timeStamp+".html";

		sparkArchive = new ExtentSparkReporter("./reports/"+repName);
		sparkArchive.config().setDocumentTitle("Magento-API_Report");
		sparkArchive.config().setReportName("Magento-API-Automation");
		sparkArchive.config().setTheme(Theme.DARK);
		
		//Static report (for Jenkins)
		sparkStatic = new ExtentSparkReporter("./reports/html-report/");
		sparkStatic.config().setDocumentTitle("Magento-API_StaticReport");
		sparkStatic.config().setReportName("Magento-API-Automation");
		sparkStatic.config().setTheme(Theme.DARK);

		report = new ExtentReports();
		report.attachReporter(sparkArchive, sparkStatic);
		report.setSystemInfo("Application", "MagentoAPI");
		report.setSystemInfo("OS", System.getProperty("os.name"));
		report.setSystemInfo("Java version", System.getProperty("java.version"));
		report.setSystemInfo("User", "Naveen");
	}

	public void onTestStart(ITestResult result) {
		extentTest = report.createTest(result.getMethod().getMethodName());
		extentTest.assignCategory(result.getMethod().getGroups());
		extentTest.createNode(result.getName());
	}
	
	public void onTestSuccess(ITestResult result) {
		extentTest.log(Status.PASS, "Test Passed");
	}
	
	public void onTestFailure(ITestResult result) {
		extentTest.log(Status.FAIL, "Test failed");
	}

	public void onTestSkipped(ITestResult result) {
		extentTest.log(Status.SKIP, "Test Skipped");
	}
	
	public void onFinish(ITestContext context) {
		if (report != null) {
			report.flush();
		}
	}
}