/*    */ package cn.gd.semaphore;
/*    */ 
/*    */ public class Semaphore {
/*    */   public static final String cvsid = "$Id: Semaphore.java,v 1.2 2001/12/19 06:44:54 hwh Exp $";
/*    */   private int value;
/*    */   
/*    */   public Semaphore() {
/*  8 */     this(1);
/*    */   }
/*    */   
/*    */   public Semaphore(int initial) {
/* 12 */     this.value = initial;
/*    */   }
/*    */   
/*    */   public void down() {
/* 16 */     down(1);
/*    */   }
/*    */   
/*    */   public synchronized void down(int count) {
/* 20 */     while (this.value < count) {
/*    */       try {
/* 22 */         wait();
/*    */       }
/*    */       catch (InterruptedException e) {}
/*    */     }
/*    */     
/*    */ 
/* 28 */     this.value -= count;
/*    */   }
/*    */   
/*    */   public void up() {
/* 32 */     up(1);
/*    */   }
/*    */   
/*    */   public synchronized void up(int count) {
/* 36 */     this.value += count;
/* 37 */     notify();
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/semaphore/Semaphore.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */