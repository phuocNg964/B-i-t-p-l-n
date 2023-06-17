import java.util.*;

import java.io.*;

public class TransactionProcessing {
    private ArrayList<Payment> paymentObjects;
    private IDCardManagement idcm;
    
    public TransactionProcessing(String idCardPath, String paymentPath) {
        idcm = new IDCardManagement(idCardPath);
        readPaymentObject(paymentPath);
    }

    public ArrayList<Payment> getPaymentObject() {
        return this.paymentObjects;
    }
    // Requirement 3
    public boolean readPaymentObject(String path) {
        try{
            paymentObjects = new ArrayList<>();
            File f = new File(path);
            Scanner readObj = new Scanner(f);
            while(readObj.hasNextLine()){
                String data = readObj.nextLine();
                String[] info= data.split(",") ;
                if(info.length==1){
                    if(info[0].length()==6){
                        for(IDCard id : idcm.getIDCards()){
                            if(id.getSdd()==(Integer.parseInt(info[0]))){
                                try{
                                    ConvenientCard cc = new ConvenientCard(id);
                                    paymentObjects.add(cc);
                                }catch(CannotCreateCard e) {
                                    System.out.println(e);
                                }
                            }
                        }
                    }else{
                        for(IDCard id : idcm.getIDCards()){
                            if(id.getSdt()==(Integer.parseInt(info[0]))){
                                EWallet ew = new EWallet(Integer.parseInt(info[0]));
                                paymentObjects.add(ew);
                            }   
                        }
                    }
                }
                else{
                    for(IDCard id : idcm.getIDCards()){
                        if(id.getSdd()==(Integer.parseInt(info[0]))){
                            BankAccount ba = new BankAccount(Integer.parseInt(info[0]) , Double.parseDouble(info[1]));
                            paymentObjects.add(ba);
                        }
                    }
                }
            }
            readObj.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Requirement 4
    public ArrayList<ConvenientCard> getAdultConvenientCards() {
        ArrayList<ConvenientCard> acc = new ArrayList<>();
        for (Payment customer : paymentObjects){
                if(customer instanceof ConvenientCard)
                    if(((ConvenientCard)customer).getType().equals("Adult"))
                        acc.add(((ConvenientCard)customer));
            }
        return acc;
    }

    // Requirement 5
    public ArrayList<IDCard> getCustomersHaveBoth() {
        ArrayList<IDCard> allcards = new ArrayList<>();
        for (IDCard idc : idcm.getIDCards()) {
            int id = idc.getSdd();
            int sdt = idc.getSdt();
            int dem=0;
            for (Payment customer  : paymentObjects) {
                if(customer instanceof ConvenientCard && ((ConvenientCard)customer).getTdd().getSdd()==id)
                    dem++;
                else if(customer instanceof BankAccount && ((BankAccount)customer).getStk()==id)
                    dem++;
                else if(customer instanceof EWallet && ((EWallet)customer).getSdt()==sdt)
                    dem++;
            }
            if(dem==3)
                allcards.add(idc);
        }
        return allcards;
    }
    // Requirement 6
    public void processTopUp(String path) {
        try {
            File f = new File(path);
            Scanner readObj = new Scanner(f);
            while(readObj.hasNextLine()){
                String line = readObj.nextLine();
                String[] data = line.split(",");
                if(data[0].equals("CC")){
                    for(Payment customer : paymentObjects){
                        if(customer instanceof ConvenientCard)
                            if(Integer.parseInt(data[1]) == (((ConvenientCard)customer).getTdd()).getSdd())
                                ((ConvenientCard)customer).topUp(Integer.parseInt(data[2]));
                    }
                }
                else if(data[0].equals("EW")){
                    for(Payment customer : paymentObjects){
                        if(customer instanceof EWallet)
                            if(Integer.parseInt(data[1]) == (((EWallet)customer).getSdt()))
                                ((EWallet)customer).topUp(Integer.parseInt(data[2]));
                    }
                }
                else{
                    for(Payment customer : paymentObjects){
                        if(customer instanceof BankAccount)
                            if(Integer.parseInt(data[1]) == (((BankAccount)customer).getStk()))
                                ((BankAccount)customer).topUp(Integer.parseInt(data[2]));
                    }
                }
            }
            readObj.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
       
    }

    // Requirement 7
    public ArrayList<Bill> getUnsuccessfulTransactions(String path) {
        ArrayList<Bill> arr_bill = new ArrayList<>();
        try{
            File f = new File(path);
            Scanner readObj = new Scanner(f);
            while(readObj.hasNextLine()){
                String line = readObj.nextLine();
                String[] data = line.split(",");
                if(data[3].equals("BA")){
                    for(Payment p : paymentObjects){
                        if(p instanceof BankAccount)
                            if(((BankAccount)p).getStk() == Integer.parseInt(data[4]))   
                                if(!(((BankAccount)p).pay(Double.parseDouble(data[1])))){
                                    Bill b = new Bill(Integer.parseInt(data[0]),Double.parseDouble(data[1]),data[2]);
                                    arr_bill.add(b);
                                }
                    }
                }else if(data[3].equals("CC")){
                    for(Payment p : paymentObjects){
                        if(p instanceof ConvenientCard)
                            if(((ConvenientCard)p).getTdd().getSdd() == Integer.parseInt(data[4]))  
                                if(!(((ConvenientCard)p).pay(Double.parseDouble(data[1])))){
                                    Bill b = new Bill(Integer.parseInt(data[0]),Double.parseDouble(data[1]),data[2]);
                                    arr_bill.add(b);
                                }
                    }
                }else{
                    for(Payment p : paymentObjects){
                        if(p instanceof EWallet)
                            if(((EWallet)p).getSdt() == Integer.parseInt(data[4]))  
                                if(!(((EWallet)p).pay(Double.parseDouble(data[1])))){
                                    Bill b = new Bill(Integer.parseInt(data[0]),Double.parseDouble(data[1]),data[2]);
                                    arr_bill.add(b);
                                }
                    }
                }
            }
            readObj.close();
        }catch(FileNotFoundException e ){
            e.printStackTrace();
        }
        return arr_bill;
    }
    public void findMaxNumbers(ArrayList<Double> arr_payment, ArrayList<Integer> arr_sdd, ArrayList<BankAccount> arr_ba){
        double max_number=0;
            for(int i = 0; i < arr_payment.size(); i++){
                if(arr_payment.get(i) > max_number)
                    max_number = arr_payment.get(i);
            }
            AbstractList<Integer> index_maxs = new ArrayList<>();
            for(int i = 0; i < arr_payment.size(); i++){
                if(max_number == arr_payment.get(i))
                    index_maxs.add(arr_sdd.get(i));
            }
            for(Payment p : paymentObjects){
                for(Integer i : index_maxs){
                    if(p instanceof BankAccount && ((BankAccount)p).getStk() == i)
                        arr_ba.add((BankAccount)p);
                }
            }
    }
    // Requirement 8
    public ArrayList<BankAccount> getLargestPaymentByBA(String path) {
        ArrayList<BankAccount> arr_ba = new ArrayList<>();
        try{
            ArrayList<Double> arr_payment = new ArrayList<>();
            ArrayList<Integer> arr_sdd = new ArrayList<>();
            for(Payment p : paymentObjects){
                double total=0;
                int sodd=0;
                if(p instanceof BankAccount){
                    File f = new File(path);
                    Scanner readObj = new Scanner(f);
                    while(readObj.hasNextLine()){
                        String line = readObj.nextLine();
                        String[] data = line.split(",");
                        if(data[3].equals("BA") && Integer.parseInt(data[4])==((BankAccount)p).getStk()){
                            if((((BankAccount)p).pay(Double.parseDouble(data[1])))){
                                total += Double.parseDouble(data[1]);    
                                sodd = Integer.parseInt(data[4]);
                            }
                        }
                    }
                    readObj.close();
                }
                if(sodd != 0 && total !=0){
                    arr_sdd.add(sodd);
                    arr_payment.add(total);
                }
            }
            findMaxNumbers(arr_payment, arr_sdd, arr_ba);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return arr_ba;
    }
    //Requirement 9
    public boolean conditionalOfCustomer(String gender, String birthday, String purpose, Double bill){
        String[] year = birthday.split("/");
        if((2023 -Integer.parseInt(year[2])) < 18 && gender.equals("Female") && purpose.equals("Clothing") && bill > 500 )
            return true;
        else if((2023 -Integer.parseInt(year[2])) < 20 && gender.equals("Male") && purpose.equals("Clothing") && bill  > 500 )
            return true;
        else
            return false;
    }
    public void processTransactionWithDiscount(String path) {
        try{
            for(Payment p : paymentObjects){
                if(p instanceof BankAccount){
                    File f = new File(path);
                    Scanner readObj = new Scanner(f);
                    while(readObj.hasNextLine()){
                        String line = readObj.nextLine();
                        String[] data = line.split(",");
                        if(data[3].equals("BA") && Integer.parseInt(data[4])==((BankAccount)p).getStk()){
                            boolean payBA =(((BankAccount)p).pay(Double.parseDouble(data[1])));
                        }
                    }
                    readObj.close();
                }
                else if(p instanceof ConvenientCard){
                    File f = new File(path);
                    Scanner readObj = new Scanner(f);
                    while(readObj.hasNextLine()){
                        String line = readObj.nextLine();
                        String[] data = line.split(",");
                        if(data[3].equals("CC") && Integer.parseInt(data[4])==((ConvenientCard)p).getTdd().getSdd()){
                            boolean payCC =(((ConvenientCard)p).pay(Double.parseDouble(data[1])));
                        }
                    }
                    readObj.close();
                }
                else{
                    File f = new File(path);
                    Scanner readObj = new Scanner(f);
                    while(readObj.hasNextLine()){
                        Boolean disscount= true;
                        String line = readObj.nextLine();
                        String[] data = line.split(",");
                        if(data[3].equals("EW") && Integer.parseInt(data[4])==((EWallet)p).getSdt()){
                            for(IDCard idc : idcm.getIDCards()){
                                if(((EWallet)p).getSdt()==idc.getSdt()){
                                    if(conditionalOfCustomer(idc.getGtinh(), idc.getNgaysinh(), data[2], Double.parseDouble(data[1])))
                                        disscount = ((EWallet)p).pay((Double.parseDouble(data[1])*0.85));
                                    else
                                        disscount = ((EWallet)p).pay(Double.parseDouble(data[1]));
                                }
                            }
                        }
                    }
                    readObj.close();
                }
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}

