/*     */ package cn.gd.seismology.liss.client;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
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
/*     */ public class BufferInfo
/*     */ {
/*  28 */   Hashtable hashtable = new Hashtable(400);
/*  29 */   Vector keyVec = new Vector(400);
/*     */   
/*     */ 
/*     */ 
/*     */   public Info getInfo(String seqNumber)
/*     */   {
/*  35 */     return (Info)this.hashtable.get(seqNumber);
/*     */   }
/*     */   
/*     */   public String[] getAllSeqNumber() {
/*  39 */     String[] seqs = new String[this.keyVec.size()];
/*  40 */     Enumeration enu = this.keyVec.elements();
/*  41 */     int i = 0;
/*  42 */     while (enu.hasMoreElements())
/*  43 */       seqs[(i++)] = ((String)enu.nextElement());
/*  44 */     return seqs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] diff(BufferInfo another)
/*     */   {
/*  52 */     Vector vec = new Vector();
/*  53 */     Vector anotherKeyVec = another.keyVec;
/*  54 */     Enumeration enu = this.keyVec.elements();
/*  55 */     while (enu.hasMoreElements()) {
/*  56 */       String key = (String)enu.nextElement();
/*  57 */       if (!anotherKeyVec.contains(key)) {
/*  58 */         vec.add(key);
/*     */       }
/*     */     }
/*  61 */     String[] seqs = new String[vec.size()];
/*  62 */     enu = vec.elements();
/*  63 */     int i = 0;
/*  64 */     while (enu.hasMoreElements())
/*  65 */       seqs[(i++)] = ((String)enu.nextElement());
/*  66 */     return seqs;
/*     */   }
/*     */   
/*     */   public void addElement(String infoLine)
/*     */   {
/*  71 */     StringTokenizer st = new StringTokenizer(infoLine, "\t");
/*  72 */     int count = st.countTokens();
/*  73 */     if (count != 5)
/*  74 */       return;
/*  75 */     String seqNum = st.nextToken();
/*  76 */     Info info = new Info();
/*  77 */     info.channelCode = st.nextToken();
/*  78 */     info.startTime = st.nextToken();
/*     */     try {
/*  80 */       info.samples = Integer.parseInt(st.nextToken());
/*  81 */       info.sampleRate = Double.parseDouble(st.nextToken());
/*     */     }
/*     */     catch (NumberFormatException nfEx) {
/*  84 */       System.err.println("parse infoLine error in BufferInfo.addElement, Ignore!");
/*     */       
/*  86 */       nfEx.printStackTrace();
/*     */     }
/*  88 */     this.keyVec.add(seqNum);
/*  89 */     this.hashtable.put(seqNum, info);
/*     */   }
/*     */   
/*     */   public void removeElement(int index) {
/*  93 */     if (index >= this.keyVec.size())
/*  94 */       return;
/*  95 */     Object key = this.keyVec.elementAt(index);
/*  96 */     this.hashtable.remove(key);
/*  97 */     this.keyVec.remove(index);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 101 */     StringBuffer strBuffer = new StringBuffer();
/* 102 */     Enumeration enu = this.keyVec.elements();
/* 103 */     while (enu.hasMoreElements()) {
/* 104 */       String seq = (String)enu.nextElement();
/* 105 */       strBuffer.append(seq);
/* 106 */       strBuffer.append('\t');
/* 107 */       Info info = (Info)this.hashtable.get(seq);
/* 108 */       strBuffer.append(info.toString());
/* 109 */       strBuffer.append('\n');
/*     */     }
/* 111 */     return strBuffer.toString();
/*     */   }
/*     */   
/*     */   public static class Info {
/*     */     public String channelCode;
/*     */     public double sampleRate;
/*     */     public int samples;
/*     */     public String startTime;
/*     */     
/*     */     public String toString() {
/* 121 */       return this.channelCode + "\t" + this.startTime + "\t" + this.samples + "\t" + this.sampleRate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/liss/client/BufferInfo.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */