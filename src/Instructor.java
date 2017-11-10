import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Instructor extends Thread {
	public static Semaphore ready = new Semaphore(0);
	public static Semaphore laststu = new Semaphore(0);
	public static Semaphore grade = new Semaphore(0);
	public static Semaphore waitfriend = new Semaphore(0);
	public static Semaphore postgrade = new Semaphore(0);
	
	public static long time = System.currentTimeMillis();
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+ getName()+": "+ m);
	}

	private final ArrayList<Thread> sharedQueue;
	private Classroom cs;
	private int gradeTable[][];
	public int nExam = 0;
	public static int range = 0;

	Instructor(Classroom cs, ArrayList<Thread> share){
		gradeTable = new int[14][3];
		sharedQueue = share;
		this.cs = cs;
		setName("Instructor ");
		msg("is walking to class");
	}

	public void run(){

		try{
			while (nExam < 3){

				Thread.sleep((long) (8000 * Math.random()));
				msg("arrive........open the class La.La..La. ");

				for (int i = 0; i < (Student.countstu%8)+8 ;i++){
					Student.door.release();
				}
				msg("Go.....");
				ready.acquire();
				msg( "----- Exam : " + (nExam+1) + " start -----");

				for (int i = 0; i<(Student.queue%8)+8 ;i++){
					msg("hand out exam to " + sharedQueue.get(i).getName() );
					Student.waitexam.release();
				}

				Thread.sleep(6000);	// sleep during the exam
				
				// weak up allow students leave the class room
				msg("leave........");
				for (int i = 0; i<(Student.queue%8)+8 ;i++){
					grade.release();
				}

				// after last student signal instructor to give grade
				laststu.acquire();
				msg("last student leave");
				msg("will give the grade");
				if (nExam == 0){
					for (int i= 0; i< sharedQueue.size();i++){	
						int id = Integer.valueOf((sharedQueue.get(i).getName().substring(8)));
						msg("id is :" + id );
						gradeTable[id][nExam] = getPoint();
							}
					range = sharedQueue.size();
				}
				else if (nExam != 0){
					for (int i= range; i< sharedQueue.size();i++){	
						int id = Integer.valueOf((sharedQueue.get(i).getName().substring(8)));
						msg("id is :" + id );
						gradeTable[id][nExam] = getPoint();
						}
					range = sharedQueue.size();
				}
				++nExam;
			}//endwhile
			
			postgrade.acquire();
			msg("Post grade");
			postGrade();
			
			msg("let everyone go home");
			for (int i=0; i<14;i++){
				waitfriend.release();
			}

		}catch(InterruptedException e){ }



	}

	private void grade(){
		for (int i=0; i<sharedQueue.size();i++){	
			// get student id get a grade and let student leave the room
			int id = Integer.valueOf((sharedQueue.get(i).getName().substring(8)));
			gradeTable[id][nExam] = getPoint();
		}
	}

	private int getPoint() {
		return  ((int) ((Math.random() * (100 - 10))) + 10);

	}
	private void postGrade() {
		for (int i=0; i<14;i++){	
			System.out.print("Student_"+ i);
			for(int j=0; j<3; j++){
				System.out.print("    " + gradeTable[i][j]);
			}
			System.out.println();
		}
	}
}