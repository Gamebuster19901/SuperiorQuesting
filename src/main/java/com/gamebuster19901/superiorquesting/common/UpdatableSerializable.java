package com.gamebuster19901.superiorquesting.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public interface UpdatableSerializable extends Serializable{
	public void convert(int prevVersion, int nextVersion, ObjectInputStream in);
	
	public default void writeObject(ObjectOutputStream out) throws IOException{
		out.defaultWriteObject();
	}
	public default void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
	}
}
