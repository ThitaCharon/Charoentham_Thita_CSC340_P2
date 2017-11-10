import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Student extends Thread implements Runnable {
	public static Semaphore mutex = new Semaphore(1);
	public static Semaphore door = new Semaphore(0);
	public static Semaphore waitexam = new Semaphore(0);
	public static int queue = 0;
	public static long time = System.currentTimeMillis();

	public int id;
	private Instructor prof;
	private Classroom cs;
	private String name;
	public static int MAX = 8;
	private final ArrayList<Thread> sharedQueue;
	public static int countstu; // number of students
	//private boolean wait = true;
	private int examday = 0;
	private int countexam = 0;
	private int lastStu = 24;

	Student(int idN, Instructor instructor, Classroom csc, ArrayList<Thread> share){
		prof = instructor;
		sharedQueue = share;
		name = ("student_"+ idN);
		setName(name);
		id = idN;
		cs= csc;	
	}

	public void run(){

		try{
			// students come to school by random time
			// while loop will run 3 exam each student needs to take 2 exam
			while (examday <= 3){
				Thread.sleep((long) (3000 * Math.random()));
				// check if don't need to take exam or last group 
				if (countexam == 2){break;}
				if (sharedQueue.size() == 24 ){break;}
				msg("wait of class open ");
				door.acquire();

				mutex.acquire();
				countstu++;
				++examday;
				
			
				if (countstu%MAX<= MAX){
					
					queue++;
					++countexam;
					
					sharedQueue.add(Thread.currentThread());
				
					msg("get in class for exam student queue is "+ queue);
					msg( "wait for exam  zzZZ");
					
					mutex.release();
					
					// last student will signal to profess when they are ready
					
					if (waitexam.getQueueLength() - (MAX*2) > 0){
						if (waitexam.getQueueLength() == waitexam.getQueueLength()-1)
						{
							Instructor.ready.release();}
					}
					
					if (waitexam.getQueueLength() == MAX-1){
						Instructor.ready.release();} 
					// for the last group
					if (queue == 24){
						Instructor.ready.release();} 
					
					waitexam.acquire();
					msg("taking exam total = " + countexam);
					Thread.sleep(5000);
					msg("wait prof allow to leave");
					Instructor.grade.acquire();
					msg("get score leave room and take a break");
					if (waitexam.getQueueLength()==0){
						Instructor.laststu.release();}
					
					if (countexam == 2 ){ break;}
					// will take a break sleep by random time
				
					Thread.sleep((long) (5000 * Math.random()));
					msg("wait outside");
					if (sharedQueue.size() == 24 ){break;}
					
				}
				else{
					if (countexam == 2 ){ break;}
					queue++;
					mutex.release();
				}
				
			}// end while
			
			// wait for grade post
			msg("wait for friends ");
			if(lastStu== sharedQueue.size()){
					Instructor.postgrade.release();}
			Instructor.waitfriend.acquire();
			
			msg("Lol...........home");

		}catch(InterruptedException e){ }




	}

	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName()+": "+ m);
	}

}