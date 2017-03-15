/*     */ package cn.gd.seismology;
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
/*     */ 
/*     */ public final class ChannelLocator
/*     */   implements Comparable
/*     */ {
/*  54 */   private String netCode = "";
/*  55 */   private String stationCode = "";
/*  56 */   private String locID = "0";
/*  57 */   private String chanCode = "";
/*     */   
/*     */ 
/*     */   public static final String cvsid = "$Id: ChannelLocator.java,v 1.4 2002/01/14 08:20:44 hwh Exp $";
/*     */   
/*     */ 
/*     */   public ChannelLocator(String netCode, String StationCode, String locID, String chanCode)
/*     */   {
/*  65 */     setChannelLocator(netCode, StationCode, locID, chanCode);
/*     */   }
/*     */   
/*     */   public ChannelLocator copy() {
/*  69 */     ChannelLocator cl = new ChannelLocator();
/*  70 */     cl.netCode = new String(this.netCode);
/*  71 */     cl.stationCode = new String(this.stationCode);
/*  72 */     cl.locID = new String(this.locID);
/*  73 */     cl.chanCode = new String(this.chanCode);
/*  74 */     return cl;
/*     */   }
/*     */   
/*     */   public void setChannelLocator(String netCode, String StationCode, String locID, String chanCode)
/*     */   {
/*  79 */     this.netCode = netCode;
/*  80 */     this.stationCode = StationCode;
/*  81 */     this.locID = locID;
/*  82 */     this.chanCode = chanCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getNetCode()
/*     */   {
/*  89 */     return this.netCode;
/*     */   }
/*     */   
/*  92 */   public String getStationCode() { return this.stationCode; }
/*     */   
/*     */   public String getLocID()
/*     */   {
/*  96 */     return this.locID;
/*     */   }
/*     */   
/*  99 */   public String getChanCode() { return this.chanCode; }
/*     */   
/*     */   public int compareTo(Object o)
/*     */   {
/* 103 */     return compareTo((ChannelLocator)o);
/*     */   }
/*     */   
/*     */   public int compareTo(ChannelLocator cl) {
/* 107 */     return toString().compareTo(cl.toString());
/*     */   }
/*     */   
/*     */   public String toString() {
/* 111 */     return this.netCode + "/" + this.stationCode + "/" + this.locID + "/" + this.chanCode;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 115 */     return (this.netCode + this.stationCode + this.locID + this.chanCode).hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 119 */     if ((obj instanceof ChannelLocator)) {
/* 120 */       ChannelLocator obj1 = (ChannelLocator)obj;
/* 121 */       if ((this.netCode.equals(obj1.netCode)) && (this.stationCode.equals(obj1.stationCode)) && (this.locID.equals(obj1.locID)) && (this.chanCode.equals(obj1.chanCode)))
/*     */       {
/* 123 */         return true;
/*     */       }
/* 125 */       return false;
/*     */     }
/*     */     
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public ChannelLocator() {}
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/ChannelLocator.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */