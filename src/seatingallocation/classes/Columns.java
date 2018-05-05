package seatingallocation.classes;

public enum Columns { 
	A(0), B(1), C(2), D(3), E(4), F(5);
	
	int index = 0;
	
	Columns(int index){
		this.index = index;
	}
	
	public static Columns byIndex(int index) {
		for(Columns col : Columns.values()) {
			if (col.index == index) {
				return col;
			}
		}
		return null;
	}
}
