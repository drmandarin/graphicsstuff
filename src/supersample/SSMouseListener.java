package supersample;

import java.awt.GraphicsDevice;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SSMouseListener implements MouseListener{
  GraphicsDevice gD;
  JFrame parent, newFrame;
  JPanel panel;

  public SSMouseListener(JFrame frame, GraphicsDevice gD, JPanel panel){
    parent = frame;
    this.gD = gD;
    this.panel = panel;
  }
  
  public void mouseClicked(MouseEvent mE){
    if (gD.getFullScreenWindow() == null){
      System.out.println("here");
      parent.getContentPane().removeAll();
      newFrame = new JFrame ("newFrame");
      newFrame.getContentPane().add(panel);
      //frame1.setExtendedState(JFrame.MAXIMIZED_BOTH);
      //frame1.setUndecorated(true);
      newFrame.addMouseListener(new SSMouseListener(newFrame,gD,panel));
      newFrame.setVisible(true);
      parent.dispose();
      gD.setFullScreenWindow(newFrame);
    }
    else{
      gD.setFullScreenWindow(null);
    }
  }

  public void mouseEntered(MouseEvent mE){
  }

  public void mouseExited(MouseEvent mE){
  }
  
  public void mousePressed(MouseEvent mE){
  }

  public void mouseReleased(MouseEvent mE){
  }
}