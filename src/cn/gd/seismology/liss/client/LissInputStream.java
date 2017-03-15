/*    */ package cn.gd.seismology.liss.client;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */ public class LissInputStream
/*    */   extends FilterInputStream
/*    */ {
/*    */   private LissClient client;
/*    */   
/*    */   LissInputStream(InputStream istr, LissClient client)
/*    */     throws IOException
/*    */   {
/* 32 */     super(istr);
/* 33 */     this.client = client;
/*    */   }
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 37 */     return this.in.read(b, off, len);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 45 */     super.close();
/* 46 */     this.client.closeTransferSocket();
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/liss/client/LissInputStream.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */