package controllers;

import play.classloading.ApplicationClasses.ApplicationClass;
import play.data.validation.*;
import play.mvc.Controller;
import models.*;

import java.util.*;

public class Authenticate extends Controller {
	
	public static void register() {
		render();
	}
	
	public static void doRegister(@Valid User user) {
		//if error --> Register else save
		user.userType = (UserType) UserType.find(" userType = ?1 ", "Patient").fetch().get(0);
		if (!user.validateAndSave()) {
			params.flash();
			validation.keep();
			register();
		}
		//no errors --> log user
		session.put("loggedInUser", user.email);
		session.put("welcomeMessage", "Welcome to MedikZeit!");
		profile();
	}
	
	public static void registerDoctor()
	{
		render();
	}
	
	public static void saveDoctor(Doctor doctor)
	{	
		doctor.user.address = new Address();
		doctor.user.firstname = doctor.user.firstname;
		doctor.user.lastname = doctor.user.lastname;
		doctor.user.email = doctor.user.email;
		doctor.user.password = doctor.user.password;
		doctor.user.userType = (UserType) UserType.find(" userType = ?1 ", "Doctor").fetch().get(0);
		doctor.user.insurance = (Insurance) Insurance.findById(doctor.user.insurance.id);
		doctor.user.address.save();
		doctor.user.insurance.save();
		doctor.user.save();
		
		doctor.docClinicName = doctor.docClinicName;
		doctor.docSpecialization = (DoctorSpecialization) DoctorSpecialization.findById(doctor.docSpecialization.id);
		doctor.docSpecialization.save();
		doctor.save();
		
		session.put("loggedInUser", doctor.user.email);
		session.put("welcomeMessage", "Welcome to MedikZeit!");
		profile();
	}
	
	public static void login() {
		
		render();
	
	}
	
	public static void doLogin(String email, String password) {
		List<User> users = User.find("email=? AND password=?", email, password).fetch();
		if (users.size() == 1) 
		{
			session.put("loggedInUser", email);
			profile();
		} 
		else 
		{
			validation.addError("email", "Incorrect email/password. Please try again!");
			validation.keep();
			login();
		}
	}
	
	public static void profile()
	{
		String email = session.get("loggedInUser");
		User loggedInUser = (User) User.find("email = ? ", email).fetch().get(0);
		if(loggedInUser.userType.userType.equals("Admin"))
			CRUD.index();
		else if(loggedInUser.userType.userType.equals("Doctor"))
			UserProfiles.doctorProfile();
		else 
			UserProfiles.patientProfile();
	}
	
	
	public static void logout() {
		session.remove("loggedInUser");
		Application.index();
	}
	
	public static void myIndex() {
		if (session.get("loggedInUser") == null) {
			Authenticate.login();
		}
        render();
	}

}

