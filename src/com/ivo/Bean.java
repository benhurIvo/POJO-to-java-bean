/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivo;

/**
 *
 * @author benhur
 */
import java.lang.annotation.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
//@Target(ElementType.CONSTRUCTOR) //can use in method only.
public @interface Bean {
    
}
