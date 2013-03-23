package webboards.tools.crt;


public class CRT {
	private final String[][] crt = {
			{"     ","1:3 ","1:2 ","1:1 ","2:1 ","3:1 ","4:1 ","5:1+"},
			{"2    ","A1r2","A1r2","A1r2","A1r1","A1r1","A1  ","A1  "},
			{"3    ","A1r2","A1r2","A1r1","A1r1","A1  ","A1  ","A1D1"},
			{"4    ","A1r2","A1r1","A1r1","A1  ","A1  ","A1D1","D1r1"},
			{"5    ","A1r1","A1r1","A1  ","A1  ","A1D1","D1r1","D1r2"},
			{"6    ","A1r1","A1  ","A1  ","A1D1","D1r1","D1r2","D1r2"},
			{"7    ","A1  ","A1  ","A1D1","D1r1","D1r2","D1r2","D1r3"},
			{"8    ","A1  ","A1  ","A1D1","D1r1","D1r2","D1r2","D1r3"},//same
			{"9    ","A1  ","A1D1","D1r1","D1r2","D1r2","D1r3","D1r4"},
			{"10   ","A1  ","D1r1","D1r2","D1r2","D1r3","D1r3","D2r5"}, 
			{"11   ","A1  ","D1r1","D1r2","D1r2","D1r3","D1r3","D2r5"},//same 
			{"12   ","A1D1","D1r2","D1r2","D1r3","D1r3","D2r4","D2r6"}, 
	};
	
	public static void main(String[] args) {
		new CRT().run();
	}

	private void run() {
		int maxRoll = Integer.parseInt(crt[crt.length-1][0].trim());
		int minRoll = Integer.parseInt(crt[1][0].trim());
		int cols = crt[0].length-1;
		System.out.println("CombatResult[][] crt=new CombatResult["+(maxRoll-minRoll)+"]["+cols+"];");
		for (int row = 1; row < crt.length; row++) {			
			String s = String.format("crt[%2d]=crt(", row-1);
			for (int i = 1; i < crt[row].length; i++) {
				if(i>1){
					s+=", ";
				}
				String val = crt[row][i];
				s+='"'+val+'"';
			}
			s+=");";
			System.out.println(s);
		}
	}
	protected void foo() {
		CombatResult[][] crt=new CombatResult[10][7];
		crt[ 0]=crt("A1r2", "A1r2", "A1r2", "A1r1", "A1r1", "A1  ", "A1  ");
		crt[ 1]=crt("A1r2", "A1r2", "A1r1", "A1r1", "A1  ", "A1  ", "A1D1");
		crt[ 2]=crt("A1r2", "A1r1", "A1r1", "A1  ", "A1  ", "A1D1", "D1r1");
		crt[ 3]=crt("A1r1", "A1r1", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2");
		crt[ 4]=crt("A1r1", "A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2");
		crt[ 5]=crt("A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3");
		crt[ 6]=crt("A1  ", "A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3");
		crt[ 7]=crt("A1  ", "A1D1", "D1r1", "D1r2", "D1r2", "D1r3", "D1r4");
		crt[ 8]=crt("A1  ", "D1r1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r5");
		crt[ 9]=crt("A1  ", "D1r1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r5");
		crt[10]=crt("A1D1", "D1r2", "D1r2", "D1r3", "D1r3", "D2r4", "D2r6");	}

	private CombatResult[] crt(String... string) {
		// TODO Auto-generated method stub
		return null;
	}
}
