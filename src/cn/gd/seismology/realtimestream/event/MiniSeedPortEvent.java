/*    */ package cn.gd.seismology.realtimestream.event;
/*    */ 
/*    */ import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;
/*    */ import java.util.EventObject;
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
/*    */ public class MiniSeedPortEvent
/*    */   extends EventObject
/*    */ {
/*    */   public static final int DATA_AVAILABLE = 1;
/*    */   public static final String cvsid = "$Id: MiniSeedPortEvent.java,v 1.5 2002/01/14 08:20:19 hwh Exp $";
/*    */   private GenericMiniSeedRecord gmsRec;
/*    */   
/*    */   public MiniSeedPortEvent(Object miniSeedPort, GenericMiniSeedRecord gmsRec)
/*    */   {
/* 25 */     super(miniSeedPort);
/* 26 */     this.gmsRec = gmsRec;
/*    */   }
/*    */   
/*    */   public GenericMiniSeedRecord getGenericMiniSeedRecord() {
/* 30 */     return this.gmsRec;
/*    */   }
/*    */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimestream/event/MiniSeedPortEvent.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */