package com.gamebuster19901.superiorquesting.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface UpdatableSerializable extends Serializable{
	public void convert(int prevVersion, int nextVersion, ObjectInputStream in);
	
	public void writeObject(ObjectOutputStream out) throws IOException;
	
	public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException;
	
}
