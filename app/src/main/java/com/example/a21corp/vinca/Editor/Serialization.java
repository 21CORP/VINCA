package com.example.a21corp.vinca.Editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serialization {

  public static void save(Serializable obj) throws IOException {
    ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(new File("/sdcard/save_object.bin")));
    oos.writeObject(obj);
    oos.close();
  }

  public static Serializable load(String fileName) throws Exception {
    ObjectInputStream oos = new ObjectInputStream(
            new FileInputStream((new File("/sdcard/save_object.bin"))));
    Object obj = oos.readObject();
    oos.close();
    return (Serializable) obj;
  }
}