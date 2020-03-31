## rdc-getting-started-android

Demonstrates running Appium tests with the TestNG testing framework on Sauce Labs across multiple mobile device combinations in parallel.

This code is provided on an "AS-IS” basis without warranty of any kind, either express or implied, including without limitation any implied warranties of condition, uninterrupted use, merchantability, fitness for a particular purpose, or non-infringement. Your tests and testing environments may require you to modify this framework. Issues regarding this framework should be submitted through GitHub. For questions regarding Sauce Labs integration, please see the Sauce Labs documentation at https://wiki.saucelabs.com/. This framework is not maintained by Sauce Labs Support.

### Environment Setup

1. Global Dependencies

    * [Install Maven](https://maven.apache.org/install.html)
    * Or Install Maven with [Homebrew](http://brew.sh/)
    
            $ brew install maven

2. Set Environment Variables

    These environment variables are used for uploading apps to Sauce Storage as well as test execution:
    
    * __SAUCE_USERNAME__
    * __SAUCE_ACCESS_KEY__
    
    These can be found by visiting [https://app.saucelabs.com/user-settings]() and grabbing the values from the __USER NAME__ and __Access Key__ fields.
            
3. Upload App to Sauce Storage

    With the environment variables set, upload the app binary to Sauce Storage:
    
        ./push-to-sauce-storage.sh
        
    The file will be cached on Sauce Storage for 7 days from the upload. 
    	        
4. Project Dependencies

	* Check that Packages are available
	
	        $ mvn test-compile
	
	* You may also want to run the command below to check for outdated dependencies. Please be sure to verify and review updates before editing your `pom.xml` file. The updated packages may or may not be compatible with your code.
	
	        $ mvn versions:display-dependency-updates
	    
### Running Tests

* Tests in Parallel:

    	$ mvn test

    View the test results in the Sauce Dashboard at [https://app.saucelabs.com/dashboard/tests]() &rarr; Real Devices

### Resources

##### [Sauce Labs Documentation](https://wiki.saucelabs.com/)

##### [SeleniumHQ Documentation](http://www.seleniumhq.org/docs/)

##### [TestNg Documentation](http://testng.org/javadocs/index.html)