package com.thg.gdeaws.db;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;

public interface Dao<T, ID extends Serializable> {

    public abstract T findById(ID id);
    
    public abstract void save(T object);
    
    public abstract void delete(T object);
    
    public abstract void flush();
    
    List<T> findAllBy(String property, List<?> values);
    
    List<T> findAllBy(String property, List<?> values, Order order);
    
    List<T> findAllBy(String property, Object value);
    
    List<T> findAllBy(String property, Object value, Order order);
    
    List<T> findAll();
    
    List<T> findAll(Order order);
    
    List<T> findAll(Map<String, Object> values, Map<String, String> orders);
    
    List<T> findAll(Map<String, String> aliases, Map<String, Object> values, Map<String, String> orders);
    
    T findBy(String property, Object value);

}