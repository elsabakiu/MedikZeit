package models;

import java.util.*;

import javax.persistence.*;

import org.joda.time.DateTime;
 
import play.Play;
import play.data.validation.*;
import play.db.jpa.*;

@Entity
public class UserType extends Model{
	
	
	public String userType;
	
	public String toString()
	{
		return userType;
	}

}
