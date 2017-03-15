package cn.gd.seismology.realtimestream.event;

import java.util.EventListener;

public abstract interface MiniSeedPortEventListener
  extends EventListener
{
  public static final String cvsid = "$Id: MiniSeedPortEventListener.java,v 1.3 2002/01/14 08:20:19 hwh Exp $";
  
  public abstract void miniSeedEvent(MiniSeedPortEvent paramMiniSeedPortEvent);
}


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/seismology/realtimestream/event/MiniSeedPortEventListener.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */