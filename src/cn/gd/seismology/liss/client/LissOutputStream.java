/*    */ package cn.gd.seismology.liss.client;
/*    */ 
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LissOutputStream
/*    */   extends FilterOutputStream
/*    */ {
/*    */   private LissClient client;
/*    */   
/*    */   LissOutputStream(OutputStream ostr, LissClient client)
/*    */     throws IOException
/*    */   {
/* 36 */     super(ostr);
/* 37 */     this.client = client;
/*    */   }
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 41 */     this.out.write(b, off, len);
/*    */   }
/*    */   
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 48 */     super.close();
/* 49 */     this.client.closeTransferSocket();
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/liss/client/LissOutputStream.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */