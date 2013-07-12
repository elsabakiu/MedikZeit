package models;

import java.util.*;

import javax.persistence.*;

import org.joda.time.DateTime;
 
import play.Play;
import play.data.validation.*;
import play.db.jpa.*;
 
@Entity
public class Address extends Model{

	public String city;
	public String zipCode;
	public String streetName;
	public int streetNumber;
	
	public Address()
	{
		
	}
	
	public String toString()
	{
		return city + " " + zipCode + " " + streetName;
	}
	
}
