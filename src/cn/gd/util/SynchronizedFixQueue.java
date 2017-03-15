/*    */ package cn.gd.util;
/*    */ 
/*    */ import cn.gd.semaphore.MultiReadOneWrite;
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
/*    */ public class SynchronizedFixQueue
/*    */   extends FixQueue
/*    */ {
/*    */   public static final String cvsid = "$Id: SynchronizedFixQueue.java,v 1.4 2002/09/26 02:50:29 hwh Exp $";
/* 20 */   private MultiReadOneWrite controler = new MultiReadOneWrite();
/*    */   
/*    */ 
/*    */   public SynchronizedFixQueue(int newMaxQueueSize, String queueID)
/*    */   {
/* 25 */     super(newMaxQueueSize, queueID);
/*    */   }
/*    */   
/*    */   public void addBack(Object obj) {
/* 29 */     this.controler.enterWrite();
/* 30 */     super.addBackNoNotify(obj);
/* 31 */     this.controler.leaveWrite();
/* 32 */     fireQueueAddElementEventListener(new QueueAddElementEvent(this, this.queueID, obj));
/*    */   }
/*    */   
/*    */   public void popFront() {
/* 36 */     this.controler.enterWrite();
/* 37 */     super.popFront();
/* 38 */     this.controler.leaveWrite();
/*    */   }
/*    */   
/*    */   public void removeAll() {
/* 42 */     this.controler.enterWrite();
/* 43 */     super.removeAll();
/* 44 */     this.controler.leaveWrite();
/*    */   }
/*    */   
/*    */   public Object getFront() {
/* 48 */     this.controler.enterRead();
/* 49 */     Object obj = super.getFront();
/* 50 */     this.controler.leaveRead();
/* 51 */     return obj;
/*    */   }
/*    */   
/*    */   public Object getBack() {
/* 55 */     this.controler.enterRead();
/* 56 */     Object obj = super.getBack();
/* 57 */     this.controler.leaveRead();
/* 58 */     return obj;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 62 */     this.controler.enterRead();
/* 63 */     boolean bEmpty = super.isEmpty();
/* 64 */     this.controler.leaveRead();
/* 65 */     return bEmpty;
/*    */   }
/*    */   
/*    */   public Object at(int index) {
/* 69 */     this.controler.enterRead();
/* 70 */     Object obj = super.at(index);
/* 71 */     this.controler.leaveRead();
/* 72 */     return obj;
/*    */   }
/*    */   
/*    */   public int getSize() {
/* 76 */     this.controler.enterRead();
/* 77 */     int size = super.getSize();
/* 78 */     this.controler.leaveRead();
/* 79 */     return size;
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/util/SynchronizedFixQueue.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */