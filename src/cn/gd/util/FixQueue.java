/*     */ package cn.gd.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.LinkedList;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FixQueue
/*     */ {
/*     */   public static final String cvsid = "$Id: FixQueue.java,v 1.4 2002/09/26 02:50:29 hwh Exp $";
/*  22 */   protected EventListenerList listenerList = new EventListenerList();
/*     */   
/*  24 */   private FixQueueException fixQueueExcep = new FixQueueException("Queue's MaxQueueLength is Exceed");
/*     */   
/*     */   private LinkedList list;
/*     */   private int maxQueueSize;
/*  28 */   protected String queueID = "";
/*     */   
/*     */   public FixQueue(int newMaxQueueSize, String queueID) {
/*  31 */     this.maxQueueSize = newMaxQueueSize;
/*  32 */     this.queueID = queueID;
/*  33 */     this.list = new LinkedList();
/*     */   }
/*     */   
/*     */   public synchronized int getListenerCount() {
/*  37 */     return this.listenerList.getListenerCount();
/*     */   }
/*     */   
/*     */   public synchronized void addQueueAddElementEventListener(QueueAddElementEventListener listener) {
/*  41 */     this.listenerList.add(QueueAddElementEventListener.class, listener);
/*     */   }
/*     */   
/*     */   public synchronized void removeQueueAddElementEventListener(QueueAddElementEventListener listener) {
/*  45 */     this.listenerList.remove(QueueAddElementEventListener.class, listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireQueueAddElementEventListener(QueueAddElementEvent e)
/*     */   {
/*  56 */     Object[] listeners = this.listenerList.getListenerList();
/*     */     
/*     */ 
/*  59 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/*  60 */       if (listeners[i] == QueueAddElementEventListener.class) {
/*  61 */         ((QueueAddElementEventListener)listeners[(i + 1)]).QueueAddElementHandler(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addBack(Object obj) {
/*  67 */     this.list.addLast(obj);
/*  68 */     if (this.list.size() > this.maxQueueSize) {
/*  69 */       this.list.removeFirst();
/*     */     }
/*  71 */     fireQueueAddElementEventListener(new QueueAddElementEvent(obj, this.queueID, obj));
/*     */   }
/*     */   
/*     */   public void removeAll() {
/*  75 */     this.list.clear();
/*     */   }
/*     */   
/*     */   protected void addBackNoNotify(Object obj) {
/*  79 */     this.list.addLast(obj);
/*  80 */     if (this.list.size() > this.maxQueueSize) {
/*  81 */       this.list.removeFirst();
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getFront() {
/*  86 */     return this.list.getFirst();
/*     */   }
/*     */   
/*     */   public Object getBack() {
/*  90 */     return this.list.getLast();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  94 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   public void popFront() {
/*  98 */     this.list.removeFirst();
/*     */   }
/*     */   
/*     */   public Object at(int index) {
/* 102 */     return this.list.get(index);
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 106 */     return this.list.size();
/*     */   }
/*     */   
/*     */   public int getMaxQueueSize() {
/* 110 */     return this.maxQueueSize;
/*     */   }
/*     */   
/*     */   public void setMaxQueueSize(int newMaxQueueSize) {
/* 114 */     this.maxQueueSize = newMaxQueueSize;
/*     */   }
/*     */   
/*     */   public String getQueueID() {
/* 118 */     return this.queueID;
/*     */   }
/*     */   
/*     */   public static void main(String[] argv)
/*     */   {
/* 123 */     FixQueue fixQueue = new FixQueue(5, "ID");
/* 124 */     for (int i = 0; i < 10; i++) {
/* 125 */       fixQueue.addBack(new Integer(i));
/*     */     }
/* 127 */     Integer I = (Integer)fixQueue.at(2);
/* 128 */     System.out.println(fixQueue.getFront());
/* 129 */     System.out.println(fixQueue.getBack());
/* 130 */     fixQueue.popFront();
/* 131 */     System.out.println(fixQueue.getFront());
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/util/FixQueue.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */