package ccdr.rainbow.Tool;

import java.util.Comparator;

public class PinYinComparator implements Comparator<Contacts> {

	@Override
	public int compare(Contacts lhs, Contacts rhs) {
		// TODO Auto-generated method stub
		System.out.println("lhs--"+lhs.getName()+"--"+rhs.getName());
		if(lhs.getPy().equals(rhs.getPy())){
			return 0 ;
		}else if(lhs.getPy().toCharArray()[0]<rhs.getPy().toCharArray()[0]){	
			return -1 ;
		}else{
			return 1 ;
		}

	}
	

}
