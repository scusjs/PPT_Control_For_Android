package com.sony.server.main;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import com.sony.server.entity.PPT;
import com.sony.server.utils.GetPPT;

public class MainUI extends JPanel {

	static final long serialVersionUID = 5854418136127725290L;
	public static String fileName;
	public static boolean stop = false;
	public class ExtensionFilter extends FileFilter {
		private String extensions[];

		private String description;

		public ExtensionFilter(String description, String extension) {
			this(description, new String[] { extension });
		}

		public ExtensionFilter(String description, String extensions[]) {
			this.description = description;
			this.extensions = (String[]) extensions.clone();
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			}
			int count = extensions.length;
			String path = file.getAbsolutePath();
			for (int i = 0; i < count; i++) {
				String ext = extensions[i];
				if (path.endsWith(ext)
						&& (path.charAt(path.length() - ext.length()) == '.')) {
					return true;
				}
			}
			return false;
		}

		public String getDescription() {
			return (description == null ? extensions[0] : description);
		}
	}

	public MainUI() {
		JButton jb = new JButton("请选择PPT文件或者PPTX文件");
		add(jb);
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				FileSystemView fsv = FileSystemView.getFileSystemView();
				chooser.setCurrentDirectory(fsv.getHomeDirectory());
				// chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				FileFilter type1 = new ExtensionFilter("PPT/PPTX", new String[] { ".ppt",".pptx"});
				
				chooser.addChoosableFileFilter(type1);
				chooser.setFileFilter(type1);
				chooser.setAcceptAllFileFilterUsed(true);
				int status = chooser.showOpenDialog(MainUI.this);
				if (status == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					String filename = f.toString().replace("\\","\\\\" );
					System.out.println(filename);
					/**
					 * 这里，filename的值即为需要的文件路径名称！
					 */
					fileName = filename;
					
					
					try {
						if(GetPPT.checkFile(filename, "pptx")){
							GetPPT.getPPTX(filename);
						}else{
							GetPPT.getPPT(filename);
						}
						System.out.println(PPT.pptList.toString());
						
						if (filename != null) {
							filename = null;
							StartServer.startServer();
							
							
						}
						
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
				}
			}
		};
		jb.addActionListener(listener);
	}
	public static String getFileName() {
		return fileName;
	}

	public static void mainUI() {
		JFrame f = new JFrame("PPT遥控器");
		JPanel j = new MainUI();
		f.getContentPane().add(j, BorderLayout.CENTER);
		f.setSize(300, 200);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int x = (int)(toolkit.getScreenSize().getWidth()-f.getWidth())/2;
		int y = (int)(toolkit.getScreenSize().getHeight()-f.getHeight())/2;
		f.setLocation(x, y);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}