/*     */ package cn.gd.seismology.realtimeplot.gui;
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
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowAdapter;
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
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ 
/*     */ public class WavePaint extends javax.swing.JPanel implements cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener
/*     */ {
/*  43 */   protected String stationCode = "";
/*  44 */   protected String channelCode = "";
/*  45 */   protected String locID = "";
/*  46 */   protected SynchronizedFixQueue fixQueue = new SynchronizedFixQueue(60, "realPlot");
/*     */   
/*     */ 
/*     */ 
/*  50 */   private transient int[] paintPixelX = new int['䀀'];
/*  51 */   private transient int[] paintPixelY = new int['䀀'];
/*     */   
/*  53 */   private transient float lastPointX = 0.0F;
/*  54 */   private transient float lastPointY = 0.0F;
/*     */   
/*  56 */   CoordinateTran Tran = new CoordinateTran();
/*  57 */   private float TZoom = 120.0F;
/*  58 */   private float YZoom = 8192.0F;
/*  59 */   int disChn = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   boolean isDragging = false;
/*  66 */   Point dragBegPoint = new Point();
/*  67 */   Point dragEndPoint = new Point();
/*  68 */   float yOffset = 0.0F;
/*     */   
/*  70 */   Dimension parentDim = new Dimension(640, 480);
/*  71 */   BorderLayout borderLayout1 = new BorderLayout();
/*     */   public static final String cvsid = "$Id: WavePaint.java,v 1.5 2002/01/14 08:20:49 hwh Exp $";
/*     */   transient int maxIndex;
/*     */   private static final int maxPixel = 16384;
/*     */   float maxY;
/*     */   transient int minIndex;
/*     */   float minY;
/*     */   private transient int nPoints;
/*     */   
/*     */   public WavePaint(String stnCode, String chnCode) {
/*  81 */     this.stationCode = stnCode;
/*  82 */     this.channelCode = chnCode;
/*     */     try {
/*  84 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/*  87 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public WavePaint() {
/*     */     try {
/*  93 */       jbInit();
/*     */     }
/*     */     catch (Exception ex) {
/*  96 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/* 100 */   public void setWaveData(String stnCode, String locid, String chnCode) { this.stationCode = stnCode;
/* 101 */     this.locID = locid;
/* 102 */     this.channelCode = chnCode;
/*     */   }
/*     */   
/*     */   private void jbInit() throws Exception {
/* 106 */     setBorder(BorderFactory.createEtchedBorder());
/* 107 */     setLayout(this.borderLayout1);
/* 108 */     addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
/*     */       public void mouseDragged(MouseEvent e) {
/* 110 */         WavePaint.this.this_mouseDragged(e);
/*     */       }
/* 112 */     });
/* 113 */     addMouseListener(new java.awt.event.MouseAdapter() {
/*     */       public void mousePressed(MouseEvent e) {
/* 115 */         WavePaint.this.this_mousePressed(e);
/*     */       }
/*     */       
/*     */       public void mouseReleased(MouseEvent e) {
/* 119 */         WavePaint.this.this_mouseReleased(e);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public Dimension getSize() {
/* 125 */     return getPreferredSize();
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/*     */     try {
/* 130 */       if (getParent().getSize().width != 0) {
/* 131 */         this.parentDim = getParent().getSize();
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {}
/* 135 */     int left = this.parentDim.height - this.parentDim.height / this.disChn * this.disChn;
/*     */     
/* 137 */     return new Dimension(this.parentDim.width, this.parentDim.height / this.disChn);
/*     */   }
/*     */   
/*     */ 
/*     */   void this_mousePressed(MouseEvent e)
/*     */   {
/* 143 */     if (e.isControlDown()) {
/* 144 */       this.YZoom /= 2.0F;
/*     */     }
/* 146 */     if (e.isShiftDown()) {
/* 147 */       this.YZoom *= 2.0F;
/*     */     }
/* 149 */     repaint();
/*     */   }
/*     */   
/*     */   void this_mouseDragged(MouseEvent e) {
/* 153 */     if (this.isDragging) {
/* 154 */       this.dragEndPoint = e.getPoint();
/*     */     } else {
/* 156 */       this.isDragging = true;
/* 157 */       this.dragBegPoint = e.getPoint();
/*     */     }
/*     */   }
/*     */   
/*     */   void this_mouseReleased(MouseEvent e) {
/* 162 */     if (this.isDragging) {
/* 163 */       this.isDragging = false;
/* 164 */       this.yOffset += (this.dragEndPoint.y - this.dragBegPoint.y) * this.Tran.getYScale();
/*     */       
/* 166 */       repaint();
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
/* 185 */     GenericMiniSeedRecord gmsRec = MiniSeedPortEvent.getGenericMiniSeedRecord();
/* 186 */     if (gmsRec == null)
/* 187 */       return;
/* 188 */     long ms = gmsRec.getStartTime().getTime() + (gmsRec.getNumSamples() / gmsRec.getSampleRate()) * 1000L;
/* 189 */     ms -= new Date().getTime();
/*     */     
/* 191 */     if (gmsRec.getStation().equals("ML03")) {
/* 192 */       System.out.println(gmsRec.getStation() + "/" + gmsRec.getSequenceNumber() + " Delay: " + ms / 1000L);
/*     */     }
/* 194 */     if ((this.stationCode.equals(gmsRec.getStation())) && (this.locID.equals(gmsRec.getLocID())) && (this.channelCode.equals(gmsRec.getChannel())))
/*     */     {
/*     */ 
/* 197 */       this.fixQueue.addBack(gmsRec);
/* 198 */       repaint();
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
/* 213 */     Vector vec = new Vector();
/* 214 */     vec.addElement(new Float(start));
/* 215 */     int i = 1;
/* 216 */     while (i * inteval < end) {
/* 217 */       vec.addElement(new Float(i++ * inteval));
/*     */     }
/* 219 */     vec.addElement(new Float(end));
/* 220 */     float[] step = new float[vec.size()];
/* 221 */     for (i = 0; i < step.length; i++) {
/* 222 */       Float aFloat = (Float)vec.elementAt(i);
/* 223 */       step[i] = aFloat.floatValue();
/*     */     }
/* 225 */     return step;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized void paintSeedBlk(GenericMiniSeedRecord gmsRec, Graphics g)
/*     */   {
/* 231 */     int[] smps = gmsRec.getUData();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 238 */     TimeStamp time = gmsRec.getStartTime();
/* 239 */     GregorianCalendar c = new GregorianCalendar();
/* 240 */     Date d = new Date(time.getTime());
/* 241 */     c.setTime(d);
/* 242 */     double leftSec = c.get(12) % 2 * 60 + c.get(13) + time.getNanoSecs() / 1.0E9D;
/*     */     
/* 244 */     Dimension dim = getSize();
/* 245 */     double xScale = gmsRec.getSampleRate() * this.TZoom / dim.width;
/* 246 */     double yScale = this.YZoom / dim.height;
/* 247 */     float Start = (float)(leftSec * gmsRec.getSampleRate());
/* 248 */     float End = Start + gmsRec.getNumSamples();
/* 249 */     int Count = gmsRec.getNumSamples();
/* 250 */     int Width = dim.width;
/* 251 */     Point Pre = new Point();
/* 252 */     Point Next = new Point();
/* 253 */     Point Temp = new Point();
/*     */     
/*     */ 
/*     */ 
/* 257 */     int X1 = 0;
/* 258 */     float[] se = calcStartEnd(Start, End, this.TZoom * (float)gmsRec.getSampleRate());
/* 259 */     for (int j = 0; j < se.length - 1; j++) {
/* 260 */       int pStart = (int)(se[j] / xScale) % Width;
/* 261 */       int pEnd = (int)(se[(j + 1)] / xScale) % Width;
/* 262 */       if (pStart > pEnd)
/* 263 */         pEnd += Width;
/* 264 */       Point aOPixel = new Point(pStart, dim.height / 2);
/* 265 */       this.Tran.setTran(aOPixel, 0.0F, this.yOffset, (float)xScale, (float)yScale);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 271 */       g.setColor(getBackground());
/*     */       
/* 273 */       g.fillRect(pStart, 1, pEnd - pStart, dim.height - 1);
/*     */       
/*     */ 
/* 276 */       g.fillRect(pEnd, 1, 20, dim.height - 1);
/*     */       
/* 278 */       g.setColor(Color.blue);
/*     */       
/* 280 */       X1 = 0;
/* 281 */       Next = this.Tran.toPixel(X1, smps[X1]);
/* 282 */       Temp = Next;
/* 283 */       int n = 0;
/* 284 */       Point lastPoint = this.Tran.toPixel(this.lastPointX, this.lastPointY);
/* 285 */       if ((!lastPoint.equals(new Point(0, 0))) && (lastPoint.x <= pStart)) {
/* 286 */         this.paintPixelX[(2 * n)] = lastPoint.x;
/* 287 */         this.paintPixelY[(2 * n)] = lastPoint.y;
/* 288 */         this.paintPixelX[(2 * n + 1)] = lastPoint.x;
/* 289 */         this.paintPixelY[(2 * n + 1)] = lastPoint.y;
/* 290 */         n++;
/*     */       }
/* 292 */       for (int x = pStart; x < pEnd; x++) {
/* 293 */         int X0 = X1;
/* 294 */         X1 = (int)this.Tran.toOrgX(new Point(x + 1, 0));
/* 295 */         if (X1 < 0)
/* 296 */           System.out.println("bug in paintSeedBlk: X1= " + X1 + "  x= " + x + "  " + this.Tran);
/* 297 */         if (X1 >= Count)
/*     */           break;
/* 299 */         findMinMax(smps, X0, X1);
/* 300 */         if (this.minIndex < this.maxIndex) {
/* 301 */           Pre = this.Tran.toPixel(this.minIndex, smps[this.minIndex]);
/* 302 */           Next = this.Tran.toPixel(this.maxIndex, smps[this.maxIndex]);
/*     */         } else {
/* 304 */           Pre = this.Tran.toPixel(this.maxIndex, smps[this.maxIndex]);
/* 305 */           Next = this.Tran.toPixel(this.minIndex, smps[this.minIndex]);
/*     */         }
/*     */         
/*     */ 
/* 309 */         this.paintPixelX[(2 * n)] = Temp.x;
/* 310 */         this.paintPixelY[(2 * n)] = Temp.y;
/* 311 */         this.paintPixelX[(2 * n + 1)] = Pre.x;
/* 312 */         this.paintPixelY[(2 * n + 1)] = Pre.y;
/* 313 */         n++;
/* 314 */         Temp = Next;
/*     */       }
/*     */       
/* 317 */       this.nPoints = (2 * n);
/* 318 */       g.drawPolyline(this.paintPixelX, this.paintPixelY, this.nPoints);
/*     */     }
/* 320 */     this.lastPointX = this.Tran.toOrgX(Pre);
/* 321 */     this.lastPointY = this.Tran.toOrgY(Pre);
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
/*     */   protected void paintComponent(Graphics g)
/*     */   {
/* 334 */     super.paintComponent(g);
/* 335 */     int size = this.fixQueue.getSize();
/* 336 */     GenericMiniSeedRecord gmsRec = null;
/* 337 */     for (int i = 0; i < size; i++) {
/* 338 */       gmsRec = (GenericMiniSeedRecord)this.fixQueue.at(i);
/* 339 */       paintSeedBlk(gmsRec, g);
/*     */     }
/* 341 */     if (gmsRec != null) {
/* 342 */       drawChnInfo(gmsRec, g);
/*     */     }
/*     */   }
/*     */   
/*     */   private void drawChnInfo(GenericMiniSeedRecord gmsRec, Graphics g) {
/* 347 */     SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
/* 348 */     long ms = gmsRec.getStartTime().getTime() + (gmsRec.getNumSamples() / gmsRec.getSampleRate() * 1000.0D);
/*     */     
/* 350 */     String Buffer = df.format(new Date(ms));
/*     */     
/* 352 */     g.setColor(Color.red);
/* 353 */     int fontHeight = getSize().height / 20;
/* 354 */     if (fontHeight < 12)
/* 355 */       fontHeight = 12;
/* 356 */     if (fontHeight > 24) {
/* 357 */       fontHeight = 24;
/*     */     }
/* 359 */     g.setFont(new Font("DialogInput", 0, fontHeight));
/* 360 */     String name = "[" + gmsRec.getNetwork() + "/" + gmsRec.getStation() + "/" + gmsRec.getLocID() + "/" + gmsRec.getChannel() + "]";
/*     */     
/* 362 */     Buffer = name + " at " + Buffer;
/*     */     
/* 364 */     FontMetrics fm = g.getFontMetrics();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 372 */     g.setColor(getBackground());
/* 373 */     g.fillRect(2, getSize().height - fm.getHeight(), fm.stringWidth(Buffer), fm.getHeight());
/*     */     
/* 375 */     g.setColor(Color.red);
/* 376 */     g.drawString(Buffer, 2, getSize().height - fm.getDescent());
/*     */     
/*     */ 
/* 379 */     String strYOffset = "" + (int)-this.yOffset;
/* 380 */     g.setColor(getBackground());
/* 381 */     g.fillRect(2, getSize().height / 2 - fm.getHeight() / 2, fm.stringWidth(strYOffset), fm.getHeight());
/* 382 */     g.setColor(Color.black);
/* 383 */     g.drawString(strYOffset, 2, getSize().height / 2 + fm.getHeight() / 2 - fm.getDescent());
/*     */     
/*     */ 
/* 386 */     String strYZoom = "" + (int)this.YZoom;
/* 387 */     g.setColor(getBackground());
/* 388 */     g.fillRect(2, 0, fm.stringWidth(strYZoom), fm.getHeight());
/* 389 */     g.setColor(Color.black);
/* 390 */     g.drawString(strYZoom, 2, fm.getHeight() - fm.getDescent());
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxCount(int[] smps, int Beg, int End)
/*     */   {
/* 396 */     int imax = Math.abs(smps[Beg]);
/* 397 */     for (int i = Beg + 1; i < End; i++)
/* 398 */       if (imax < Math.abs(smps[i]))
/* 399 */         imax = Math.abs(smps[i]);
/* 400 */     return imax;
/*     */   }
/*     */   
/*     */   public int getPPCount(int[] smps, int Beg, int End) {
/* 404 */     findMinMax(smps, Beg, End);
/* 405 */     return Math.abs(smps[this.maxIndex] - smps[this.minIndex]);
/*     */   }
/*     */   
/*     */   void findMinMax(int[] smps, int Beg, int End) {
/* 409 */     this.minIndex = Beg;
/* 410 */     this.maxIndex = Beg;
/* 411 */     for (int i = Beg + 1; i < End; i++) {
/* 412 */       if (smps[this.minIndex] <= smps[i]) {
/* 413 */         if (smps[this.maxIndex] <= smps[i]) {
/* 414 */           this.maxIndex = i;
/*     */         }
/*     */       }
/*     */       else {
/* 418 */         this.minIndex = i;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTZoom(float newTZoom)
/*     */   {
/* 425 */     this.TZoom = newTZoom;
/*     */   }
/*     */   
/*     */   public float getTZoom()
/*     */   {
/* 430 */     return this.TZoom;
/*     */   }
/*     */   
/*     */   public void setYZoom(float newYZoom) {
/* 434 */     this.YZoom = newYZoom;
/*     */   }
/*     */   
/*     */ 
/*     */   public float getYZoom()
/*     */   {
/* 440 */     return this.YZoom;
/*     */   }
/*     */   
/*     */   public void setDisChn(int newDisChn)
/*     */   {
/* 445 */     this.disChn = newDisChn;
/*     */   }
/*     */   
/*     */   public int getDisChn() {
/* 449 */     return this.disChn;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws java.io.IOException
/*     */   {
/* 454 */     SimpleJFrame frame = new SimpleJFrame("IPPlot");
/* 455 */     frame.addWindowListener(new WindowAdapter() {
/* 456 */       public void windowClosing(WindowEvent e) { System.exit(0); }
/* 457 */     });
/* 458 */     boolean bTCP = true;
/* 459 */     Properties props = new Properties();
/* 460 */     props.load(new FileInputStream("IPPlot.properties"));
/*     */     
/* 462 */     String connectionType = props.getProperty("IPPlot.connection.type");
/* 463 */     if (connectionType == null) {
/* 464 */       bTCP = true;
/*     */     } else {
/* 466 */       if (connectionType.equalsIgnoreCase("TCP"))
/* 467 */         bTCP = true;
/* 468 */       if (connectionType.equalsIgnoreCase("UDP"))
/* 469 */         bTCP = false;
/*     */     }
/* 471 */     String host = props.getProperty("IPPlot.connection.host");
/* 472 */     int port = Integer.parseInt(props.getProperty("IPPlot.connection.port"));
/* 473 */     String user = props.getProperty("IPPlot.connection.user");
/* 474 */     String password = props.getProperty("IPPlot.connection.password");
/*     */     
/* 476 */     String line = props.getProperty("IPPlot.channel");
/* 477 */     StringTokenizer st = new StringTokenizer(line, "+");
/*     */     
/* 479 */     String[] netID = new String[st.countTokens()];
/* 480 */     String[] stnCode = new String[st.countTokens()];
/* 481 */     String[] locID = new String[st.countTokens()];
/* 482 */     String[] chanCode = new String[st.countTokens()];
/* 483 */     int j = 0;
/* 484 */     while (st.hasMoreTokens()) {
/* 485 */       StringTokenizer st0 = new StringTokenizer(st.nextToken(), " /");
/* 486 */       netID[j] = st0.nextToken();
/* 487 */       stnCode[j] = st0.nextToken();
/* 488 */       locID[j] = st0.nextToken();
/* 489 */       chanCode[(j++)] = st0.nextToken();
/*     */     }
/* 491 */     frame.setStationInfo(netID, stnCode, locID, chanCode);
/*     */     
/* 493 */     WavePaint[] wavePaintZ = new WavePaint[stnCode.length];
/* 494 */     for (int i = 0; i < wavePaintZ.length; i++) {
/* 495 */       wavePaintZ[i] = new WavePaint();
/* 496 */       wavePaintZ[i].setWaveData(stnCode[i], locID[i], chanCode[i]);
/* 497 */       wavePaintZ[i].setDisChn(wavePaintZ.length);
/*     */     }
/*     */     
/* 500 */     Box box = new Box(1);
/* 501 */     for (int i = 0; i < wavePaintZ.length; i++)
/* 502 */       box.add(wavePaintZ[i]);
/* 503 */     frame.getContentPane().add("Center", box);
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
/* 514 */     frame.setSize(1024, 768);
/*     */     
/* 516 */     frame.setVisible(true);
/*     */     
/* 518 */     if (bTCP) {
/* 519 */       CLissRTPort miniSeedPort = new CLissRTPort(host, port, user, password, stnCode);
/*     */       
/*     */ 
/* 522 */       miniSeedPort.addMiniSeedPortEventListener(frame);
/* 523 */       for (int i = 0; i < wavePaintZ.length; i++) {
/* 524 */         miniSeedPort.addMiniSeedPortEventListener(wavePaintZ[i]);
/*     */       }
/*     */     }
/*     */     else {
/* 528 */       UDPPort udpPort = new UDPPort(host, port, stnCode);
/*     */       
/*     */ 
/* 531 */       udpPort.addMiniSeedPortEventListener(frame);
/* 532 */       for (int i = 0; i < wavePaintZ.length; i++) {
/* 533 */         udpPort.addMiniSeedPortEventListener(wavePaintZ[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimeplot/gui/WavePaint.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */