/*    */ package cn.gd.util;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ 
/*    */ public class QueueAddElementEvent extends EventObject
/*    */ {
/*    */   public static final String cvsid = "$Id: QueueAddElementEvent.java,v 1.2 2001/12/19 06:44:48 hwh Exp $";
/*    */   private String queueID;
/*    */   private Object value;
/*    */   
/*    */   public QueueAddElementEvent(Object source, String queueID, Object value)
/*    */   {
/* 13 */     super(source);
/* 14 */     this.queueID = queueID;
/* 15 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getQueueID() {
/* 19 */     return this.queueID;
/*    */   }
/*    */   
/*    */   public Object getValue() {
/* 23 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/util/QueueAddElementEvent.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */