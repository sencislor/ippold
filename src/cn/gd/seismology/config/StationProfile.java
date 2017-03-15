/*     */ package cn.gd.seismology.config;
/*     */ 
/*     */ import cn.gd.seismology.ChannelLocator;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StationProfile
/*     */ {
/*     */   double calt;
/*     */   double clat;
/*     */   double clon;
/*     */   public static final String cvsid = "$Id: StationProfile.java,v 1.17 2004/01/10 02:28:03 hwh Exp $";
/*     */   String discrip;
/*     */   String networkID;
/*     */   Vector stationList;
/*     */   Vector stations;
/*     */   String version;
/*     */   
/*     */   public StationProfile()
/*     */   {
/*  46 */     this.networkID = "";
/*  47 */     this.discrip = "";
/*  48 */     this.version = "1.0";
/*     */     
/*     */ 
/*     */ 
/*  52 */     this.calt = 0.0D;
/*     */     
/*  54 */     this.stationList = new Vector();
/*  55 */     this.stations = new Vector(10);
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  59 */     try { StationProfile sprofile = new StationProfile();
/*  60 */       sprofile.parseNetwork(new FileInputStream("/tmp/network.xml"));
/*  61 */       System.err.println("Clat: " + sprofile.getClat());
/*  62 */       System.err.println("Clon: " + sprofile.getClon());
/*  63 */       System.err.println("Calt: " + sprofile.getCalt());
/*  64 */       sprofile.writeXML("/tmp/network_doc.xml");
/*     */     }
/*     */     catch (IOException ioEx) {
/*  67 */       ioEx.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeXML(String xmlFile)
/*     */     throws IOException
/*     */   {
/*  77 */     Document doc = buidDocument();
/*     */     
/*  79 */     TransformerFactory tFactory = TransformerFactory.newInstance();
/*     */     try
/*     */     {
/*  82 */       FileOutputStream fos = new FileOutputStream(xmlFile);
/*  83 */       DOMSource source = new DOMSource(doc);
/*  84 */       StreamResult result = new StreamResult(fos);
/*  85 */       Transformer transformer = tFactory.newTransformer();
/*  86 */       transformer.transform(source, result);
/*     */     }
/*     */     catch (TransformerConfigurationException tce)
/*     */     {
/*  90 */       System.out.println("\n** Transformer Factory error");
/*  91 */       System.out.println("   " + tce.getMessage());
/*     */     }
/*     */     catch (TransformerException tranFunEx)
/*     */     {
/*  95 */       System.out.println("\n** Transformation error");
/*  96 */       System.out.println("   " + tranFunEx.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */   Document buidDocument() {
/* 101 */     Document doc = null;
/* 102 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */     
/*     */     try
/*     */     {
/* 106 */       DocumentBuilder builder = factory.newDocumentBuilder();
/* 107 */       doc = builder.newDocument();
/*     */     }
/*     */     catch (ParserConfigurationException pcEx) {
/* 110 */       pcEx.printStackTrace();
/*     */     }
/*     */     
/* 113 */     Element root = doc.createElement("NETWORK");
/* 114 */     root.setAttribute("Name", this.networkID);
/* 115 */     root.setAttribute("Version", this.version);
/* 116 */     root.setAttribute("CLat", "" + this.clat);
/* 117 */     root.setAttribute("CLon", "" + this.clon);
/* 118 */     root.setAttribute("Discrip", this.discrip);
/* 119 */     root.appendChild(doc.createTextNode("\n"));
/*     */     
/* 121 */     doc.appendChild(root);
/*     */     
/* 123 */     int size = this.stations.size();
/* 124 */     for (int i = 0; i < size; i++) {
/* 125 */       Station sttn = (Station)this.stations.get(i);
/* 126 */       Element station = doc.createElement("STATION");
/* 127 */       root.appendChild(station);
/* 128 */       root.appendChild(doc.createTextNode("\n"));
/*     */       
/* 130 */       station.setAttribute("Net", sttn.getNet());
/* 131 */       station.setAttribute("Sta", sttn.getSta());
/* 132 */       station.setAttribute("Cname", sttn.getCname());
/* 133 */       station.setAttribute("Sname", sttn.getSname());
/* 134 */       station.setAttribute("StaType", sttn.getStaType());
/* 135 */       station.setAttribute("Lat", "" + sttn.getLat());
/* 136 */       station.setAttribute("Lon", "" + sttn.getLon());
/* 137 */       station.setAttribute("Coordsys", "" + sttn.getCoordsys());
/* 138 */       station.setAttribute("Elev", "" + sttn.getElev());
/* 139 */       station.setAttribute("MType", "" + sttn.getMtype());
/* 140 */       station.setAttribute("TransmitType", "" + sttn.getTransmitType());
/* 141 */       station.setAttribute("Ondate", "" + sttn.getOndate());
/* 142 */       station.setAttribute("Offdate", "" + sttn.getOffdate());
/* 143 */       station.setAttribute("Comment", "" + sttn.getComment());
/*     */       
/* 145 */       station.appendChild(doc.createTextNode("\n"));
/* 146 */       for (int chn = 0; chn < sttn.chans.size(); chn++) {
/* 147 */         Channel chnnl = (Channel)sttn.chans.get(chn);
/*     */         
/* 149 */         Element channel = doc.createElement("CHANNEL");
/* 150 */         station.appendChild(channel);
/* 151 */         station.appendChild(doc.createTextNode("\n"));
/*     */         
/* 153 */         channel.setAttribute("Cha", chnnl.getCha());
/* 154 */         channel.setAttribute("LocID", chnnl.getLocID());
/* 155 */         channel.setAttribute("Hang", "" + chnnl.getHang());
/* 156 */         channel.setAttribute("Vang", "" + chnnl.getVang());
/* 157 */         channel.setAttribute("SampleRate", "" + chnnl.getSampleRate());
/*     */         
/* 159 */         channel.appendChild(doc.createTextNode("\n"));
/* 160 */         Element response = doc.createElement("RESPONSE");
/* 161 */         channel.appendChild(response);
/* 162 */         channel.appendChild(doc.createTextNode("\n"));
/*     */         
/* 164 */         response.appendChild(doc.createTextNode("\n"));
/* 165 */         Element paz = doc.createElement("PAZ");
/* 166 */         response.appendChild(paz);
/* 167 */         response.appendChild(doc.createTextNode("\n"));
/*     */         
/* 169 */         paz.setAttribute("Snum", "" + chnnl.tranFun.getSnum());
/* 170 */         paz.setAttribute("Iunits", "" + chnnl.tranFun.getIunits());
/* 171 */         paz.setAttribute("Ounits", "" + chnnl.tranFun.getOunits());
/* 172 */         paz.setAttribute("Sfactor", "" + chnnl.tranFun.getSfactor());
/* 173 */         paz.setAttribute("Sensitivity", "" + chnnl.tranFun.getSensitivity());
/* 174 */         paz.setAttribute("Calper", "" + chnnl.tranFun.getCalper());
/* 175 */         paz.setAttribute("Deci", "" + chnnl.tranFun.getDeci());
/* 176 */         paz.setAttribute("Corr", "" + chnnl.tranFun.getCorr());
/* 177 */         paz.setAttribute("Nzero", "" + chnnl.tranFun.getNzero());
/* 178 */         paz.setAttribute("Npole", "" + chnnl.tranFun.getNpole());
/* 179 */         paz.setAttribute("Descrip", "" + chnnl.tranFun.getDescrip());
/*     */         
/* 181 */         paz.appendChild(doc.createTextNode("\n"));
/* 182 */         Element zero = doc.createElement("ZERO");
/* 183 */         paz.appendChild(zero);
/* 184 */         paz.appendChild(doc.createTextNode("\n"));
/*     */         
/* 186 */         zero.appendChild(doc.createTextNode(Channel.access$100(chnnl).zero));
/*     */         
/* 188 */         Element pole = doc.createElement("POLE");
/* 189 */         paz.appendChild(pole);
/* 190 */         paz.appendChild(doc.createTextNode("\n"));
/*     */         
/* 192 */         pole.appendChild(doc.createTextNode(Channel.access$100(chnnl).pole));
/*     */       }
/*     */     }
/*     */     
/* 196 */     return doc;
/*     */   }
/*     */   
/*     */   public void addStation(Station station) {
/* 200 */     this.stationList.add(station.getSta());
/* 201 */     this.stations.add(station);
/*     */   }
/*     */   
/*     */   public void removeStation(String stn) {
/* 205 */     this.stationList.remove(stn);
/* 206 */     int size = this.stations.size();
/* 207 */     Station station = null;
/* 208 */     for (int i = 0; i < size; i++) {
/* 209 */       station = (Station)this.stations.get(i);
/* 210 */       if (station.getSta().equals(stn)) {
/* 211 */         this.stations.remove(station);
/* 212 */         break;
/*     */       }
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
/*     */   public double getClat()
/*     */   {
/* 226 */     return this.clat;
/*     */   }
/*     */   
/*     */   public double getClon() {
/* 230 */     return this.clon;
/*     */   }
/*     */   
/*     */   public double getCalt() {
/* 234 */     return this.calt;
/*     */   }
/*     */   
/*     */   public int getNStation() {
/* 238 */     return this.stations.size();
/*     */   }
/*     */   
/*     */   public int getNChannel() {
/* 242 */     int nsum = 0;
/* 243 */     int size = this.stations.size();
/* 244 */     for (int i = 0; i < size; i++) {
/* 245 */       nsum += ((Station)this.stations.get(0)).chans.size();
/*     */     }
/* 247 */     return nsum;
/*     */   }
/*     */   
/*     */   public Vector getStationList() {
/* 251 */     return this.stationList;
/*     */   }
/*     */   
/*     */   public void setClat(double clat) {
/* 255 */     this.clat = clat;
/*     */   }
/*     */   
/*     */   public void setClon(double clon) {
/* 259 */     this.clon = clon;
/*     */   }
/*     */   
/*     */   public void setDiscrip(String discrip) {
/* 263 */     this.discrip = discrip;
/*     */   }
/*     */   
/*     */   public String getNetworkID() {
/* 267 */     return this.networkID;
/*     */   }
/*     */   
/*     */   public void setNetworkID(String networkID) {
/* 271 */     this.networkID = networkID;
/*     */   }
/*     */   
/*     */   public String getVersion() {
/* 275 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(String version) {
/* 279 */     this.version = version;
/*     */   }
/*     */   
/*     */   public void setCalt(double calt) {
/* 283 */     this.calt = calt;
/*     */   }
/*     */   
/*     */   public Station getStation(String stnCode) {
/* 287 */     int size = this.stations.size();
/* 288 */     for (int i = 0; i < size; i++) {
/* 289 */       Station stn = (Station)this.stations.get(i);
/* 290 */       if (stn.getSta().equals(stnCode)) {
/* 291 */         return stn;
/*     */       }
/*     */     }
/*     */     
/* 295 */     return null;
/*     */   }
/*     */   
/*     */   public Channel getChannel(String stnCode, String locID, String chan) {
/* 299 */     int size = this.stations.size();
/* 300 */     for (int i = 0; i < size; i++) {
/* 301 */       Station station = (Station)this.stations.get(i);
/* 302 */       for (int j = 0; j < station.chans.size(); j++) {
/* 303 */         Channel chn = (Channel)station.chans.get(j);
/*     */         
/* 305 */         if ((chn.getSta().equals(stnCode)) && (chn.getLocID().equals(locID)) && (chn.getCha().equals(chan)))
/*     */         {
/* 307 */           return chn;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 312 */     return null;
/*     */   }
/*     */   
/*     */   public Channel[] getAllChannel()
/*     */   {
/* 317 */     Channel[] chs = new Channel[getNChannel()];
/* 318 */     int n = 0;
/* 319 */     int size = this.stations.size();
/* 320 */     for (int i = 0; i < size; i++) {
/* 321 */       Station station = (Station)this.stations.get(i);
/* 322 */       for (int j = 0; j < station.chans.size(); j++) {
/* 323 */         chs[(n++)] = ((Channel)station.chans.get(j));
/*     */       }
/*     */     }
/* 326 */     return chs;
/*     */   }
/*     */   
/*     */   public ChannelLocator[] getAllChannelLocator() {
/* 330 */     ChannelLocator[] cls = new ChannelLocator[getNChannel()];
/* 331 */     Channel[] chs = getAllChannel();
/* 332 */     for (int i = 0; i < cls.length; i++)
/* 333 */       cls[i] = chs[i].getChannelLocator();
/* 334 */     return cls;
/*     */   }
/*     */   
/*     */   public Channel[] getAllChannel(String stnCode)
/*     */   {
/* 339 */     Channel[] ch = null;
/* 340 */     int size = this.stations.size();
/* 341 */     for (int i = 0; i < size; i++) {
/* 342 */       Station station = (Station)this.stations.get(i);
/* 343 */       if (station.getSta().equals(stnCode)) {
/* 344 */         ch = new Channel[station.chans.size()];
/* 345 */         station.chans.copyInto(ch);
/* 346 */         break;
/*     */       }
/*     */     }
/* 349 */     return ch;
/*     */   }
/*     */   
/*     */   public Channel[] getAllChannel(String stnCode, String locId) {
/* 353 */     Vector cVec = new Vector();
/*     */     
/* 355 */     Channel[] ch = null;
/* 356 */     int size = this.stations.size();
/* 357 */     for (int i = 0; i < size; i++) {
/* 358 */       Station station = (Station)this.stations.get(i);
/* 359 */       if (station.getSta().equals(stnCode)) {
/* 360 */         for (int j = 0; j < station.chans.size(); j++) {
/* 361 */           Channel chn = (Channel)station.chans.get(j);
/*     */           
/* 363 */           if (chn.getLocID().equals(locId)) {
/* 364 */             cVec.add(chn);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 369 */     if (cVec.size() == 0)
/* 370 */       return null;
/* 371 */     ch = new Channel[cVec.size()];
/* 372 */     cVec.copyInto(ch);
/* 373 */     return ch;
/*     */   }
/*     */   
/*     */   public void parseNetwork(InputStream in)
/*     */   {
/* 378 */     DefaultHandler handler = new ParseHandler();
/*     */     
/* 380 */     SAXParserFactory factory = SAXParserFactory.newInstance();
/*     */     try {
/* 382 */       SAXParser saxParser = factory.newSAXParser();
/* 383 */       saxParser.parse(in, handler);
/*     */     }
/*     */     catch (SAXParseException spe)
/*     */     {
/* 387 */       System.out.println("\n** Parsing error, line " + spe.getLineNumber() + ", uri " + spe.getSystemId());
/*     */       
/*     */ 
/* 390 */       System.out.println("   " + spe.getMessage());
/*     */       
/*     */ 
/* 393 */       Exception x = spe;
/* 394 */       if (spe.getException() != null)
/* 395 */         x = spe.getException();
/* 396 */       x.printStackTrace();
/*     */ 
/*     */     }
/*     */     catch (SAXException sxe)
/*     */     {
/* 401 */       Exception x = sxe;
/* 402 */       if (sxe.getException() != null)
/* 403 */         x = sxe.getException();
/* 404 */       x.printStackTrace();
/*     */     }
/*     */     catch (ParserConfigurationException pce)
/*     */     {
/* 408 */       pce.printStackTrace();
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 412 */       ioe.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/* 416 */   public String getDiscrip() { return this.discrip; }
/*     */   
/*     */   public void addChannel(Channel chan) {}
/*     */   
/* 420 */   public static class Station { private String net = null;
/* 421 */     private String sta = null;
/* 422 */     private String cname = null;
/* 423 */     private String sname = null;
/* 424 */     private String staType = null;
/*     */     
/*     */ 
/* 427 */     private String coordsys = "WGS-84";
/*     */     
/* 429 */     private String mtype = null;
/* 430 */     private String transmitType = null;
/* 431 */     private String ondate = "2001/06/01";
/* 432 */     private String offdate = "";
/* 433 */     private String comment = "";
/* 434 */     private Vector chans = new Vector(3);
/*     */     private double elev;
/*     */     
/* 437 */     public Vector getChans() { return this.chans; }
/*     */     
/*     */     private double lat;
/*     */     private double lon;
/* 441 */     public String getCname() { return this.cname; }
/*     */     
/*     */     public void setCname(String cname) {
/* 444 */       this.cname = cname;
/*     */     }
/*     */     
/* 447 */     public String getComment() { return this.comment; }
/*     */     
/*     */     public String getCoordsys() {
/* 450 */       return this.coordsys;
/*     */     }
/*     */     
/* 453 */     public double getElev() { return this.elev; }
/*     */     
/*     */     public double getLat() {
/* 456 */       return this.lat;
/*     */     }
/*     */     
/* 459 */     public double getLon() { return this.lon; }
/*     */     
/*     */     public String getMtype() {
/* 462 */       return this.mtype;
/*     */     }
/*     */     
/* 465 */     public String getNet() { return this.net; }
/*     */     
/*     */     public String getOffdate() {
/* 468 */       return this.offdate;
/*     */     }
/*     */     
/* 471 */     public String getOndate() { return this.ondate; }
/*     */     
/*     */     public String getSname() {
/* 474 */       return this.sname;
/*     */     }
/*     */     
/* 477 */     public String getSta() { return this.sta; }
/*     */     
/*     */     public String getStaType() {
/* 480 */       return this.staType;
/*     */     }
/*     */     
/* 483 */     public String getTransmitType() { return this.transmitType; }
/*     */     
/*     */     public void setTransmitType(String transmitType) {
/* 486 */       this.transmitType = transmitType;
/*     */     }
/*     */     
/* 489 */     public void setStaType(String staType) { this.staType = staType; }
/*     */     
/*     */     public void setSta(String sta) {
/* 492 */       this.sta = sta;
/*     */     }
/*     */     
/* 495 */     public void setSname(String sname) { this.sname = sname; }
/*     */     
/*     */     public void setOndate(String ondate) {
/* 498 */       this.ondate = ondate;
/*     */     }
/*     */     
/* 501 */     public void setOffdate(String offdate) { this.offdate = offdate; }
/*     */     
/*     */     public void setNet(String net) {
/* 504 */       this.net = net;
/*     */     }
/*     */     
/* 507 */     public void setMtype(String mtype) { this.mtype = mtype; }
/*     */     
/*     */     public void setLon(double lon) {
/* 510 */       this.lon = lon;
/*     */     }
/*     */     
/* 513 */     public void setLat(double lat) { this.lat = lat; }
/*     */     
/*     */     public void setElev(double elev) {
/* 516 */       this.elev = elev;
/*     */     }
/*     */     
/* 519 */     public void setCoordsys(String coordsys) { this.coordsys = coordsys; }
/*     */     
/*     */     public void setComment(String comment) {
/* 522 */       this.comment = comment;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 526 */       StringBuffer strBuf = new StringBuffer();
/* 527 */       strBuf.append(this.net);strBuf.append('\t');
/* 528 */       strBuf.append(this.sta);strBuf.append('\t');
/* 529 */       strBuf.append(this.cname);strBuf.append('\t');
/* 530 */       strBuf.append(this.sname);strBuf.append('\t');
/* 531 */       strBuf.append(this.staType);strBuf.append('\t');
/* 532 */       strBuf.append(this.lat);strBuf.append('\t');
/* 533 */       strBuf.append(this.lon);strBuf.append('\t');
/* 534 */       strBuf.append(this.coordsys);strBuf.append('\t');
/* 535 */       strBuf.append(this.elev);strBuf.append('\t');
/* 536 */       strBuf.append(this.mtype);strBuf.append('\t');
/* 537 */       strBuf.append(this.transmitType);strBuf.append('\t');
/* 538 */       strBuf.append(this.ondate);strBuf.append('\t');
/* 539 */       strBuf.append(this.offdate);strBuf.append('\t');
/* 540 */       strBuf.append(this.comment);
/* 541 */       return strBuf.toString();
/*     */     } }
/*     */   
/*     */   public static class Channel { private String cha;
/*     */     private double hang;
/*     */     private String locID;
/* 547 */     private String net = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 555 */     private int sampleRate = 50;
/* 556 */     private String sta; private StationProfile.TransferFunction tranFun = new StationProfile.TransferFunction();
/*     */     private double vang;
/*     */     
/*     */     public ChannelLocator getChannelLocator() {
/* 560 */       ChannelLocator cl = new ChannelLocator();
/* 561 */       cl.setChannelLocator(this.net, this.sta, this.locID, this.cha);
/* 562 */       return cl;
/*     */     }
/*     */     
/* 565 */     public StationProfile.TransferFunction getTranFun() { return this.tranFun; }
/*     */     
/*     */     public void setTranFun(StationProfile.TransferFunction newTranFun) {
/* 568 */       this.tranFun = newTranFun;
/*     */     }
/*     */     
/* 571 */     public String getCha() { return this.cha; }
/*     */     
/*     */     public double getHang() {
/* 574 */       return this.hang;
/*     */     }
/*     */     
/* 577 */     public String getLocID() { return this.locID; }
/*     */     
/*     */     public String getNet() {
/* 580 */       return this.net;
/*     */     }
/*     */     
/* 583 */     public int getSampleRate() { return this.sampleRate; }
/*     */     
/*     */     public String getSta() {
/* 586 */       return this.sta;
/*     */     }
/*     */     
/* 589 */     public double getVang() { return this.vang; }
/*     */     
/*     */     public void setVang(double vang) {
/* 592 */       this.vang = vang;
/*     */     }
/*     */     
/* 595 */     public void setSta(String sta) { this.sta = sta; }
/*     */     
/*     */     public void setSampleRate(int sampleRate) {
/* 598 */       this.sampleRate = sampleRate;
/*     */     }
/*     */     
/* 601 */     public void setNet(String net) { this.net = net; }
/*     */     
/*     */     public void setLocID(String locID) {
/* 604 */       this.locID = locID;
/*     */     }
/*     */     
/* 607 */     public void setHang(double hang) { this.hang = hang; }
/*     */     
/*     */     public void setCha(String cha) {
/* 610 */       this.cha = cha;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 615 */       StringBuffer strBuf = new StringBuffer();
/* 616 */       strBuf.append(this.sta);strBuf.append('\t');
/* 617 */       strBuf.append(this.cha);strBuf.append('\t');
/* 618 */       strBuf.append(this.locID);strBuf.append('\t');
/* 619 */       strBuf.append(this.hang);strBuf.append('\t');
/* 620 */       strBuf.append(this.vang);strBuf.append('\t');
/* 621 */       strBuf.append(this.sampleRate);
/* 622 */       return strBuf.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeChannel(Channel chan) {}
/*     */   
/* 628 */   public static class TransferFunction { private String iunits = "um/s";
/* 629 */     private String ounits = "C";
/* 630 */     private double sfactor = 1.264053E12D;
/* 631 */     private double sensitivity = 440.0D;
/* 632 */     private double calper = 5.0D;
/* 633 */     private String deci = "";
/* 634 */     private String corr = "";
/*     */     
/*     */ 
/*     */ 
/* 638 */     private String zero = "";
/* 639 */     private String pole = "";
/*     */     private String descrip;
/*     */     
/* 642 */     public double getCalper() { return this.calper; }
/*     */     
/*     */     private int npole;
/* 645 */     public String getCorr() { return this.corr; }
/*     */     
/*     */ 
/* 648 */     public String getDeci() { return this.deci; }
/*     */     
/*     */     public String getDescrip() {
/* 651 */       return this.descrip;
/*     */     }
/*     */     
/* 654 */     public String getIunits() { return this.iunits; }
/*     */     
/*     */     public int getNpole() {
/* 657 */       return this.npole;
/*     */     }
/*     */     
/* 660 */     public int getNzero() { return this.nzero; }
/*     */     
/*     */     public String getOunits() {
/* 663 */       return this.ounits;
/*     */     }
/*     */     
/* 666 */     public String getPole() { return this.pole; }
/*     */     
/*     */     public double getSensitivity() {
/* 669 */       return this.sensitivity;
/*     */     }
/*     */     
/* 672 */     public double getSfactor() { return this.sfactor; }
/*     */     
/*     */     public int getSnum() {
/* 675 */       return this.snum;
/*     */     }
/*     */     
/* 678 */     public String getZero() { return this.zero; }
/*     */     
/*     */     public void setZero(String zero) {
/* 681 */       this.zero = zero;
/*     */     }
/*     */     
/* 684 */     public void setSnum(int snum) { this.snum = snum; }
/*     */     
/*     */     public void setSfactor(double sfactor) {
/* 687 */       this.sfactor = sfactor;
/*     */     }
/*     */     
/* 690 */     public void setSensitivity(double sensitivity) { this.sensitivity = sensitivity; }
/*     */     
/*     */     public void setPole(String pole) {
/* 693 */       this.pole = pole;
/*     */     }
/*     */     
/* 696 */     public void setOunits(String ounits) { this.ounits = ounits; }
/*     */     
/*     */     public void setNzero(int nzero) {
/* 699 */       this.nzero = nzero;
/*     */     }
/*     */     
/* 702 */     public void setNpole(int npole) { this.npole = npole; }
/*     */     
/*     */     public void setIunits(String iunits) {
/* 705 */       this.iunits = iunits;
/*     */     }
/*     */     
/* 708 */     public void setDescrip(String descrip) { this.descrip = descrip; }
/*     */     
/*     */     public void setDeci(String deci) {
/* 711 */       this.deci = deci;
/*     */     }
/*     */     
/* 714 */     public void setCorr(String corr) { this.corr = corr; }
/*     */     
/*     */     public void setCalper(double calper) {
/* 717 */       this.calper = calper;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 721 */       StringBuffer strBuf = new StringBuffer();
/* 722 */       strBuf.append(this.snum);strBuf.append('\t');
/* 723 */       strBuf.append(this.iunits);strBuf.append('\t');
/* 724 */       strBuf.append(this.ounits);strBuf.append('\t');
/* 725 */       strBuf.append(this.sfactor);strBuf.append('\t');
/* 726 */       strBuf.append(this.sensitivity);strBuf.append('\t');
/* 727 */       strBuf.append(this.calper);strBuf.append('\t');
/* 728 */       strBuf.append(this.deci);strBuf.append('\t');
/* 729 */       strBuf.append(this.corr);strBuf.append('\t');
/* 730 */       strBuf.append(this.nzero);strBuf.append('\t');
/* 731 */       strBuf.append(this.npole);strBuf.append('\t');
/* 732 */       strBuf.append(this.descrip);strBuf.append('\t');
/* 733 */       strBuf.append(this.zero);strBuf.append('\t');
/* 734 */       strBuf.append(this.pole);
/* 735 */       return strBuf.toString();
/*     */     }
/*     */     
/*     */     private int nzero;
/*     */     private int snum;
/*     */   }
/*     */   
/*     */   class ParseHandler extends DefaultHandler
/*     */   {
/*     */     private boolean bpole;
/*     */     private boolean bzero;
/*     */     private String chan;
/*     */     private StationProfile.Channel channel;
/*     */     private String locID;
/*     */     private String sta;
/*     */     private StationProfile.Station station;
/*     */     
/*     */     public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException
/*     */     {
/* 754 */       if (qName.equals("NETWORK")) {
/* 755 */         StationProfile.this.clat = Double.parseDouble(attrs.getValue("CLat"));
/* 756 */         StationProfile.this.clon = Double.parseDouble(attrs.getValue("CLon"));
/* 757 */         StationProfile.this.networkID = attrs.getValue("Name");
/* 758 */         StationProfile.this.version = attrs.getValue("Version");
/* 759 */         StationProfile.this.discrip = attrs.getValue("Discrip");
/*     */       }
/* 761 */       if (qName.equals("STATION")) {
/* 762 */         this.station = new StationProfile.Station();
/* 763 */         StationProfile.this.stationList.add(attrs.getValue("Sta"));
/* 764 */         this.station.setCname(attrs.getValue("Cname"));
/* 765 */         this.station.setComment(attrs.getValue("Comment"));
/* 766 */         this.station.setCoordsys(attrs.getValue("Coordsys"));
/* 767 */         this.station.setElev(Double.parseDouble(attrs.getValue("Elev")));
/* 768 */         this.station.setLat(Double.parseDouble(attrs.getValue("Lat")));
/* 769 */         this.station.setLon(Double.parseDouble(attrs.getValue("Lon")));
/* 770 */         this.station.setMtype(attrs.getValue("MType"));
/* 771 */         this.station.setNet(attrs.getValue("Net"));
/* 772 */         this.station.setOffdate(attrs.getValue("Offdate"));
/* 773 */         this.station.setOndate(attrs.getValue("Ondate"));
/* 774 */         this.station.setSname(attrs.getValue("Sname"));
/* 775 */         this.station.setSta(attrs.getValue("Sta"));
/* 776 */         this.station.setStaType(attrs.getValue("StaType"));
/* 777 */         this.station.setTransmitType(attrs.getValue("TransmitType"));
/* 778 */         this.sta = this.station.getSta();
/*     */       }
/* 780 */       if (qName.equals("CHANNEL")) {
/* 781 */         this.channel = new StationProfile.Channel();
/* 782 */         this.channel.setNet(this.station.getNet());
/* 783 */         this.channel.setSta(this.sta);
/* 784 */         this.channel.setCha(attrs.getValue("Cha"));
/* 785 */         this.channel.setLocID(attrs.getValue("LocID"));
/* 786 */         this.channel.setHang(Double.parseDouble(attrs.getValue("Hang")));
/* 787 */         this.channel.setVang(Double.parseDouble(attrs.getValue("Vang")));
/* 788 */         this.channel.setSampleRate(Integer.parseInt(attrs.getValue("SampleRate")));
/* 789 */         this.chan = this.channel.getCha();
/* 790 */         this.locID = this.channel.getLocID();
/*     */       }
/* 792 */       if (qName.equals("PAZ")) {
/* 793 */         StationProfile.Channel.access$100(this.channel).setSnum(Integer.parseInt(attrs.getValue("Snum")));
/* 794 */         StationProfile.Channel.access$100(this.channel).setIunits(attrs.getValue("Iunits"));
/* 795 */         StationProfile.Channel.access$100(this.channel).setOunits(attrs.getValue("Ounits"));
/* 796 */         StationProfile.Channel.access$100(this.channel).setSfactor(Double.parseDouble(attrs.getValue("Sfactor")));
/* 797 */         StationProfile.Channel.access$100(this.channel).setSensitivity(Double.parseDouble(attrs.getValue("Sensitivity")));
/*     */         
/* 799 */         StationProfile.Channel.access$100(this.channel).setCalper(Double.parseDouble(attrs.getValue("Calper")));
/* 800 */         StationProfile.Channel.access$100(this.channel).setDeci(attrs.getValue("Deci"));
/* 801 */         StationProfile.Channel.access$100(this.channel).setNzero(Integer.parseInt(attrs.getValue("Nzero")));
/* 802 */         StationProfile.Channel.access$100(this.channel).setNpole(Integer.parseInt(attrs.getValue("Npole")));
/* 803 */         StationProfile.Channel.access$100(this.channel).setDescrip(attrs.getValue("Descrip"));
/*     */       }
/* 805 */       if (qName.equals("ZERO")) {
/* 806 */         this.bzero = true;
/*     */       }
/* 808 */       if (qName.equals("POLE")) {
/* 809 */         this.bpole = true;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void endElement(String namespaceURI, String sName, String qName)
/*     */       throws SAXException
/*     */     {
/* 819 */       if (qName.equals("STATION")) {
/* 820 */         StationProfile.this.stations.add(this.station);
/* 821 */         this.station = null;
/*     */       }
/* 823 */       if (qName.equals("CHANNEL")) {
/* 824 */         StationProfile.Station.access$000(this.station).add(this.channel);
/* 825 */         this.channel = null;
/*     */       }
/* 827 */       if (qName.equals("ZERO")) {
/* 828 */         this.bzero = false;
/*     */       }
/* 830 */       if (qName.equals("POLE")) {
/* 831 */         this.bpole = false;
/*     */       }
/*     */     }
/*     */     
/*     */     public void characters(char[] buf, int offset, int len)
/*     */       throws SAXException
/*     */     {
/* 838 */       if (this.bzero) {
/* 839 */         StationProfile.TransferFunction.access$284(StationProfile.Channel.access$100(this.channel), new String(buf, offset, len));
/*     */       }
/* 841 */       if (this.bpole) {
/* 842 */         StationProfile.TransferFunction.access$384(StationProfile.Channel.access$100(this.channel), new String(buf, offset, len));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void error(SAXParseException e)
/*     */       throws SAXParseException
/*     */     {
/* 854 */       throw e;
/*     */     }
/*     */     
/*     */     public void warning(SAXParseException err)
/*     */       throws SAXParseException
/*     */     {
/* 860 */       System.out.println("** Warning, line " + err.getLineNumber() + ", uri " + err.getSystemId());
/*     */       
/*     */ 
/* 863 */       System.out.println("   " + err.getMessage());
/*     */     }
/*     */     
/*     */     ParseHandler() {}
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/config/StationProfile.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */