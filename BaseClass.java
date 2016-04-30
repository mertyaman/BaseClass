package bilyoner.selenium.base;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import bilyoner.selenium.utils.BilyonerConfiguration;
import bilyoner.selenium.utils.BilyonerStringOperations;


/**
 * @author mert.yaman
 *	BilyonerBase sinifi selenium driver ile ilgili tum fonksiyonlari icerir. Butun page siniflari bu classtan turemistir.
 *	Logger , WebDriver, WebDriverWait bu sinif icinde tanimlanmistir.
 *	Webelement ile ilgili fonksiyonlar , Websayfası ile ilgili fonksiyonlar bu class icindedir.
 *	
 */
public abstract class BilyonerBase {
	protected final static Logger loger = Logger.getLogger(BilyonerBase.class);
	protected static WebDriver driver;
	protected WebDriverWait wait;
	protected FluentWait<WebDriver> fluentWait;
	protected JavascriptExecutor js;
	private String mainWindowHandle;
	protected BilyonerConfiguration bilyonerConfiguration;

	public BilyonerBase(WebDriver driver) {
		
		this.driver = driver;
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver, 3);
		fluentWait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(NoSuchElementException.class);
		mainWindowHandle = driver.getWindowHandle();
		js = (JavascriptExecutor) driver;
		bilyonerConfiguration = BilyonerConfiguration.getInstance();
	}

	protected void webDriverWait(int second) {
		driver.manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);
	}

	protected void webDriverWindowMaximize() {
		driver.manage().window().maximize();
	}

	protected void setWebDriverUrl(String Url) {
		driver.navigate().to(Url);
	}

	protected String getWebDriverUrl() {
		return driver.getCurrentUrl();
	}

	protected void webDriverRefresh() {
		driver.navigate().refresh();
	}

	protected String getWebDriverPageSource() {
		return driver.getPageSource().trim();
	}

	protected String getWebDriverTitle() {
		return driver.getTitle();
	}
	
	public static void HighlightElement(WebElement el) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);" ,el,
        " border: 3px solid red;" );        
        }
	// Click işlemlerini ayırt edebilmek için oluşturuldu.
	protected void HighlightElementClick(WebElement el) {  
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);" ,el,
        " border: 3px solid blue;" );   
        }
    	
	protected boolean webDriverPageSourceSearch(String findText) {
		return getWebDriverPageSource().contains(findText);
	}

	protected void switchWindow() {
		Set<String> set = driver.getWindowHandles();

		set.remove(mainWindowHandle);
		Assert.assertTrue(set.size() == 1 ? true : false);
		for (String string : set) {
			driver.switchTo().window(string);
		}

	}
	
	protected void switchContent() {
		driver.switchTo().defaultContent();
	}

	protected void switchMainWindow() {
		driver.switchTo().window(mainWindowHandle);
	}

	protected void webDriverClose() {
		driver.close();
	}

	protected WebElement findWebElement(String htmlText) {
		
		WebElement el = driver.findElement(By.cssSelector(htmlText));
        HighlightElement(el);
        return el;
	}
	
	protected WebElement findElementViaJS(String htmlText) {
		
		WebElement el=(WebElement)js.executeScript("return document.querySelector(arguments[0])", htmlText);
		HighlightElement(el);
		return el;
		
	}

	protected String findElementViaJSValue(String htmlText) {
		
		return (String)js.executeScript("return document.querySelector(arguments[0]).value;", htmlText);
		
	}
	

	
	protected WebElement findElementsViaJS(String htmlText, String index) {
		
		return (WebElement)js.executeScript("return document.querySelectorAll(arguments[0]).item(arguments[1])", htmlText,index);
		
	}
	
	protected boolean getWebElementSelectedJS(String htmlText,String index)
	{
		
		return (Boolean)js.executeScript("return document.querySelectorAll(arguments[0]).item(arguments[1]).disabled", htmlText,index);
		
	}
	protected String findElementViaJSValue(String htmlText, String index) {
		
		return (String)js.executeScript("return document.querySelectorAll(arguments[0]).item(arguments[1]).textContent;", htmlText,index);
		
	}
	
	protected int findElementsViaJSSize(String htmlText) {
		
		long s=(Long)js.executeScript("return document.querySelectorAll(arguments[0]).length", htmlText);
		//System.out.println(s);
		//return Integer.valueOf(s);
		return (int)s;
	}
	

	protected void clickWebElement(String htmlText) {
		
		WebElement el=findWebElement(htmlText);
		HighlightElementClick(el);
		el.click();
	}

	protected void clickWebElement(WebElement element) {

		HighlightElementClick(element);
		element.click();
	}

	protected String getWebElementText(String htmlText) {

		return findWebElement(htmlText).getText();
	}
	
	public String getWebElementTextUntil(String htmlText){
		return untilElementIsPresence(htmlText).getText();
	}

	protected void setScreenOnElement(WebElement element) {
		
		try {
			js.executeScript("arguments[0].scrollIntoView", element);
		} catch (Exception e) {
			loger.error(e.getMessage());
		}
		
	}
	
	protected void setScreenOnElement(String htmlText) {
		
		try {
			js.executeScript("arguments[0].scrollIntoView", findWebElement(htmlText));
		} catch (Exception e) {
			WebElement element=(WebElement) js.executeScript("document.querySelector(arguments[0])",htmlText);
			setScreenOnElement(element);
		}
		
	}
	
	protected void getWebElementeTextAndSetScreen(String htmlText) {
		setScreenOnElement(htmlText);
		getWebElementText(htmlText);
		
	}
	
	protected void getWebElementeTextAndSetScreen(WebElement element) {
		setScreenOnElement(element);
		getWebElementText(element);
		
	}
	
	protected String  getWebElementTextViaJS(String htmlText) {
		setScreenOnElement(findWebElement(htmlText));
		String s;
		s=(String) js.executeScript("document.querySelector(arguments[0]).textContent", htmlText);
		return s;
	}
	
	protected String  getWebElementAttributeTextViaJS(String htmlText,String attributeText) {
		
		setScreenOnElement(findWebElement(htmlText));
		String s;
		s=(String)  js.executeScript("document.querySelector(arguments[0]).getAttribute(arguments[1])", htmlText,attributeText);
		return s;
	}
	
	protected String  getWebElementAttributeTextViaJS(WebElement element,String attributeText) {
		
		//setScreenOnElement(element);
		//return getWebElementAttributeText(element, attributeText);
		String s;
		s=(String) js.executeScript("return arguments[0].getAttribute(arguments[1])", element,attributeText);
		return s;
	}
	
	protected String  getWebElementTextViaJS(WebElement element) {
	//	setScreenOnElement(element);
	//	return getWebElementText(element);
		String s;
		s=(String) js.executeScript("return arguments[0].textContent", element);
		return s;
	}
	
	protected String getWebElementText(WebElement element) {//duzenlenecek kod
//		JavascriptExecutor jse = (JavascriptExecutor)driver;
//		jse.executeScript("arguments[0].scrollIntoView()", element); //odaklanma
		return element.getText();
	}
	////Kişisel bilgilerimde username gibi angular textleri almak için kullanılır.
	protected String getAngularText(String htmltext){
		return findWebElement(htmltext).getAttribute("value");
		
	}
	
	protected String getWebElementTextJS(String htmlText) {//duzenlenecek kod
//		JavascriptExecutor jse = (JavascriptExecutor)driver;
//		jse.executeScript("arguments[0].scrollIntoView()", element); //odaklanma
		return findElementViaJS(htmlText).getText();
	}
	
	protected String getWebElementTextJSS(String htmlText) {//duzenlenecek kod
		return (String)js.executeScript("return document.querySelector(arguments[0]).textContent", htmlText);
	}
	
	protected void fillInputFieldWebElement(String htmlText, String sendKeyText) {
		fillInputFieldWebElement(findWebElement(htmlText),sendKeyText);
	}

	protected void fillInputFieldWebElement(WebElement element, String sendKeyText) {
		element.clear();
		element.sendKeys(sendKeyText);
	}
	//Mert:Input alanına klavyeden enter,delete gibi komutları gondermek icin olusturuldu
	protected void fillInputFieldWebElement(String htmlText, Keys keyboardKey){
		WebElement element=findWebElement(htmlText);
		element.sendKeys(keyboardKey);
	}

	protected String getWebElementAttributeText(String htmlText, String attributeText) {
		return findWebElement(htmlText).getAttribute(attributeText);
	}

	protected String getWebElementAttributeText(WebElement element, String attributeText) {
		return element.getAttribute(attributeText);
	}

	protected WebElement findWebElementInWebElement(String mainHtmlText, String findHtmlText) {
		return findWebElement(mainHtmlText).findElement(By.cssSelector(findHtmlText));
	}

	protected WebElement findWebElementInWebElement(WebElement element, String findHtmlText) {
		return element.findElement(By.cssSelector(findHtmlText));
	}

	protected void moveOnWebElementClickJS(String mainHtmlText, String subMenuHtmlText) {//duzenlenecek kod

		WebElement mainElement = untilElementIsClickable(mainHtmlText);
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		js.executeScript(mouseOverScript, mainElement);
		WebElement clickElement = untilElementIsClickable(subMenuHtmlText);
		HighlightElementClick(clickElement);
		js.executeScript("arguments[0].click();", clickElement);
//		js.executeScript(mouseOverScript, clickElement);
//		clickWebElement(clickElement);
	}
	
	protected void moveOnWebElementClickJS(String mainHtmlText, String subMenuHtmlText,int index) {//duzenlenecek kod

		WebElement mainElement = untilElementIsClickable(mainHtmlText);
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		js.executeScript(mouseOverScript, mainElement);
		WebElement clickElement = untilElementsAreVisible(subMenuHtmlText).get(index);
		HighlightElementClick(clickElement);
		js.executeScript("arguments[0].click();", clickElement);
//		js.executeScript(mouseOverScript, clickElement);
//		clickWebElement(clickElement);
	}
	
	protected void moveOnWebElementClickJS(WebElement mainElement, WebElement clickElement) {//duzenlenecek kod

		//WebElement mainElement = untilElementIsClickable(mainHtmlText);
		String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
		js.executeScript(mouseOverScript, mainElement);
		HighlightElementClick(clickElement);
	//	WebElement clickElement = untilElementIsClickable(subMenuHtmlText);
		js.executeScript("arguments[0].click();", clickElement);
//		js.executeScript(mouseOverScript, clickElement);
//		clickWebElement(clickElement);
	}
	////Sayfadaki webelemente scroll
	protected WebElement scrollIntowebElement2(WebElement element){
		Coordinates coordinate = ((Locatable)element).getCoordinates(); 
		coordinate.onPage(); 
		coordinate.inViewPort();
		sayfayiYukariKaydir(driver);
		return element;
	}
	

	protected WebElement scrollIntoWebElement(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView(true)", element);
		return element;
	}

	protected WebElement scrollIntoWebElement(String htmlText) {
		WebElement element = untilElementIsVisible(htmlText);
		js.executeScript("arguments[0].scrollIntoView(true)", element);
		return element;
	}

	protected List<WebElement> findWebElementInWebElements(String mainHtmlText, String findHtmlText) {
		return findWebElement(mainHtmlText).findElements(By.cssSelector(findHtmlText));
	}

	protected List<WebElement> findWebElementInWebElements(WebElement element, String findHtmlText) {
		return element.findElements(By.cssSelector(findHtmlText));
	}

	protected boolean getWebElementIsDisplayed(String htmlText) {
		return findWebElement(htmlText).isDisplayed();
	}

	protected boolean getWebElementIsDisplayed(WebElement element) {
		return element.isDisplayed();
	}

	protected boolean getWebElementIsEnabled(String htmlText) {
		return findWebElement(htmlText).isEnabled();
	}

	protected boolean getWebElementIsEnabled(WebElement element) {
		return element.isEnabled();
	}

	protected boolean getWebElementIsSelected(String htmlText) {
		return findWebElement(htmlText).isSelected();
	}

	protected boolean getWebElementIsSelected(WebElement element) {
		return element.isSelected();
	}
	
	protected String getWebElementCssValue(WebElement element, String css)
	{
		return element.getCssValue(css);
	}
	
	protected String getOptionSelectText(String htmlText) {
		return new Select(findWebElement(htmlText)).getFirstSelectedOption().getText();
	}

	protected void setOptionSelect(String htmlText, String value) {
		new Select(findWebElement(htmlText)).selectByValue(value);
	}

	protected void setOptionSelect(WebElement element, String value) {
		new Select(element).selectByValue(value);
	}

	protected void setOptionSelect(String htmlText, int index) {
		new Select(findWebElement(htmlText)).selectByIndex(index);
	}

	protected void setOptionSelect(WebElement element, int index) {
		new Select(element).selectByIndex(index);
	}
	
	protected int getWebElementTextToInt(WebElement element)
	{
		return Integer.valueOf(getWebElementText(element));
	}
	
	protected void mouseMoveOnWebElement(WebElement element) {
		
		 Actions builder = new Actions(driver);
		 builder.moveToElement(element).perform();
		 HighlightElement(element);
//		 builder.mo(element.getLocation().getX(), element.getLocation().getY()).build().perform();
	}
	protected WebElement mouseMoveOnWebElement2(WebElement element) {
		
		 Actions builder = new Actions(driver);
		 builder.moveToElement(element).perform();
		 HighlightElement(element);
//		 builder.mo(element.getLocation().getX(), element.getLocation().getY()).build().perform();
		 return element;
	}
	
	protected List<WebElement> findWebElements(String htmlText) {
		return driver.findElements(By.cssSelector(htmlText));
	}

	protected void ClickWebElements(String htmlText, int index) {
		List<WebElement> list = findWebElements(htmlText);
		Assert.assertTrue(list.size() > 0 ? true : false, "WebElement listesi bulunamadi");
		Assert.assertTrue(list.size() > index ? true : false, "WebElement listesi indexden kucuk");
//		WebElement el=list.get(index);
//		HighlightElementClick(el);
		list.get(index).click();
	}

	protected void clickWebElements(List<WebElement> list, int index) {
		Assert.assertTrue(list.size() > 0 ? true : false, "WebElement listesi bulunamadi");
		Assert.assertTrue(list.size() > index ? true : false, "WebElement listesi indexden kucuk");
//		WebElement el=list.get(index);
//		HighlightElementClick(el);
		list.get(index).click();
	}

	protected WebElement untilElementIsClickable(String htmlText) {
		try {
			WebElement el=wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(htmlText)));
			HighlightElement(el);
			return el;
		} catch (Exception e) {
			return null;
		}

	}
	
	protected WebElement untilElementIsClickable(WebElement element) {
		try {
			WebElement el=wait.until(ExpectedConditions.elementToBeClickable(element));
			HighlightElement(el);
			return el;
		} catch (Exception e) {
			return null;
		}

	}


	protected WebElement untilElementIsPresence(String htmlText) {
		try {
			WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(htmlText)));
            HighlightElement(el);
            return el;
		} catch (Exception e) {
			return null;
		}

	}

	protected boolean untilElementIsInvisible(String htmlText) {
		try {
			return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(htmlText)));
		} catch (Exception e) {
			return false;
		}

	}
	
	protected void sayfayiAsagiKaydir(WebDriver driver ){
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,250)", "");
		
	}
	
	protected void sayfayiYukariKaydir(WebDriver driver){
		JavascriptExecutor jse =(JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0,-250)", "");
	}
	protected void scrollToTop(){
		JavascriptExecutor jse =(JavascriptExecutor)driver;
		jse.executeScript("window.scrollTo(0,0)","");
		//window.scrollTo(x-coord, y-coord);
	}
	//test edilmedi
	protected void scrollToBottom() {
		JavascriptExecutor jse =(JavascriptExecutor)driver;
		jse.executeScript("window.scrollTo(0, document.body.scrollHeight","");
		//window.scrollTo(0, document.body.scrollHeight);
	}

	protected boolean untilElementIsSelected(String htmlText) {
		return wait.until(ExpectedConditions.elementToBeSelected(By.cssSelector(htmlText)));
	}

	protected WebElement untilElementIsVisible(String htmlText) {
		try {
			WebElement el=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(htmlText)));
			HighlightElement(el);
			return el;
		} catch (Exception e) {
			return null;
		}

	}

	protected boolean untilElementTextIs(String htmlText, String text) {
		return wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector(htmlText), text));
	}

	protected boolean untilElementTextIs(WebElement element, String text) {
		return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}

	protected boolean untilDriverTitleIs(String title) {
		return wait.until(ExpectedConditions.titleIs(title));
	}

	protected boolean untilDriverTitleContains(String findTitle) {
		return wait.until(ExpectedConditions.titleContains(findTitle));
	}

	protected boolean untilDriverUrlContains(String findUrl) {
		return wait.until(ExpectedConditions.urlContains(findUrl));
	}

	protected boolean untilDriverUrlIs(String Url) {
		return wait.until(ExpectedConditions.urlMatches(Url));
	}

	protected boolean untilDriverUrlToBe(String Url) {
		return wait.until(ExpectedConditions.urlToBe(Url));
	}

	protected List<WebElement> untilElementsArePresence(String htmlText) {
		try {

			return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(htmlText)));
		} catch (Exception e) {
			return new ArrayList<WebElement>();
		}
	}

	protected List<WebElement> untilElementsAreVisible(String htmlText) {
		try {
			return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(htmlText)));
		} catch (Exception e) {
			return new ArrayList<WebElement>();
		}

	}

	protected List<WebElement> untilElementsAreVisible(List<WebElement> elements) {
		return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}

	protected void waitForAjaxLoad() {
		long startTime = System.currentTimeMillis();
		long estimatedTime;
		try {
			Thread.sleep(250);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		boolean stillRunnigPageLoad = (Boolean) js
				.executeScript("return window.jQuery != undefined && jQuery.active == 0");
		try {
			while (!stillRunnigPageLoad) {
				stillRunnigPageLoad = (Boolean) js
						.executeScript("return window.jQuery != undefined && jQuery.active == 0");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
				}
				estimatedTime = System.currentTimeMillis() - startTime;

				if (estimatedTime >= 15000) {
					stillRunnigPageLoad = true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void waitForPageLoad() {
		long startTime = System.currentTimeMillis();
		long estimatedTime;
		try {
			Thread.sleep(250);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		boolean stillRunnigPageLoad = js.executeScript("return document.readyState").toString().equals("complete")
				? true : false;
		try {
			while (!stillRunnigPageLoad) {
				stillRunnigPageLoad = js.executeScript("return document.readyState").toString().equals("complete")
						? true : false;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				estimatedTime = System.currentTimeMillis() - startTime;

				if (estimatedTime >= 15000) {
					stillRunnigPageLoad = true;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void waitForAngularLoad() {
		long startTime = System.currentTimeMillis();
		long estimatedTime;
		try {
			Thread.sleep(250);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		boolean stillRunningAngular = (Boolean) executor
				.executeScript("return (window.angular != null) && (angular.element(document).injector() != null) &&"
						+ " (angular.element(document).injector().get('$http').pendingRequests.length === 0)");
		try {
			while (!stillRunningAngular) // Handle timeout somewhere
			{

				stillRunningAngular = (Boolean) executor.executeScript(
						"return (window.angular != null) && (angular.element(document).injector() != null) &&"
								+ " (angular.element(document).injector().get('$http').pendingRequests.length === 0)");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}

				estimatedTime = System.currentTimeMillis() - startTime;

				if (estimatedTime >= 15000) {
					stillRunningAngular = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void pageUrlAndTitleControl(String url, String titleCS, String title) {
		Assert.assertTrue(untilDriverUrlIs(url), "Page url Hatali");
		Assert.assertTrue(BilyonerStringOperations.stringContains(getWebElementText(titleCS), title),
				"Page title hatali");
	}
	
	protected int getStringToInt(String text)
	{
		return Integer.valueOf(text);
	}
	
	
	protected Double getStringToDouble(String text)
	{
		return Double.valueOf(text);
	}
	
	protected int noktaSil(String text){
		int number=Integer.parseInt(text.replace(".", "").trim());
		return number;
	}
	
	protected String getTime(){
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date dateobj = new Date();
		return df.format(dateobj);
	}
	protected void closeCurrentTab() throws AWTException{
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_W);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyRelease(KeyEvent.VK_W);
	}
	
	
}
