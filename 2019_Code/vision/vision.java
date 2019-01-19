import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.Buffer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
class vision{
    
    static void main(String[] args){
        //Open Video Capture (Get camera feed)
        VideoCapture cam = new VideoCapture(0);

        //Mat (Image) Variable that feed is going into.
        Mat frame = new Mat();

        //Apply frame to image
        cam.read(frame);
        
        
    }

    static BufferedImage mat2BufferedImage(Mat m){

        int bufferSize = m.channels()*m.col()*m.rows();

        byte[] b = new byte[bufferSize];
        m.get(0, 0,b);

        BufferedImage img = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();

        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return img;
    }

    static void displayImage(Image img){
        ImageIcon icon = new ImageIcon(img);

        JFrame frame = new JFrame();
        frame.setSize(img.getWidth(null) + 50, img.getHeight(null) + 50 );

        JLabel label = new JLabel();
        label.setIcon(icon);
        frame.add(label);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}