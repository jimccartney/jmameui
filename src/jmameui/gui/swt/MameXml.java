package jmameui.gui.swt;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import jmameui.mame.FileIO;

import org.w3c.dom.Element;

public class MameXml {

	public static void main(String[] args) {
//		try {
//			createGameFiles(FileIO.readFile(new File(
//					"/home/james/Desktop/MAME.xml")));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		HashMap<String, String> genres = FileIO.getSettingFileLines(new File("/home/james/Desktop/mamegenres"));
		System.out.println(genres.get("amidar"));
	}

	public static void createGameFiles(ArrayList<String> pOutput)
			throws IOException {
		File f = new File("/home/james/Desktop/mamegenres");
		if(!f.exists()){
			f.createNewFile();
		}
		PrintWriter pw = new PrintWriter(f);
		
		for (int i = 0; i < pOutput.size(); i++) {
			String a = pOutput.get(i);
			if (a.contains("game name")) {
				String name = a.split("=")[1].split(" ")[0].replace("\"", "");
				i += 6;
				name += "="
						+ pOutput.get(i).replace("<genre>", "")
								.replace("</genre>", "").replace("&apos;", "").replace("&amp; ", "").trim();
				pw.println(name);

			}
		}
		pw.flush();
		pw.close();
	}

	static String getElement(Element e, String tagName) {
		try {
			return e.getElementsByTagName(tagName).item(0).getTextContent();
		} catch (NullPointerException ex) {
			return "????";
		}
	}
}
