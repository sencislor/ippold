/*     */ package cn.gd.seismology.realtimeplot.gui;
/*     */ 
/*     */ import cn.gd.seismology.ChannelLocator;
/*     */ import cn.gd.seismology.config.StationProfile;
/*     */ import java.awt.Container;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemEvent;
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ public class SetupDialog extends JDialog
/*     */ {
/*  33 */   static StationProfile sprofile = null;
/*     */   
/*  35 */   Properties props = null;
/*  36 */   Vector vecUnSeclect = new Vector();
/*  37 */   Vector vecSelected = new Vector();
/*  38 */   boolean breLoad = false;
/*     */   
/*  40 */   JPanel panel1 = new JPanel();
/*  41 */   ButtonGroup buttonGroupConnectionType = new ButtonGroup();
/*  42 */   JPanel jPanel1 = new JPanel();
/*  43 */   JRadioButton jRadioButtonTCP = new JRadioButton();
/*  44 */   JRadioButton jRadioButtonUDP = new JRadioButton();
/*  45 */   JPanel jPanel2 = new JPanel();
/*  46 */   GridLayout gridLayout1 = new GridLayout();
/*  47 */   JTextField jTextFieldServer = new JTextField();
/*  48 */   JTextField jTextFieldUser = new JTextField();
/*  49 */   JLabel jLabel2 = new JLabel();
/*  50 */   JLabel jLabel3 = new JLabel();
/*  51 */   JLabel jLabel4 = new JLabel();
/*  52 */   JTextField jTextFieldPort = new JTextField();
/*  53 */   JLabel jLabel1 = new JLabel();
/*  54 */   JScrollPane jScrollPane1 = new JScrollPane();
/*  55 */   JList jListUnSelect = new JList();
/*  56 */   JScrollPane jScrollPane2 = new JScrollPane();
/*  57 */   JList jListSelected = new JList();
/*  58 */   JLabel jLabel5 = new JLabel();
/*  59 */   JLabel jLabel6 = new JLabel();
/*  60 */   JButton jButtonAdd = new JButton();
/*  61 */   JButton jButtonRemove = new JButton();
/*  62 */   javax.swing.JPasswordField jPasswordField = new javax.swing.JPasswordField();
/*  63 */   JButton jButtonOK = new JButton();
/*  64 */   JButton jButtonCancel = new JButton();
/*  65 */   java.awt.GridBagLayout gridBagLayout1 = new java.awt.GridBagLayout();
/*     */   static ChannelLocator[] cls;
/*     */   public static final String cvsid = "$Id: SetupDialog.java,v 1.3 2002/01/14 08:20:49 hwh Exp $";
/*     */   static final String nnfile = "cfg/network.xml";
/*     */   static final String pnfile = "IPPlot.properties";
/*     */   
/*     */   static
/*     */   {
/*  34 */     cls = null;
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
/*     */     try
/*     */     {
/*  69 */       sprofile = new StationProfile();
/*  70 */       sprofile.parseNetwork(new java.io.FileInputStream("cfg/network.xml"));
/*  71 */       cls = sprofile.getAllChannelLocator();
/*     */     }
/*     */     catch (IOException ioEx) {
/*  74 */       ioEx.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public SetupDialog(java.awt.Frame frame, String title, boolean modal) {
/*  79 */     super(frame, title, modal);
/*     */     try {
/*  81 */       jbInit();
/*  82 */       pack();
/*     */     }
/*     */     catch (Exception ex) {
/*  85 */       ex.printStackTrace();
/*     */     }
/*  87 */     setInitValue();
/*  88 */     update();
/*     */   }
/*     */   
/*     */   public SetupDialog() {
/*  92 */     this(null, "", false);
/*     */   }
/*     */   
/*     */   void jbInit() throws Exception {
/*  96 */     this.panel1.setLayout(this.gridBagLayout1);
/*  97 */     this.jRadioButtonTCP.setToolTipText("");
/*  98 */     this.jRadioButtonTCP.setSelected(true);
/*  99 */     this.jRadioButtonTCP.setText("TCP");
/* 100 */     this.jRadioButtonUDP.setText("UDP");
/* 101 */     this.jRadioButtonUDP.addItemListener(new java.awt.event.ItemListener() {
/*     */       public void itemStateChanged(ItemEvent e) {
/* 103 */         SetupDialog.this.jRadioButtonUDP_itemStateChanged(e);
/*     */       }
/* 105 */     });
/* 106 */     this.jPanel2.setLayout(this.gridLayout1);
/* 107 */     this.gridLayout1.setColumns(2);
/* 108 */     this.gridLayout1.setRows(4);
/* 109 */     this.jLabel2.setText("用户");
/* 110 */     this.jLabel3.setText("服务器");
/* 111 */     this.jLabel4.setText("端口");
/* 112 */     this.jLabel1.setText("密码");
/* 113 */     this.jLabel5.setText("未显示的通道表");
/* 114 */     this.jLabel6.setText("显示的通道表");
/* 115 */     this.jButtonAdd.setText("增加");
/* 116 */     this.jButtonAdd.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 118 */         SetupDialog.this.jButtonAdd_actionPerformed(e);
/*     */       }
/* 120 */     });
/* 121 */     this.jButtonRemove.setText("删除");
/* 122 */     this.jButtonRemove.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 124 */         SetupDialog.this.jButtonRemove_actionPerformed(e);
/*     */       }
/* 126 */     });
/* 127 */     this.jPasswordField.setText("");
/* 128 */     this.jButtonOK.setActionCommand("jButtonOK");
/* 129 */     this.jButtonOK.setText("确定");
/* 130 */     this.jButtonOK.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 132 */         SetupDialog.this.jButtonOK_actionPerformed(e);
/*     */       }
/* 134 */     });
/* 135 */     this.jButtonCancel.setActionCommand("jButtonOK");
/* 136 */     this.jButtonCancel.setText("取消");
/* 137 */     this.jButtonCancel.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent e) {
/* 139 */         SetupDialog.this.jButtonCancel_actionPerformed(e);
/*     */       }
/* 141 */     });
/* 142 */     this.jPanel1.add(this.jRadioButtonTCP, null);
/* 143 */     this.jPanel1.add(this.jRadioButtonUDP, null);
/* 144 */     this.panel1.add(this.jButtonOK, new GridBagConstraints(1, 4, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(36, 48, 16, 39), 22, 0));
/*     */     
/* 146 */     this.panel1.add(this.jButtonCancel, new GridBagConstraints(2, 4, 2, 1, 0.0D, 0.0D, 10, 0, new Insets(36, 12, 16, 31), 0, 0));
/*     */     
/* 148 */     this.panel1.add(this.jScrollPane2, new GridBagConstraints(4, 1, 1, 3, 1.0D, 1.0D, 10, 1, new Insets(13, 17, 0, 26), -86, 201));
/*     */     
/* 150 */     this.panel1.add(this.jLabel6, new GridBagConstraints(4, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(25, 17, 0, 71), 57, 9));
/*     */     
/* 152 */     this.panel1.add(this.jLabel5, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(25, 31, 0, 0), 45, 9));
/*     */     
/* 154 */     this.panel1.add(this.jScrollPane1, new GridBagConstraints(1, 1, 2, 3, 1.0D, 1.0D, 10, 1, new Insets(13, 31, 0, 0), -86, 201));
/*     */     
/* 156 */     this.panel1.add(this.jButtonRemove, new GridBagConstraints(3, 3, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(57, 16, 108, 0), 0, 0));
/*     */     
/* 158 */     this.panel1.add(this.jButtonAdd, new GridBagConstraints(3, 2, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(97, 16, 0, 0), 0, 0));
/*     */     
/* 160 */     this.jScrollPane1.getViewport().add(this.jListUnSelect, null);
/* 161 */     this.jScrollPane2.getViewport().add(this.jListSelected, null);
/* 162 */     getContentPane().add(this.panel1);
/* 163 */     this.buttonGroupConnectionType.add(this.jRadioButtonUDP);
/* 164 */     this.buttonGroupConnectionType.add(this.jRadioButtonTCP);
/* 165 */     this.panel1.add(this.jPanel2, new GridBagConstraints(0, 2, 1, 2, 1.0D, 1.0D, 10, 1, new Insets(17, 32, 106, 15), 167, 109));
/*     */     
/* 167 */     this.jPanel2.add(this.jLabel3, null);
/* 168 */     this.jPanel2.add(this.jTextFieldServer, null);
/* 169 */     this.jPanel2.add(this.jLabel4, null);
/* 170 */     this.jPanel2.add(this.jTextFieldPort, null);
/* 171 */     this.jPanel2.add(this.jLabel2, null);
/* 172 */     this.jPanel2.add(this.jTextFieldUser, null);
/* 173 */     this.jPanel2.add(this.jLabel1, null);
/* 174 */     this.jPanel2.add(this.jPasswordField, null);
/* 175 */     this.panel1.add(this.jPanel1, new GridBagConstraints(0, 0, 1, 2, 1.0D, 1.0D, 10, 1, new Insets(16, 50, 0, 0), 121, 26));
/*     */   }
/*     */   
/*     */   void jRadioButtonUDP_itemStateChanged(ItemEvent e)
/*     */   {
/* 180 */     if (this.jRadioButtonUDP.isSelected()) {
/* 181 */       this.jPasswordField.setEditable(false);
/* 182 */       this.jTextFieldUser.setEnabled(false);
/* 183 */       this.props.put("IPPlot.connection.type", "UDP");
/*     */     }
/* 185 */     if (this.jRadioButtonTCP.isSelected()) {
/* 186 */       this.jPasswordField.setEditable(true);
/* 187 */       this.jTextFieldUser.setEnabled(true);
/* 188 */       this.props.put("IPPlot.connection.type", "TCP");
/*     */     }
/*     */   }
/*     */   
/*     */   private int getIdx(ChannelLocator cl)
/*     */   {
/* 194 */     int i = -1;
/* 195 */     for (i = 0; i < cls.length; i++)
/* 196 */       if (cls[i].equals(cl))
/* 197 */         return i;
/* 198 */     return i;
/*     */   }
/*     */   
/*     */   void setInitValue() {
/* 202 */     for (int i = 0; i < cls.length; i++) {
/* 203 */       this.vecUnSeclect.add(cls[i]);
/*     */     }
/* 205 */     this.props = new Properties();
/*     */     try {
/* 207 */       this.props.load(new java.io.FileInputStream("IPPlot.properties"));
/*     */     }
/*     */     catch (IOException ioEx) {
/* 210 */       ioEx.printStackTrace();
/*     */     }
/*     */     
/* 213 */     boolean bTCP = true;
/* 214 */     String connectionType = this.props.getProperty("IPPlot.connection.type", "TCP");
/* 215 */     if (connectionType == null) {
/* 216 */       bTCP = true;
/*     */     } else {
/* 218 */       if (connectionType.equalsIgnoreCase("TCP")) {
/* 219 */         this.jRadioButtonTCP.setSelected(true);
/* 220 */         this.jPasswordField.setEditable(true);
/* 221 */         this.jTextFieldUser.setEnabled(true);
/*     */       }
/* 223 */       if (connectionType.equalsIgnoreCase("UDP")) {
/* 224 */         this.jRadioButtonUDP.setSelected(true);
/* 225 */         this.jPasswordField.setEditable(false);
/* 226 */         this.jTextFieldUser.setEnabled(false);
/*     */       }
/*     */     }
/* 229 */     this.jTextFieldServer.setText(this.props.getProperty("IPPlot.connection.host", "127.0.0.1"));
/* 230 */     this.jTextFieldPort.setText(this.props.getProperty("IPPlot.connection.port", "5000"));
/* 231 */     this.jTextFieldUser.setText(this.props.getProperty("IPPlot.connection.user", "ipplot"));
/* 232 */     this.jPasswordField.setText(this.props.getProperty("IPPlot.connection.password", "ipplot"));
/*     */     
/* 234 */     String line = this.props.getProperty("IPPlot.channel");
/* 235 */     StringTokenizer st = new StringTokenizer(line, " +");
/*     */     
/* 237 */     while (st.hasMoreTokens()) {
/* 238 */       StringTokenizer st0 = new StringTokenizer(st.nextToken(), " /");
/* 239 */       ChannelLocator cl = new ChannelLocator(st0.nextToken(), st0.nextToken(), st0.nextToken(), st0.nextToken());
/* 240 */       this.vecUnSeclect.remove(cl);
/* 241 */       this.vecSelected.add(cl);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void save()
/*     */   {
/* 248 */     this.props.put("IPPlot.connection.host", this.jTextFieldServer.getText());
/* 249 */     this.props.put("IPPlot.connection.port", this.jTextFieldPort.getText());
/* 250 */     this.props.put("IPPlot.connection.user", this.jTextFieldUser.getText());
/* 251 */     this.props.put("IPPlot.connection.password", new String(this.jPasswordField.getPassword()));
/*     */     
/*     */ 
/* 254 */     StringBuffer sb = new StringBuffer();
/* 255 */     Enumeration en = this.vecSelected.elements();
/* 256 */     while (en.hasMoreElements()) {
/* 257 */       sb.append(en.nextElement());
/* 258 */       sb.append(" + ");
/*     */     }
/* 260 */     this.props.put("IPPlot.channel", sb.toString());
/*     */     try {
/* 262 */       java.io.FileOutputStream fos = new java.io.FileOutputStream("IPPlot.properties");
/* 263 */       this.props.store(fos, "IPPlot configuration");
/*     */     }
/*     */     catch (Exception ex) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void update()
/*     */   {
/* 272 */     this.jListSelected.removeAll();
/* 273 */     this.jListUnSelect.removeAll();
/* 274 */     java.util.Collections.sort(this.vecSelected);
/* 275 */     java.util.Collections.sort(this.vecUnSeclect);
/*     */     
/* 277 */     this.jListSelected.setListData(this.vecSelected);
/* 278 */     this.jListUnSelect.setListData(this.vecUnSeclect);
/* 279 */     this.jScrollPane1.validate();
/* 280 */     this.jScrollPane2.validate();
/*     */   }
/*     */   
/*     */   void jButtonAdd_actionPerformed(ActionEvent e)
/*     */   {
/* 285 */     Object[] objs = this.jListUnSelect.getSelectedValues();
/* 286 */     for (int i = 0; i < objs.length; i++) {
/* 287 */       this.vecUnSeclect.remove(objs[i]);
/* 288 */       this.vecSelected.add(objs[i]);
/*     */     }
/* 290 */     update();
/*     */   }
/*     */   
/*     */   void jButtonRemove_actionPerformed(ActionEvent e) {
/* 294 */     Object[] objs = this.jListSelected.getSelectedValues();
/* 295 */     for (int i = 0; i < objs.length; i++) {
/* 296 */       this.vecSelected.remove(objs[i]);
/* 297 */       this.vecUnSeclect.add(objs[i]);
/*     */     }
/* 299 */     update();
/*     */   }
/*     */   
/*     */   void jButtonOK_actionPerformed(ActionEvent e) {
/* 303 */     this.breLoad = true;
/* 304 */     save();
/* 305 */     dispose();
/*     */   }
/*     */   
/*     */   void jButtonCancel_actionPerformed(ActionEvent e) {
/* 309 */     this.breLoad = false;
/* 310 */     dispose();
/*     */   }
/*     */   
/*     */   public boolean isReload() {
/* 314 */     return this.breLoad;
/*     */   }
/*     */ }


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimeplot/gui/SetupDialog.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */