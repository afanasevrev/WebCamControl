/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcamcontrol;

import CvUtils.CvUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

/**
 *
 * @author rewoly
 */
public class WebCamControl {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static boolean isRun = true;
    public static boolean isEnd = true;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame window = new JFrame("Нажмите ESC для отключения от камеры");
        window.setSize(640, 480);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setLocationRelativeTo(null);
        
        // Обработка нажатия кнопки Закрыть в заголовке окна
        
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRun = false;
                if (isEnd) {
                    window.dispose();
                    System.exit(0);
                } else {
                    System.out.println("Сначала нажмите ESC,потом закрыть");
                }
            }
        });
        // Обработка нажатия клавиши ESC
        window.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 27) {
                isRun = false;
            }
        }
        });
        
        JLabel label = new JLabel();
        window.setContentPane(label);
        window.setVisible(true);
        
        //Подключаемся к камере
        
        VideoCapture camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            window.setTitle("Не удалось подключиться к камере");
            isRun = false;
            isEnd = true;
            return;
        }
        try {
        //Задаем размеры кадра
        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);
        //Считываем кадры
        Mat frame = new Mat();
        BufferedImage img = null;
        while (isRun) {
        if (camera.read(frame)) {
            //Здесь можно вставить код обработки кадра
            img = CvUtils.MatToBufferedImage(frame);
            if (img != null) {
                ImageIcon imageIcon = new ImageIcon(img);
                label.setIcon(imageIcon);
                label.repaint();
                window.pack();
                //detectAndDecode​(img);
            }
            try {
                Thread.sleep(40); //25 кадров в секунду
            } catch (InterruptedException e) {}
        } else {
            System.out.println("Не удалось захватить кадр");
            break;
        }
        }
        } finally {
            camera.release();
            isRun = false;
            isEnd = true;
        }
        window.setTitle("Камера отключена");
    }
}
