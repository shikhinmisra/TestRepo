package com.middleware.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.middleware.controller.dao.UserCredentialsDaoImpl;
import com.middleware.controller.service.LoadHomePage;
import com.middleware.controller.service.LoadTableSelectedByUser;
import com.middleware.restclient.service.RemoteDataReceiverClient;

@Controller
public class IController extends AbstractController {

	/* This is the DAO class for all the CRUD operations in the application */
	@Autowired
	private UserCredentialsDaoImpl userCredentials;

	@Autowired
	private RemoteDataReceiverClient remoteDataReceiverClient;
	
	//adding comments..

	@Autowired
	private LoadHomePage loadHomePage;

	@Autowired
	private LoadTableSelectedByUser loadTableSelectedByUser;

	/*
	 * This is the WELCOME page displayed to the user. User is going to perform
	 * 2 activities here: 1) Login to the application or 2) Register in the
	 * application
	 */
	@RequestMapping("/welcome")
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView modelandview = new ModelAndView("WelcomePage");
		modelandview.addObject("welcomeMessage", "Please LOGIN to Proceed..");

		return modelandview;
	}

	/*
	 * This is the method for validating the user credentials and redirect to
	 * the HOME PAGE if the user is authenticated. Otherwise an error message
	 * will be displayed. This method has to be moved to some other validation
	 * class instead of the controller
	 */
	@RequestMapping(value = "/welcome", method = RequestMethod.POST)
	protected ModelAndView validateUser(@RequestParam String userName, @RequestParam String password,
			HttpSession session) throws Exception {

		String passwordFromDB = userCredentials.getUserPassword(userName);

		if (passwordFromDB.equals("invaliduser")) {
			ModelAndView modelandview = new ModelAndView("WelcomePage");
			modelandview.addObject("loginError", "Incorrect Username. Please verify");
			return modelandview;
		}
		if (passwordFromDB.equals(password)) {
			session.setAttribute("appUser", userName);
			return new ModelAndView("redirect:/welcome/HomePage");
		} else {
			ModelAndView modelandview = new ModelAndView("WelcomePage");
			modelandview.addObject("loginError", "Incorrect Username/Password details entered. Please verify");

			return modelandview;
		}
	}

	/*
	 * This method loads the HOME PAGE for the logged in user.
	 */

	@RequestMapping(value = "/welcome/HomePage")
	protected ModelAndView openHomePage(HttpSession session,final HttpServletResponse response ) throws Exception {
		response.setHeader("Cache-Control", "no-cache");
		String userName = (String) session.getAttribute("appUser");
		ModelAndView modelandview = new ModelAndView("HomePage");
		modelandview.addObject("successMessage", "Hello " +userName + "!! Welcome to the application");
		modelandview.addObject("tableNames", loadHomePage.getAllSchemaTables());
		return modelandview;

	}
	
	/*
	 * This method logs the user out of the application.
	 */
	
	
	@RequestMapping(value = "/welcome/Logout")
	protected ModelAndView logoutUser(HttpSession session) throws Exception {
		session.removeAttribute("appUser");
		ModelAndView modelandview = new ModelAndView("LogoutPage");
		modelandview.addObject("logoutMessage"," You are successfully logged out of the HCPI application");

		return modelandview;

	}
		

	/*
	 * This method is just for redirection. Instead of going to a web page from
	 * the JSP directly, the flow is transferred to the Controller.
	 */

	@RequestMapping(value = "/welcome", params = "register")
	protected ModelAndView registerUser() {
		ModelAndView modelandview = new ModelAndView("RegisterNewUser");
		return modelandview;
	}

	/*
	 * This method is to register the new user. The new user details are saved
	 * to the database and the user is redirected to the Welcome page to enter
	 * the credentials. The credentials are then verified
	 */

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	protected ModelAndView registerUser(@RequestParam String userName, @RequestParam String password,
			HttpSession session) throws Exception {

		userCredentials.registerNewUser(userName, password);

		ModelAndView modelandview = new ModelAndView("WelcomePage");
		modelandview.addObject("welcomeMessage", "Please LOGIN to Proceed..");
		return modelandview;
	}

	/*
	 * This method opens the page to display the contents of the table
	 * selected by the user.
	 */

	@RequestMapping(value = "/welcome/OpenTablePage/{tableName}")
	protected ModelAndView openTable(@PathVariable String tableName) {

		ModelAndView modelandview = new ModelAndView("OpenTablePage");

		modelandview.addObject("consignmentHeaderList", loadTableSelectedByUser.getSelectedTableDetails(tableName));		
		
		modelandview.addObject("tableName", tableName);
		return modelandview;
	}

	/*
	 * This method transfers FULL DATA from remote application and displays
	 * appropriate message (success/failure) to the user when the process is
	 * finished.
	 */

	@RequestMapping(value = "/welcome/exportfulldata/{tableName}")
	protected ModelAndView exportFullData(@PathVariable String tableName) {
		ModelAndView modelandview = new ModelAndView("DataExportSuccessPage");

		remoteDataReceiverClient.getAllRemoteTableData(tableName);

		modelandview.addObject("message", "Data for table " + tableName + " exported successfully!!!");
		return modelandview;
	}

	/* This method opens the JSP to enter the index value based on which 
	 * the data is fetched for the table. This is for a single row data
	 * transfer only.
	 */

	@RequestMapping(value = "/welcome/tableindex/{tableName}")
	protected ModelAndView getIndexFromUser(@PathVariable String tableName) {

		ModelAndView modelandview = new ModelAndView("EnterTableIndex");
		modelandview.addObject("tableName", tableName);
		return modelandview;
	}

	
	/* This method posts the index value taken by the user and the data is 
	 * exported from the remote system based upon the inputted index value.
	 * The success/failure message is displayed as per the operation. 
	 */
	
	@RequestMapping(value = "/receiveIndex", method = RequestMethod.POST)
	protected ModelAndView receiveTableIndexFromUser(@RequestParam String indexValue, @RequestParam String tableName)
			throws Exception {
		
		remoteDataReceiverClient.getRemoteTableDataForIndexId(tableName,indexValue);

		ModelAndView modelandview = new ModelAndView("DataExportSuccessPage");
		modelandview.addObject("message", "Data for table " + tableName + " exported successfully!!!");
		return modelandview;
	}

}
