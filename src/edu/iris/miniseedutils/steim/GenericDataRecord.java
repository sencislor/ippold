/*    */ package edu.iris.miniseedutils.steim;
/*    */ 
/*    */ public abstract class GenericDataRecord
/*    */ {
/*    */   public static final String cvsid = "$Id: GenericDataRecord.java,v 1.2 2001/12/19 07:02:44 hwh Exp $";
/*    */   int numHeaderFrames;
/*    */   int numDataFrames;
/*    */   int frameLength;
/*    */   int recordLength;
/*    */   int dataLength;
/*    */   int headLength;
/*    */   byte[] head;
/*    */   byte[] data;
/*    */   CompressedFrame[] frames;
/*    */   
/*    */   protected GenericDataRecord()
/*    */   {
/* 18 */     this.numHeaderFrames = 0;
/* 19 */     this.numDataFrames = 0;
/* 20 */     this.frameLength = 0;
/* 21 */     this.recordLength = 0;
/* 22 */     this.dataLength = 0;
/* 23 */     this.headLength = 0;
/* 24 */     this.head = null;
/* 25 */     this.data = null;
/* 26 */     this.frames = null;
/*    */   }
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
/* 50 */   boolean byteSwappedRec = false;
/*    */   
/*    */   public abstract String toString();
/*    */   
/*    */   public abstract byte[] getBytes();
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/edu/iris/miniseedutils/steim/GenericDataRecord.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */