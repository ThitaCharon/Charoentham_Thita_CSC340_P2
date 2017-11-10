import java.util.ArrayList;

public class Classroom {
	public String [] namelist;
	public int capacity;
	public int Nstudents; 
	public int Nexam; // count number of exam offer
	ArrayList<String> stulist;
	public ArrayList<Thread> studentInClass;
	public Classroom ( ){
		stulist = new ArrayList<String>();
		Nstudents = 0;
		capacity = 0;
		Nexam = 1;
	}

	public int getCapacity(){
		return capacity;
	}
	/*
	public void incCapacity(){
		++capacity;
	}
	public int decCapacity(){
		return --capacity;
	}
	public void incNstudents(){
		++Nstudents;
	}
	public int getNstudents(){
		return Nstudents;
	}
	public void incNexam(){
		++Nexam;
	}
	public int getNexam(){
		return Nexam;
	}
	*/


}
