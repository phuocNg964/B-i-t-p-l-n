public class ConvenientCard implements Payment{
	private String type;	
	private IDCard tdd;
	private double sodu;
    public ConvenientCard(IDCard tdd) throws CannotCreateCard{
		this.tdd = tdd;	
		this.sodu = 100;
		String[] data = tdd.getNgaysinh().split("/");
		int namsinh = Integer.parseInt(data[2]);
		if(2023 - namsinh < 12 ){
			CannotCreateCard obj = new CannotCreateCard("Not enough age");
			throw obj;
		}
		else if(2023 - namsinh > 18 )
			this.type= "Adult";
		else
			this.type= "Student";
	}
	//
	public String getType(){
		return this.type;
	}
	public IDCard getTdd() {
		return tdd;
	}
	public void setTdd(IDCard tdd) {
		this.tdd = tdd;
	}
	public void setSodu(double sodu) {
		this.sodu = sodu;
	}
	//
	public void topUp(double money){
		this.sodu+= money;
	}
	@Override
	public boolean pay(double amount){
		double tientt=0;
		if(this.type.equals("Student"))
			tientt= amount;
		else 
			tientt = amount * 1.01;
		if(tientt > this.sodu)
			return false;
		else{
			this.sodu-= tientt; 
			return true;
		}
	}
	public double checkBalance(){
		return sodu;
	}
	public String toString() {
		return tdd + "," + type + "," + sodu;
	}
}
