package com.siteview.ecc.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

public class AutoListbox<T> {  
	   
    private final static String HEADER_TO_COL_SPLIT="_";  
      
    private int PAGE_SIZE=10;  
      
    /*databean.class*/  
    private Class<T> _clazz;  
      
    /*_clazz.Fields*/  
    private Map<String, Field> _beanFieldMap;  
  
    /* 需要显示的结果集 */  
    private List<T> _lstrs;  
      
    private Listbox _listbox;  
      
    private Paging _paging;  
      
    private Listhead _listhead;  
      
      
      
    /*执行查询的类和方法*/  
    private Object objQueryClazz;  
    private Method objQueryMethod;  
    private Integer _totalrows=0;  
      
      
    public AutoListbox(Class<T> clazz, Component comp){  
        super();  
        this._clazz=clazz;  
        
        
        
        this._listbox=new Listbox();  
        this._listhead=new Listhead();  
        this._paging=new Paging();                          ///new Paging();  
        
        
          
          
        this._listbox.setId("MyAutoListbox");  
        this._listbox.setWidth("99%");  
        this._listbox.setHeight("75%");  
        this._listbox.setVisible(true);  
  
        this._listhead.setSizable(true);  
          
        this._listhead.setParent(this._listbox);  
          
        this._paging.setPageSize(PAGE_SIZE);  
        this._paging.setTotalSize(this._totalrows);  
        this._paging.setDetailed(true);                       //显示记录数  
        this._paging.setWidth("99%");  
        this._listbox.setPaginal(this._paging);  
        this._paging.setDetailed(true);  
        this._paging.setParent(this._listbox);  
        addPagingListener();  
          
          
        this._listbox.setParent(comp);  
        this._paging.setParent(comp);  
    }  
       
      
  
    private void addPagingListener(){  
          
        this._paging.addEventListener("onPaging", new EventListener(){  
              
            @Override  
            public void onEvent(Event event) throws Exception {  
                PagingEvent pevt=(PagingEvent) event;  
                int pagesize=PAGE_SIZE;  
                int pgno =pevt.getActivePage();;  
                int ofs = pgno * pagesize;  
                redraw(ofs+1,ofs+pagesize);  
            }  
                  
            });  
    }  
      
      
    private void redraw(Integer noFrom,Integer onTo){  
        //int rowCount=cityservice.findCount("");  
        //List<TCity> list=cityservice.findByOption(1, 20);  
        //method.invoke(a, new Object[]{"world", new Integer(5)});  
 
        //this._listbox.setWidth("99%");  
        //this._paging.setWidth("100%");  
        this._paging.setAutohide(false);  
        this._paging.setVisible(true);  
        try {  
            this._paging.setTotalSize(this._totalrows);  
            List<T> list = (List<T>) objQueryMethod.invoke(objQueryClazz, new Object[]{noFrom,onTo});  
            loadList(list);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    private void loadList(List<T> list){  
        //读每行记录，构造Listitem，构造每列Listcell  
    }  
  
    public void beginQuery(Integer rowCount, Object objQueryClazz,  
         Method objQueryMethod) {  
          this._totalrows=rowCount;  
          this._paging.setTotalSize(this._totalrows);  
          //this._paging.setTotalSize(rowCount);  
          this.objQueryClazz=objQueryClazz;  
          this.objQueryMethod=objQueryMethod;  
          redraw(0, this.PAGE_SIZE);  
    }  
}  