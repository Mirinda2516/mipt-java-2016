package ru.mipt.java2016.homework.g597.sigareva.task2;

import javafx.util.Pair;
import ru.mipt.java2016.homework.base.task2.KeyValueStorage;
import ru.mipt.java2016.homework.tests.task2.Student;
import ru.mipt.java2016.homework.tests.task2.StudentKey;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.CheckedInputStream;

/**
 * Created by 1 on 30.10.2016.
 */
public class KeyValueStorageImpl implements KeyValueStorage {

    private Boolean FileOpen = true;
    private File lockFile;

    KeyValueStorageImpl(String path, String keyType, String valueType) throws IOException {

        if (keyType == "Integer" && valueType == "Double"){
            Serialisator = new IntegerDoubleSerialisator(path);
            mapa = new HashMap<Integer, Double>();
        } else if (keyType == "String" && valueType == "String") {
            Serialisator = new StringStringSerialisator(path);
            mapa = new HashMap<String, String>();
        } else if (keyType == "StudentKey" && valueType == "Student") {
            Serialisator = new StudentSerialisator(path);
            mapa = new HashMap<StudentKey, Student>();
        }
        if(!Serialisator.goodFile){
            System.out.println("Lenin is sad");
        }
        else{
            Serialisator.CheckBeforeRead();
            while(true){
                try{
                    Pair currPair = Serialisator.read();
                    mapa.put(currPair.getKey(), currPair.getValue());
                }
                catch (IOException e){
                    if (e.getMessage() == "EOF"){
                        break;
                    }
                }
            }
        }
    }

ObjectSerialisator Serialisator;

    HashMap mapa;

    @Override
    public Object read(Object key) throws IOException {
        if (!FileOpen) {
            throw new IOException("Lenin is sad");
        }
        return mapa.get(key);
    }

    @Override
    public boolean exists(Object key) throws IOException {
        if (!FileOpen) {
            throw new IOException("Lenin is sad");
        }
        return mapa.containsKey(key);
    }

    @Override
    public void write(Object key, Object value) throws IOException {
        if (!FileOpen) {
            throw new IOException("Lenin is sad");
        }
        mapa.put(key, value);
    }

    @Override
    public void delete(Object key) throws IOException {
        if (!FileOpen) {
            throw new IOException("Lenin is sad");
        }
        mapa.remove(key);
    }

    @Override
    public Iterator readKeys() throws IOException {
        if (!FileOpen) {
            throw new IOException("Lenin is sad");
        }
        return mapa.keySet().iterator();
    }

    @Override
    public int size() throws IOException {
        if (!FileOpen) {
            throw new IOException("Lenin is sad");
        }
        return mapa.size();
    }

    @Override
    public void close() throws IOException {
        Serialisator.CheckBeforeWrite();

        Iterator currIter = mapa.entrySet().iterator();
        while(currIter.hasNext()){
            Map.Entry thisEntry = (Map.Entry) currIter.next();
            Serialisator.write(thisEntry.getKey(), thisEntry.getValue());
        }
        Serialisator.outputStream.close();
        FileOpen = false;
    }
}
