package controllers;

import play.data.validation.*;
import play.mvc.Controller;
import models.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.Future;
import org.apache.commons.io.IOUtils;
import play.*;


public class UserProfiles extends Controller {
	
	public static void patientProfile()
	{
		String email = session.get("loggedInUser");
		User user = (User) User.find("email = ? ", email).fetch().get(0);
		render(user);
	}
	
	
	public static void doctorProfile()
	{
		String email = session.get("loggedInUser");
		Doctor doctor = (Doctor) Doctor.find(" user.email = ? ", email).fetch().get(0);
		render(doctor);
	}
	
	public static void doctorCalendar()
	{
		render();
	}
	
	
	public static void savePatientPersonalData()
	{
		patientProfile();
	}
	
	public static void saveDoctorPersonalData(Doctor doctor, String email)
	{
		Doctor updDoctor = (Doctor) Doctor.find(" user.email = ?1 ", doctor.user.email).fetch().get(0);
		updDoctor.user.address.streetName = doctor.user.address.streetName;
		updDoctor.user.address.streetNumber = doctor.user.address.streetNumber;
		updDoctor.user.address.city = doctor.user.address.city;
		updDoctor.user.address.zipCode = doctor.user.address.zipCode;
		
		updDoctor.user.firstname = doctor.user.firstname;
		updDoctor.user.lastname = doctor.user.lastname;
		updDoctor.user.gender = doctor.user.gender;
		updDoctor.user.email = doctor.user.email;
		updDoctor.user.password = doctor.user.password;
		updDoctor.user.phoneNumber = doctor.user.phoneNumber;
		updDoctor.user.userType = (UserType) UserType.find(" userType = ?1 ", "Doctor").fetch().get(0);
		updDoctor.user.insurance = (Insurance) Insurance.findById(doctor.user.insurance.id);
		updDoctor.user.address.save();
		updDoctor.user.insurance.save();
		updDoctor.user.save();
		
		updDoctor.docClinicName = doctor.docClinicName;
		updDoctor.docPhoto = doctor.docPhoto;
		updDoctor.docSpecialization = (DoctorSpecialization) DoctorSpecialization.findById(doctor.docSpecialization.id);
		updDoctor.docSpecialization.save();
		updDoctor.save();
		
		flash("saveMessage", "Your personal data was successfully saved!");
		doctorProfile();
	}
	
	
	//Upload photo of doctor to the server
	public static void upload(String qqfile) 
	{
		if (request.isNew) {

		    FileOutputStream moveTo = null;

		    Logger.info("Name of the file %s", qqfile);
		    // Another way I used to grab the name of the file
		    String filename = request.headers.get("x-file-name").value();

		    Logger.info("Absolute on where to send %s", Play.getFile("").getAbsolutePath() + File.separator + "public" + File.separator + "images" + File.separator + "DoctorProfilePhotos" + File.separator);
		    try {

		        InputStream data = request.body;


		        moveTo = new FileOutputStream(new File(Play.getFile("").getAbsolutePath()) + File.separator + "public" + File.separator + "images" + File.separator + "DoctorProfilePhotos" + File.separator + filename);
		        IOUtils.copy(data, moveTo);

		    } catch (Exception ex) {

		        // catch file exception
		        // catch IO Exception later on
		        renderJSON("{success: false}");
		    }
		}
		renderJSON("{success: true}");
	} 
	
}
