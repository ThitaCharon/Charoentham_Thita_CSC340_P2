import java.util.ArrayList;
import java.util.concurrent.Semaphore;



public class DriveApp {

	public  long time = System.currentTimeMillis();
	public 	ArrayList<Thread> stulist ;

	public static void main(String[] args) {
		DriveApp drive = new DriveApp();
		drive.doWork();
	}

	public void doWork()
	{
		// list of student and id
		stulist = new ArrayList<Thread>();

		// keep tracking of students' name and their grades
		ArrayList<Thread>  sharedQueue = new ArrayList<Thread>();

		Classroom csc = new Classroom();	
		Instructor instructor = new Instructor(csc,sharedQueue); 		// create instructor
		//Instructor instructor = new Instructor();
		// create 14 students Threads
		for (int i = 0; i < 14; i++){
			stulist.add(new Student(i, instructor, csc, sharedQueue));
			stulist.get(i).start();
		}
		instructor.start();
	}
	
	
	
	
}



