package cn.gd.util;

import java.util.EventListener;

public abstract interface QueueAddElementEventListener
  extends EventListener
{
  public static final String cvsid = "$Id: QueueAddElementEventListener.java,v 1.2 2001/12/19 06:44:48 hwh Exp $";
  
  public abstract void QueueAddElementHandler(QueueAddElementEvent paramQueueAddElementEvent);
}


/* Location:              /home/muly/Desktop/IPPlot.jar!/cn/gd/util/QueueAddElementEventListener.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       0.7.1
 */