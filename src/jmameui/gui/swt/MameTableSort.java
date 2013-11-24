package jmameui.gui.swt;

import java.util.Comparator;

import jmameui.mame.MameRom;

public class MameTableSort {
	private String columnName = "";
	
	private Comparator<MameRom> forwardSort = new Comparator<MameRom>() {
		@Override
		public int compare(MameRom o1, MameRom o2) {
			String s1 = getString(o1);
			String s2 = getString(o2);
			
			return s1.compareTo(s2);
		}
	};
	private Comparator<MameRom> reverseSort = new Comparator<MameRom>() {
		@Override
		public int compare(MameRom o1, MameRom o2) {
			String s1 = getString(o1);
			String s2 = getString(o2);
			
			return s2.compareTo(s1);
		}
	};

	private String getString(MameRom m) {
		if (columnName.equals("Game Name")) {
			return m.getName();
		} else if (columnName.equals("Description")) {
			return m.getDescription();
		} else if (columnName.equals("Manufacturer")) {
			return m.getManufacturer();
		} else if (columnName.equals("Year")) {
			return m.getYear();
		} else if (columnName.equals("Emulation state")) {
			return m.getEmuState();
		} else if (columnName.equals("Mame version")) {
			return m.getMameVersion();
		} else if (columnName.equals("Genre")) {
			return m.getGenre();
		}
		return "";
	}

	public Comparator<MameRom> getForwardSort() {
		return forwardSort;
	}

	public Comparator<MameRom> getReverseSort() {
		return reverseSort;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
