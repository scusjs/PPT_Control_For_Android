package com.sony.server.utils;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sony.server.entity.PPT;

public class GetPPT {
	private static String PPT_comment = "";
	
	/**
	 * ���ppt��ʽ�������ļ�·����String�����׳�IOException�������Ϣ������PPTInfo.pptList��
	 * @param fileName
	 * @throws IOException
	 * @author �����
	 */
	
	public static void getPPT(String fileName) throws IOException {

		if(fileName == null)
			return ;
		FileInputStream is = new FileInputStream(fileName);
		SlideShow ppt = new SlideShow(is);
		
		is.close();
		PPT_comment = "";
		Dimension pgsize = ppt.getPageSize();// PPTҳ���С�������ã�����
		org.apache.poi.hslf.model.Slide[] slide = ppt.getSlides();
		for (int i = 0; i < slide.length; i++) {
			if (slide[i].getNotesSheet() != null
					&& slide[i].getNotesSheet().getTextRuns() != null) {
				// ��ȡ��ע				
				PPT_comment = slide[i].getNotesSheet().getTextRuns()[0]
						.getText();
				
			} else
				PPT_comment = "";
			PPT.pptList.add(new PPT(i+1,"",PPT_comment));
		}
	}
	/**
	 * ���pptx��ʽ�������ļ�·����String�����׳�IOException�������Ϣ������PPTInfo.pptList��
	 * @param fileName
	 * @throws IOException
	 * @author �����
	 */
	public static void getPPTX(String fileName) throws FileNotFoundException, IOException{
		FileInputStream is = new FileInputStream(fileName);
		XMLSlideShow xmlSlideShow = new XMLSlideShow(is);
		XSLFSlide[] xslfSlides = xmlSlideShow.getSlides();
		is.close();
		PPT_comment = "";
		for (int i = 0; i < xslfSlides.length; i++) {
			PPT_comment = xslfSlides[i].getNotes().getXmlObject().xmlText();
			Document doc = Jsoup.parse(PPT_comment);
			//Elements comment = doc.getElementsByTag("a:t");
			PPT_comment = doc.getElementsByTag("a:t").first().text();
			PPT.pptList.add(new PPT(i+1,"",PPT_comment));
		}
	}

	/**
	 * �ж��Ƿ�Ϊָ����ʽ�ļ�
	 * @param filename �ļ���
	 * @param str ��׺������"."
	 * @return
	 */
	public static boolean checkFile(String filename,String str) {
		boolean isppt = false;
		String suffixname = null;
		if (filename != null && filename.indexOf(".") != -1) {
			suffixname = filename.substring(filename.indexOf("."));
			if (suffixname.equals("." + str)) {
				isppt = true;
			}
			return isppt;
		} else {
			return isppt;
		}
	}
}
