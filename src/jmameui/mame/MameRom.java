/*
 * This file is part of JMameUI.
 * 
 * JMameUI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * JMameUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with JMameUI.  If not, see <http://www.gnu.org/licenses/>.
 */
package jmameui.mame;


public class MameRom implements Comparable<MameRom>{
	private String name = "";
	private String MamePath = "";
	private boolean goodRom = false;
	private String year = "";
	private String manufacturer = "";
	private String description = "";
	private String emuState = "";
	private boolean favourite = false;
	private String mameVersion;
	
	public MameRom(String name,String mPath, boolean gr){
		this.name = name;
		this.MamePath = mPath;
		this.goodRom = gr;
	}
	
	public MameRom(String name){
	  this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getMamePath() {
		return MamePath;
	}
	
	public void setMamePath(String mameVersion) {
		MamePath = mameVersion;
	}
	
	public void setMameVersion(String mameVersion) {
		this.mameVersion = mameVersion;
	}
	public String getMameVersion() {
		return mameVersion;
	}


	public boolean isGoodRom() {
		return goodRom;
	}

	public void setGoodRom(boolean goodRom) {
		this.goodRom = goodRom;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getEmuState() {
		return emuState;
	}

	public void setEmuState(String emuState) {
		this.emuState = emuState;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MameRom){
			if(name == ((MameRom)obj).getName()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "MameRom [name=" + name + "]";
	}

	@Override
	public int compareTo(MameRom o) {
		return name.compareTo(o.getName());
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}
	
	
}
