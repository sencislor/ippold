/*     */ package cn.gd.seismology.realtimeplot.awt;
/*     */ 
/*     */ import cn.gd.convert.CoordinateTran;
/*     */ import cn.gd.seismology.realtimestream.CLissRTPort;
/*     */ import cn.gd.seismology.realtimestream.MiniSeedPort;
/*     */ import cn.gd.seismology.realtimestream.UDPPort;
/*     */ import cn.gd.seismology.realtimestream.event.MiniSeedPortEvent;
/*     */ import cn.gd.util.SynchronizedFixQueue;
/*     */ import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;
/*     */ import edu.iris.timeutils.TimeStamp;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Point;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class WavePaint extends Panel implements cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener
/*     */ {
/*  39 */   protected String stationCode = "";
/*  40 */   protected String channelCode = "";
/*  41 */   protected String locID = "";
/*  42 */   protected SynchronizedFixQueue fixQueue = new SynchronizedFixQueue(60, "realPlot");
/*     */   
/*     */ 
/*     */ 
/*  46 */   private transient int[] paintPixelX = new int['䀀'];
/*  47 */   private transient int[] paintPixelY = new int['䀀'];
/*     */   
/*  49 */   private transient float lastPointX = 0.0F;
/*  50 */   private transient float lastPointY = 0.0F;
/*     */   
/*  52 */   CoordinateTran Tran = new CoordinateTran();
/*  53 */   private float TZoom = 120.0F;
/*  54 */   private float YZoom = 8192.0F;
/*  55 */   int disChn = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   boolean isDragging = false;
/*  62 */   Point dragBegPoint = new Point();
/*  63 */   Point dragEndPoint = new Point();
/*  64 */   float yOffset = 0.0F;
/*     */   
/*  66 */   Dimension parentDim = new Dimension(640, 480);
/*  67 */   BorderLayout borderLayout1 = new BorderLayout();
/*     */   public static final String cvsid = "$Id: WavePaint.java,v 1.5 2002/01/14 08:20:49 hwh Exp $";
/*     */   transient int maxIndex;
/*     */   private static final int maxPixel = 16384;
/*     */   float maxY;
/*     */   transient int minIndex;
/*     */   float minY;
/*     */   private transient int nPoints;
/*     */   
/*     */   public WavePaint(String stnCode, String chnCode) {
/*  77 */     this.stationCode = stnCode;
/*  78 */     this.channelCode = chnCode;
/*     */     try {
/*  80 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/*  83 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public WavePaint() {
/*     */     try {
/*  89 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/*  92 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*  96 */   public void setWaveData(String stnCode, String locid, String chnCode) { this.stationCode = stnCode;
/*  97 */     this.locID = locid;
/*  98 */     this.channelCode = chnCode;
/*     */   }
/*     */   
/*     */   private void jbInit() throws Exception {
/* 102 */     setLayout(this.borderLayout1);
/* 103 */     addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
/*     */       public void mouseDragged(MouseEvent e) {
/* 105 */         WavePaint.this.this_mouseDragged(e);
/*     */       }
/* 107 */     });
/* 108 */     addMouseListener(new java.awt.event.MouseAdapter() {
/*     */       public void mousePressed(MouseEvent e) {
/* 110 */         WavePaint.this.this_mousePressed(e);
/*     */       }
/*     */       
/*     */       public void mouseReleased(MouseEvent e) {
/* 114 */         WavePaint.this.this_mouseReleased(e);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public Dimension getSize() {
/* 120 */     return getPreferredSize();
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/*     */     try {
/* 125 */       if (getParent().getSize().width != 0) {
/* 126 */         this.parentDim = getParent().getSize();
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {}
/* 130 */     int left = this.parentDim.height - this.parentDim.height / this.disChn * this.disChn;
/*     */     
/* 132 */     return new Dimension(this.parentDim.width, this.parentDim.height / this.disChn);
/*     */   }
/*     */   
/*     */ 
/*     */   void this_mousePressed(MouseEvent e)
/*     */   {
/* 138 */     if (e.isControlDown()) {
/* 139 */       this.YZoom /= 2.0F;
/*     */     }
/* 141 */     if (e.isShiftDown()) {
/* 142 */       this.YZoom *= 2.0F;
/*     */     }
/* 144 */     repaint();
/*     */   }
/*     */   
/*     */   void this_mouseDragged(MouseEvent e) {
/* 148 */     if (this.isDragging) {
/* 149 */       this.dragEndPoint = e.getPoint();
/*     */     } else {
/* 151 */       this.isDragging = true;
/* 152 */       this.dragBegPoint = e.getPoint();
/*     */     }
/*     */   }
/*     */   
/*     */   void this_mouseReleased(MouseEvent e) {
/* 157 */     if (this.isDragging) {
/* 158 */       this.isDragging = false;
/* 159 */       this.yOffset += (this.dragEndPoint.y - this.dragBegPoint.y) * this.Tran.getYScale();
/*     */       
/* 161 */       repaint();
/*     */     }
/*     */   }
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
/*     */   public void miniSeedEvent(MiniSeedPortEvent MiniSeedPortEvent)
/*     */   {
/* 180 */     GenericMiniSeedRecord gmsRec = MiniSeedPortEvent.getGenericMiniSeedRecord();
/* 181 */     if (gmsRec == null)
/* 182 */       return;
/* 183 */     long ms = gmsRec.getStartTime().getTime() + (gmsRec.getNumSamples() / gmsRec.getSampleRate()) * 1000L;
/* 184 */     ms -= new Date().getTime();
/*     */     
/* 186 */     if (gmsRec.getStation().equals("ML03")) {
/* 187 */       System.out.println(gmsRec.getStation() + "/" + gmsRec.getSequenceNumber() + " Delay: " + ms / 1000L);
/*     */     }
/* 189 */     if ((this.stationCode.equals(gmsRec.getStation())) && (this.locID.equals(gmsRec.getLocID())) && (this.channelCode.equals(gmsRec.getChannel())))
/*     */     {
/*     */ 
/* 192 */       this.fixQueue.addBack(gmsRec);
/* 193 */       repaint();
/*     */     }
/*     */   }
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
/*     */   private float[] calcStartEnd(float start, float end, float inteval)
/*     */   {
/* 208 */     Vector vec = new Vector();
/* 209 */     vec.addElement(new Float(start));
/* 210 */     int i = 1;
/* 211 */     while (i * inteval < end) {
/* 212 */       vec.addElement(new Float(i++ * inteval));
/*     */     }
/* 214 */     vec.addElement(new Float(end));
/* 215 */     float[] step = new float[vec.size()];
/* 216 */     for (i = 0; i < step.length; i++) {
/* 217 */       Float aFloat = (Float)vec.elementAt(i);
/* 218 */       step[i] = aFloat.floatValue();
/*     */     }
/* 220 */     return step;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void paintSeedBlk(GenericMiniSeedRecord gmsRec, Graphics g)
/*     */   {
/* 226 */     int[] smps = gmsRec.getUData();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 233 */     TimeStamp time = gmsRec.getStartTime();
/* 234 */     GregorianCalendar c = new GregorianCalendar();
/* 235 */     Date d = new Date(time.getTime());
/* 236 */     c.setTime(d);
/* 237 */     double leftSec = c.get(12) % 2 * 60 + c.get(13) + time.getNanoSecs() / 1.0E9D;
/*     */     
/* 239 */     Dimension dim = getSize();
/* 240 */     double xScale = gmsRec.getSampleRate() * this.TZoom / dim.width;
/* 241 */     double yScale = this.YZoom / dim.height;
/* 242 */     float Start = (float)(leftSec * gmsRec.getSampleRate());
/* 243 */     float End = Start + gmsRec.getNumSamples();
/* 244 */     int Count = gmsRec.getNumSamples();
/* 245 */     int Width = dim.width;
/* 246 */     Point Pre = new Point();
/* 247 */     Point Next = new Point();
/* 248 */     Point Temp = new Point();
/*     */     
/*     */ 
/*     */ 
/* 252 */     int X1 = 0;
/* 253 */     float[] se = calcStartEnd(Start, End, this.TZoom * (float)gmsRec.getSampleRate());
/* 254 */     for (int j = 0; j < se.length - 1; j++) {
/* 255 */       int pStart = (int)(se[j] / xScale) % Width;
/* 256 */       int pEnd = (int)(se[(j + 1)] / xScale) % Width;
/* 257 */       if (pStart > pEnd)
/* 258 */         pEnd += Width;
/* 259 */       Point aOPixel = new Point(pStart, dim.height / 2);
/* 260 */       this.Tran.setTran(aOPixel, 0.0F, this.yOffset, (float)xScale, (float)yScale);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 266 */       g.setColor(getBackground());
/*     */       
/* 268 */       g.fillRect(pStart, 1, pEnd - pStart, dim.height - 1);
/*     */       
/*     */ 
/* 271 */       g.fillRect(pEnd, 1, 20, dim.height - 1);
/*     */       
/* 273 */       g.setColor(Color.blue);
/*     */       
/* 275 */       X1 = 0;
/* 276 */       Next = this.Tran.toPixel(X1, smps[X1]);
/* 277 */       Temp = Next;
/* 278 */       int n = 0;
/* 279 */       Point lastPoint = this.Tran.toPixel(this.lastPointX, this.lastPointY);
/* 280 */       if ((!lastPoint.equals(new Point(0, 0))) && (lastPoint.x <= pStart)) {
/* 281 */         this.paintPixelX[(2 * n)] = lastPoint.x;
/* 282 */         this.paintPixelY[(2 * n)] = lastPoint.y;
/* 283 */         this.paintPixelX[(2 * n + 1)] = lastPoint.x;
/* 284 */         this.paintPixelY[(2 * n + 1)] = lastPoint.y;
/* 285 */         n++;
/*     */       }
/* 287 */       for (int x = pStart; x < pEnd; x++) {
/* 288 */         int X0 = X1;
/* 289 */         X1 = (int)this.Tran.toOrgX(new Point(x + 1, 0));
/* 290 */         if (X1 < 0)
/* 291 */           System.out.println("bug in paintSeedBlk: X1= " + X1 + "  x= " + x + "  " + this.Tran);
/* 292 */         if (X1 >= Count)
/*     */           break;
/* 294 */         findMinMax(smps, X0, X1);
/* 295 */         if (this.minIndex < this.maxIndex) {
/* 296 */           Pre = this.Tran.toPixel(this.minIndex, smps[this.minIndex]);
/* 297 */           Next = this.Tran.toPixel(this.maxIndex, smps[this.maxIndex]);
/*     */         } else {
/* 299 */           Pre = this.Tran.toPixel(this.maxIndex, smps[this.maxIndex]);
/* 300 */           Next = this.Tran.toPixel(this.minIndex, smps[this.minIndex]);
/*     */         }
/*     */         
/*     */ 
/* 304 */         this.paintPixelX[(2 * n)] = Temp.x;
/* 305 */         this.paintPixelY[(2 * n)] = Temp.y;
/* 306 */         this.paintPixelX[(2 * n + 1)] = Pre.x;
/* 307 */         this.paintPixelY[(2 * n + 1)] = Pre.y;
/* 308 */         n++;
/* 309 */         Temp = Next;
/*     */       }
/*     */       
/* 312 */       this.nPoints = (2 * n);
/* 313 */       g.drawPolyline(this.paintPixelX, this.paintPixelY, this.nPoints);
/*     */     }
/* 315 */     this.lastPointX = this.Tran.toOrgX(Pre);
/* 316 */     this.lastPointY = this.Tran.toOrgY(Pre);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void paint(Graphics g)
/*     */   {
/* 329 */     super.paint(g);
/* 330 */     int size = this.fixQueue.getSize();
/* 331 */     GenericMiniSeedRecord gmsRec = null;
/* 332 */     for (int i = 0; i < size; i++) {
/* 333 */       gmsRec = (GenericMiniSeedRecord)this.fixQueue.at(i);
/* 334 */       paintSeedBlk(gmsRec, g);
/*     */     }
/* 336 */     if (gmsRec != null) {
/* 337 */       drawChnInfo(gmsRec, g);
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawChnInfo(GenericMiniSeedRecord gmsRec, Graphics g) {
/* 342 */     SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
/* 343 */     long ms = gmsRec.getStartTime().getTime() + (gmsRec.getNumSamples() / gmsRec.getSampleRate() * 1000.0D);
/*     */     
/* 345 */     String Buffer = df.format(new Date(ms));
/*     */     
/* 347 */     g.setColor(Color.red);
/* 348 */     int fontHeight = getSize().height / 20;
/* 349 */     if (fontHeight < 12)
/* 350 */       fontHeight = 12;
/* 351 */     if (fontHeight > 24) {
/* 352 */       fontHeight = 24;
/*     */     }
/* 354 */     g.setFont(new java.awt.Font("DialogInput", 0, fontHeight));
/* 355 */     String name = "[" + gmsRec.getNetwork() + "/" + gmsRec.getStation() + "/" + gmsRec.getLocID() + "/" + gmsRec.getChannel() + "]";
/*     */     
/* 357 */     Buffer = name + " at " + Buffer;
/*     */     
/* 359 */     FontMetrics fm = g.getFontMetrics();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 367 */     g.setColor(getBackground());
/* 368 */     g.fillRect(2, getSize().height - fm.getHeight(), fm.stringWidth(Buffer), fm.getHeight());
/*     */     
/* 370 */     g.setColor(Color.red);
/* 371 */     g.drawString(Buffer, 2, getSize().height - fm.getDescent());
/*     */     
/*     */ 
/* 374 */     String strYOffset = "" + (int)-this.yOffset;
/* 375 */     g.setColor(getBackground());
/* 376 */     g.fillRect(2, getSize().height / 2 - fm.getHeight() / 2, fm.stringWidth(strYOffset), fm.getHeight());
/* 377 */     g.setColor(Color.black);
/* 378 */     g.drawString(strYOffset, 2, getSize().height / 2 + fm.getHeight() / 2 - fm.getDescent());
/*     */     
/*     */ 
/* 381 */     String strYZoom = "" + (int)this.YZoom;
/* 382 */     g.setColor(getBackground());
/* 383 */     g.fillRect(2, 0, fm.stringWidth(strYZoom), fm.getHeight());
/* 384 */     g.setColor(Color.black);
/* 385 */     g.drawString(strYZoom, 2, fm.getHeight() - fm.getDescent());
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxCount(int[] smps, int Beg, int End)
/*     */   {
/* 391 */     int imax = Math.abs(smps[Beg]);
/* 392 */     for (int i = Beg + 1; i < End; i++)
/* 393 */       if (imax < Math.abs(smps[i]))
/* 394 */         imax = Math.abs(smps[i]);
/* 395 */     return imax;
/*     */   }
/*     */   
/*     */   public int getPPCount(int[] smps, int Beg, int End) {
/* 399 */     findMinMax(smps, Beg, End);
/* 400 */     return Math.abs(smps[this.maxIndex] - smps[this.minIndex]);
/*     */   }
/*     */   
/*     */   void findMinMax(int[] smps, int Beg, int End) {
/* 404 */     this.minIndex = Beg;
/* 405 */     this.maxIndex = Beg;
/* 406 */     for (int i = Beg + 1; i < End; i++) {
/* 407 */       if (smps[this.minIndex] <= smps[i]) {
/* 408 */         if (smps[this.maxIndex] <= smps[i]) {
/* 409 */           this.maxIndex = i;
/*     */         }
/*     */       }
/*     */       else {
/* 413 */         this.minIndex = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTZoom(float newTZoom)
/*     */   {
/* 420 */     this.TZoom = newTZoom;
/*     */   }
/*     */   
/*     */   public float getTZoom()
/*     */   {
/* 425 */     return this.TZoom;
/*     */   }
/*     */   
/*     */   public void setYZoom(float newYZoom) {
/* 429 */     this.YZoom = newYZoom;
/*     */   }
/*     */   
/*     */ 
/*     */   public float getYZoom()
/*     */   {
/* 435 */     return this.YZoom;
/*     */   }
/*     */   
/*     */   public void setDisChn(int newDisChn)
/*     */   {
/* 440 */     this.disChn = newDisChn;
/*     */   }
/*     */   
/*     */   public int getDisChn() {
/* 444 */     return this.disChn;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws java.io.IOException
/*     */   {
/* 449 */     SimpleFrame frame = new SimpleFrame("IPPlot");
/* 450 */     frame.addWindowListener(new java.awt.event.WindowAdapter() {
/* 451 */       public void windowClosing(WindowEvent e) { System.exit(0);
/*     */       }
/* 453 */     });
/* 454 */     boolean bTCP = true;
/* 455 */     Properties props = new Properties();
/* 456 */     props.load(new FileInputStream("IPPlot.properties"));
/*     */     
/* 458 */     String connectionType = props.getProperty("IPPlot.connection.type");
/* 459 */     if (connectionType == null) {
/* 460 */       bTCP = true;
/*     */     } else {
/* 462 */       if (connectionType.equalsIgnoreCase("TCP"))
/* 463 */         bTCP = true;
/* 464 */       if (connectionType.equalsIgnoreCase("UDP"))
/* 465 */         bTCP = false;
/*     */     }
/* 467 */     String host = props.getProperty("IPPlot.connection.host");
/* 468 */     int port = Integer.parseInt(props.getProperty("IPPlot.connection.port"));
/* 469 */     String user = props.getProperty("IPPlot.connection.user");
/* 470 */     String password = props.getProperty("IPPlot.connection.password");
/*     */     
/* 472 */     String line = props.getProperty("IPPlot.channel");
/* 473 */     StringTokenizer st = new StringTokenizer(line, "+");
/*     */     
/* 475 */     String[] netID = new String[st.countTokens()];
/* 476 */     String[] stnCode = new String[st.countTokens()];
/* 477 */     String[] locID = new String[st.countTokens()];
/* 478 */     String[] chanCode = new String[st.countTokens()];
/* 479 */     int j = 0;
/* 480 */     while (st.hasMoreTokens()) {
/* 481 */       StringTokenizer st0 = new StringTokenizer(st.nextToken(), " /");
/* 482 */       netID[j] = st0.nextToken();
/* 483 */       stnCode[j] = st0.nextToken();
/* 484 */       locID[j] = st0.nextToken();
/* 485 */       chanCode[(j++)] = st0.nextToken();
/*     */     }
/* 487 */     frame.setStationInfo(netID, stnCode, locID, chanCode);
/*     */     
/* 489 */     WavePaint[] wavePaintZ = new WavePaint[stnCode.length];
/* 490 */     for (int i = 0; i < wavePaintZ.length; i++) {
/* 491 */       wavePaintZ[i] = new WavePaint();
/* 492 */       wavePaintZ[i].setWaveData(stnCode[i], locID[i], chanCode[i]);
/* 493 */       wavePaintZ[i].setDisChn(wavePaintZ.length);
/*     */     }
/*     */     
/* 496 */     Panel box = new Panel();
/* 497 */     GridLayout gridLayout1 = new GridLayout();
/* 498 */     box.setLayout(gridLayout1);
/* 499 */     gridLayout1.setColumns(1);
/* 500 */     gridLayout1.setHgap(0);
/* 501 */     gridLayout1.setRows(0);
/* 502 */     gridLayout1.setVgap(0);
/*     */     
/* 504 */     for (int i = 0; i < wavePaintZ.length; i++)
/* 505 */       box.add(wavePaintZ[i]);
/* 506 */     frame.add(box, "Center");
/* 507 */     frame.setSize(300, 200);
/*     */     
/* 509 */     frame.setVisible(true);
/*     */     
/* 511 */     if (bTCP) {
/* 512 */       CLissRTPort miniSeedPort = new CLissRTPort(host, port, user, password, stnCode);
/*     */       
/*     */ 
/* 515 */       miniSeedPort.addMiniSeedPortEventListener(frame);
/* 516 */       for (int i = 0; i < wavePaintZ.length; i++) {
/* 517 */         miniSeedPort.addMiniSeedPortEventListener(wavePaintZ[i]);
/*     */       }
/*     */     }
/*     */     else {
/* 521 */       UDPPort udpPort = new UDPPort(host, port, stnCode);
/*     */       
/*     */ 
/* 524 */       udpPort.addMiniSeedPortEventListener(frame);
/* 525 */       for (int i = 0; i < wavePaintZ.length; i++) {
/* 526 */         udpPort.addMiniSeedPortEventListener(wavePaintZ[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimeplot/awt/WavePaint.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */