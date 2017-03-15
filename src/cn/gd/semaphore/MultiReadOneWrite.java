/*    */ package cn.gd.semaphore;
/*    */ 
/*    */ public class MultiReadOneWrite {
/*    */   public static final String cvsid = "$Id: MultiReadOneWrite.java,v 1.2 2001/12/19 06:44:54 hwh Exp $";
/*  5 */   private Semaphore db = new Semaphore();
/*  6 */   private int readers = 0;
/*    */   
/*    */   public synchronized void enterRead() {
/*  9 */     this.readers += 1;
/* 10 */     if (this.readers == 1) {
/* 11 */       this.db.down();
/*    */     }
/*    */   }
/*    */   
/*    */   public synchronized void leaveRead() {
/* 16 */     this.readers -= 1;
/* 17 */     if (this.readers == 0) {
/* 18 */       this.db.up();
/*    */     }
/*    */   }
/*    */   
/*    */   public void enterWrite() {
/* 23 */     this.db.down();
/*    */   }
/*    */   
/*    */   public void leaveWrite() {
/* 27 */     this.db.up();
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/semaphore/MultiReadOneWrite.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */