package com.csplice.log.collector.slf4j;

import org.apache.logging.log4j.MarkerManager;
import org.slf4j.Marker;

import java.util.Iterator;

/**
 * 类说明: slf4j日志标识对象<br/>
 *
 * @author huangyubin <br/>
 * @version 1.0
 * @date 2021/10/22 15:23<br/>
 * @since JDK 1.8
 */
public class CspliceLogMaker implements Marker {

    CspliceLogMaker(){}

    private String name ;
    CspliceLogMaker(String name ){
        this.name = name ;
    }
    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public void add(Marker marker) {

    }

    @Override
    public boolean remove(Marker marker) {
        return false;
    }

    /**
     * @deprecated
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public boolean hasReferences() {
        return false;
    }

    @Override
    public Iterator<Marker> iterator() {
        return null;
    }

    @Override
    public boolean contains(Marker marker) {
        return false;
    }

    @Override
    public boolean contains(String s) {
        return false;
    }
}
