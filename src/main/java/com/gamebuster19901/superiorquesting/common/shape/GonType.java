package com.gamebuster19901.superiorquesting.common.shape;

public enum GonType {
	PENTA(5),
	HEXA(6),
	SEPTA(7),
	OCTA(8),
	NONA(9),
	DECA(10),
	UNDECA(11),
	DODECA(12);
	
	private int intVal;
	
	private GonType(int i) {
		intVal = i;
	}
	
	public String getGonType(int sides) {
		if(sides > 4 && sides < 13) {
			return (GonType.values()[sides - 5] + "gon").toLowerCase();
		}
		return sides + "-gon";
	}
	
	public String toString() {
		return this.toString().toLowerCase();
	}
}
