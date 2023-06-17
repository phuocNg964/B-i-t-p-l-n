public class IDCard {
      private int sdd;
      private String hoten;
      private String gtinh;
      private String ngaysinh;
      private String diachi;
      private int sdt;
      public IDCard(){}
      public IDCard(int sdd, String hoten, String gtinh, String ngaysinh, String diachi, int sdt) {
            this.sdd = sdd;
            this.hoten = hoten;
            this.gtinh = gtinh;
            this.ngaysinh = ngaysinh;
            this.diachi = diachi;
            this.sdt = sdt;
      }
      public int getSdd() {
            return sdd;
      }
      public void setSdd(int sdd) {
            this.sdd = sdd;
      }
      public String getHoten() {
            return hoten;
      }
      public void setHoten(String hoten) {
            this.hoten = hoten;
      }
      public String getGtinh() {
            return gtinh;
      }
      public void setGtinh(String gtinh) {
            this.gtinh = gtinh;
      }
      public String getNgaysinh() {
            return ngaysinh;
      }
      public void setNgaysinh(String ngaysinh) {
            this.ngaysinh = ngaysinh;
      }
      public String getDiachi() {
            return diachi;
      }
      public void setDiachi(String diachi) {
            this.diachi = diachi;
      }
      public int getSdt() {
            return sdt;
      }
      public void setSdt(int sdt) {
            this.sdt = sdt;
      }
      @Override
      public String toString() {
            return sdd + "," + hoten + "," + gtinh + "," + ngaysinh+ "," + diachi + "," + sdt;
      }
}
