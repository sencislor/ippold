package cn.gd.seismology.realtimestream;

import cn.gd.seismology.realtimestream.event.MiniSeedPortEventListener;
import edu.iris.miniseedutils.steim.GenericMiniSeedRecord;

public abstract interface MiniSeedStreamInf
{
  public static final String cvsid = "$Id: MiniSeedStreamInf.java,v 1.2 2002/01/14 08:20:41 hwh Exp $";
  
  public abstract boolean accept(GenericMiniSeedRecord paramGenericMiniSeedRecord);
  
  public abstract void addMiniSeedPortEventListener(MiniSeedPortEventListener paramMiniSeedPortEventListener);
  
  public abstract void close();
  
  public abstract String[] getStationCode();
  
  public abstract void removeMiniSeedPortEventListener(MiniSeedPortEventListener paramMiniSeedPortEventListener);
}


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimestream/MiniSeedStreamInf.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */