public class BankAccount implements Payment, Transfer{
    private int stk;
    private double laisuat;
    private double sodu;
    public BankAccount(int stk, double laisuat) {
        this.stk = stk;
        this.laisuat = laisuat;
        this.sodu = 50;
    }
    //
    public int getStk() {
        return stk;
    }
    public void setStk(int stk) {
        this.stk = stk;
    }
    public double getLaisuat() {
        return laisuat;
    }
    public void setLaisuat(double laisuat) {
        this.laisuat = laisuat;
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
        if(amount + 50 <= this.sodu){
            this.sodu -= amount;
            return true;
        }
        else
            return false;
    }
    public boolean transfer (double amount, Transfer to){
		double tientt = amount + transferFee * amount;
		if(sodu > tientt + 50){
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
		return stk + "," + laisuat + "," + sodu;
	}
}
