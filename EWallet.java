public class EWallet implements Payment, Transfer {
	private int sdt;
	private double sodu;
	public EWallet(int sdt) {
		this.sdt = sdt;
		this.sodu = 0;
	}
	//
	public int getSdt() {
		return sdt;
	}
	public void setSdt(int sdt) {
		this.sdt = sdt;
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
		if(amount <= this.sodu){
			this.sodu -= amount;
			return true;
		}
		else
			return false;
	}
	public boolean transfer (double amount, Transfer to){
		double tientt = amount + transferFee * amount; 
		if(sodu > tientt){
			sodu -= tientt;
			if(to instanceof EWallet){
				((EWallet)to).topUp(amount);
			}else if(to instanceof BankAccount){
				((BankAccount)to).topUp(amount);
			}
		}
		return false;  
	}
    public double checkBalance(){
		return sodu;
	}
	public String toString() {
		return sdt + "," + sodu;
	}
}
