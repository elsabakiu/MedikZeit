package controllers;

import play.*;
import play.mvc.*;

import java.util.*;
import java.util.Calendar;
import java.net.*;
import java.io.*;

import models.*;

public class AppointmentBooking extends Controller {
	
	public static void bookAppointment(Doctor doctor, Calendar appointmentDate)
	{
		render(doctor, appointmentDate);
	}
	
	public static void sendPinCode(Doctor doctor, Calendar appointmentDate, String phoneNumber)
	{
		 try {
        // Construct data
		String pinCode = randomString(4);
		session.put("pinCode", pinCode);
        String data = "uname=" + URLEncoder.encode("dt.detianyin@gmail.com", "UTF-8");
        data += "&pword=" + URLEncoder.encode("p31415926", "UTF-8");
        data += "&message=" + URLEncoder.encode(pinCode, "UTF-8");
        data += "&from=" + URLEncoder.encode("Medik Zeit", "UTF-8");
        data += "&selectednums=" + URLEncoder.encode(phoneNumber, "UTF-8");
        data += "&info=" + URLEncoder.encode("1", "UTF-8");
        // Send data
        URL url = new URL("https://www.txtlocal.com/sendsmspost.php");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        String sResult="";
        while ((line = rd.readLine()) != null) {
            // Process line...
            sResult=sResult+line+" ";
        }
        wr.close();
        rd.close();
        enterPinCode(doctor, appointmentDate);
        
    } catch (Exception e) 
    {
        System.out.println("Error SMS "+e);
    }
	}
	
	public static void enterPinCode(Doctor doctor, Calendar appointmentDate)
	{
		render(doctor, appointmentDate);
	}
	
	public static void confirmPinCode(Doctor doctor, Calendar appointmentDate, String pinCode)
	{
		String sentPinCode = session.get("pinCode");
		if(sentPinCode.equals(pinCode))
		{
			saveAppointment(doctor, appointmentDate);
		}
		else
		{
			flash("invalidPinCode", "Invalid pin code!");
			enterPinCode(doctor, appointmentDate);
		}
	}

	
	public static void saveAppointment(Doctor doctor, Calendar appointmentDate)
	{
		session.remove("pinCode");
		String loggedInUserEmail = session.get("loggedInUser");
		User loggedInUser = (User) User.find(" email = ?1 " , loggedInUserEmail).fetch().get(0);
		Doctor doc = (Doctor) Doctor.find(" user.email = ?1 ", doctor.user.email).fetch().get(0);
		Date appointmentStartTime = appointmentDate.getTime();
		Appointment appointment = new Appointment(appointmentStartTime, loggedInUser, doc);
		appointment.save();
		
		appointmentDate.add(Calendar.MINUTE, 30);
		Date appointmentEndTime = appointmentDate.getTime();
		String title = "Consultation Appointment";
		String body = "Patient: " + loggedInUser.firstname + " " + loggedInUser.lastname;
		Event event = new Event(appointmentStartTime, appointmentEndTime, title, body, doc, loggedInUser);
		event.save();
		
		saveAppointmentConfirmation(doctor, appointmentDate);
	}
	
	public static void saveAppointmentConfirmation(Doctor doctor, Calendar appointmentDate)
	{
		render(doctor, appointmentDate);
	}
	
	public static String randomString( int len ) 
	{
		String AB = "0123456789";
		Random rnd = new Random();

	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
}
