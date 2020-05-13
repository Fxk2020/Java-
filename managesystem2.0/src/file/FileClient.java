package file;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import common.Users;
import log.Log;

public class FileClient extends Socket {
	
	private static final String SERVER_IP = "127.0.0.1"; // �����IP
	private static final int SERVER_PORT = 8899; // ����˶˿�
	private Socket client;
	private FileInputStream fis;
	private DataOutputStream dos;
	
	JFrame frame = new JFrame();
	JFileChooser fileChooser;//����ϴ��ļ���ʱ�򶼻ᵯ��һ����������ѡ��Ҫ�ϴ����ļ�����Ȼ������������ȴ��֪�����������������Java���������ļ��������ھ���FileChooser
	
	boolean ok=true;

	public FileClient() throws Exception {
		super(SERVER_IP, SERVER_PORT);
		this.client = this;
		System.out.println("Cliect[port:" + client.getLocalPort() + "] �ɹ����ӷ����");
	}   

	public void sendFile() throws Exception {
		try {
			
			while(ok){
//			String s = JOptionPane.showInputDialog( "��������Ҫ������ļ���λ�ã�");//this������contentPaneҲ�У�����ʾ��Ҫ��ʾ����Ϣ��Ŀ����� 
//			//���showMessaggeDialog�����������ˣ��������·��� //JOptionPane.showMessageDialog
			
			File file = new File(Gui());
			if (file.exists()) {
				fis = new FileInputStream(file);
				dos = new DataOutputStream(client.getOutputStream());

				// �ļ����ͳ���
				dos.writeUTF(file.getName());
				dos.flush();
				dos.writeLong(file.length());
				dos.flush();

				// ��ʼ�����ļ�
				System.out.println("======== ��ʼ�����ļ� ========");
				
				byte[] bytes = new byte[1024];
				int length = 0;
				long progress = 0;
				while ((length = fis.read(bytes, 0, bytes.length)) != -1) {//����-1ʱ�ļ��Ѿ��������
					dos.write(bytes, 0, length);
					dos.flush();
					progress += length;
					System.out.print("| " + (100 * progress / file.length()) + "% |");
				}
				System.out.println();
				System.out.println("======== �ļ�����ɹ� ========");
				
//				JOptionPane.showMessageDialog(frame, "����ɹ�", "��", JOptionPane.INFORMATION_MESSAGE);
//�Ƿ����㣬�񷵻�1����ŷ���-1		
				int i=0;
				i=JOptionPane.showConfirmDialog(null, "�Ƿ��������", "           ����ɹ�,��", JOptionPane.YES_NO_OPTION); 
				if(i!=0) {
					break;
				}
			}
			
			}
		}catch(NullPointerException e2) {
			System.out.println("ð����");
		}
		
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null)
				fis.close();
			if (dos != null)
				dos.close();
			client.close();
		}
	}
	
	//���ѡ����ļ���·����String��
	public  String Gui() {
		
		String s=null;
		 try { // ʹ��Windows�Ľ�����
			   UIManager
			     .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			  } catch (Exception e) {
			   e.printStackTrace();
			  }

		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		fileChooser.setMultiSelectionEnabled(true);
		
		fileChooser.showOpenDialog(null);
		File f = fileChooser.getSelectedFile();
		if (f != null) {
//		    System.out.println(f.getPath());
			s= f.getPath();
		}
		return s;
	}

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {                      // �����ڲ��࣬��д��run����
			public void run() {
				try {
					FileClient c = new FileClient(); // �����ͻ�������
					c.sendFile(); // �����ļ�
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		
	}
}
