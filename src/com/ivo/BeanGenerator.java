/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author benhur
 */
public class BeanGenerator {

    static String realpath = "";
    static String fpath = "";

    public static void main(String[] args) {

        try {

            String misteryName = JOptionPane.showInputDialog(
                    "Give me the qualified name of a class", null);

            Class c = Class.forName(misteryName);

            Class z = Serializable.class;
            Class supa_c = c.getSuperclass();

            Annotation[] ann_c = c.getAnnotations();
            Annotation[] ann_supa = supa_c.getAnnotations();

            boolean is_bean = false;

            for (Annotation an_c : ann_c) {
                if (an_c.annotationType().getSimpleName().toLowerCase().equals("bean")) {
                    is_bean = true;
                }
            }

            for (Annotation an_c : ann_supa) {
                if (an_c.annotationType().getSimpleName().toLowerCase().equals("bean")) {
                    is_bean = true;
                }
            }

            if (is_bean) {

                final File f = new File(c.getProtectionDomain().getCodeSource().getLocation().getPath());

                String path = f.getAbsolutePath().substring(0, f.getAbsolutePath().indexOf(File.separator + "build"));
                walk(path, c.getSimpleName(), c.getPackage().toString().substring(c.getPackage().toString().lastIndexOf(".") + 1));

                fpath = realpath.substring(0, realpath.lastIndexOf(File.separator)) + File.separator + c.getSimpleName() + "Bean.java";
                File f1 = new File(fpath);
                f1.delete();
                f1.createNewFile();

                p(c.getPackage().toString() + ";\n\n");
                
                
                p("public class " + c.getSimpleName() + "Bean implements " + z.getName() + "{\n\n");

                Field[] vars = c.getDeclaredFields();
                for (Field var : vars) {
                    if (!Modifier.isPublic(var.getModifiers())) {
                        var.setAccessible(true);
                    }

                    if (Modifier.isPublic(var.getModifiers())) {
                        p1("public ");
                    }
                    if (Modifier.isPrivate(var.getModifiers())) {
                        p1("private ");
                    }
                    if (Modifier.isProtected(var.getModifiers())) {
                        p1("protected ");
                    }
                    if (Modifier.isStatic(var.getModifiers())) {
                        p1("static ");
                    }
                    p(var.getType().getSimpleName() + " " + var.getName() + ";");
                }
                p("\n\n");

                for (Field var : vars) {
                    if (!Modifier.isPublic(var.getModifiers())) {
                        var.setAccessible(true);
                    }

                    String str = var.getName();
                    String nem = str.substring(0, 1).toUpperCase() + str.substring(1);
                    String retn = var.getType().getSimpleName();
                    
                    
                    boolean ck = false;
                    ck = confirmMtd(realpath, "set" + nem, ck);
                    if (ck == false) {
                        p("public void set" + nem + "(" + retn + " " + str + "){");
                        p("this." + str + "=" + str + ";");
                        p("}\n");
                    }
                    ck = confirmMtd(realpath, "set" + nem, ck);
                    if (ck == false) {
                        p("public " + retn + " get" + nem + "(" + retn + " " + str + "){");
                        p("return " + str + ";");
                        p("}\n");
                    }

                }

                //                            
                Method[] ms = c.getMethods();
                for (Method m : ms) {

                    getMtBody(realpath, m.getName());
                }

                p("}");
            } else {
                JOptionPane.showMessageDialog(null, "Bean annotation not found");
            }

        } catch (Exception ex) {
            Logger.getLogger(BeanGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void p(String s) {
        BufferedWriter bw = null;

        try {
            // APPEND MODE SET HERE
            bw = new BufferedWriter(new FileWriter(fpath, true));
            bw.write(s);
            bw.newLine();
            bw.flush();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {                       // always close the file
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception ioe2) {
                    // just ignore it
                }
            }
        } // end try/catch/finally

    } // end test()

    static void p1(String s) {
        BufferedWriter bw = null;

        try {
            // APPEND MODE SET HERE
            bw = new BufferedWriter(new FileWriter(fpath, true));
            bw.write(s);
            bw.flush();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {                       // always close the file
            if (bw != null) {
                try {
                    bw.close();
                } catch (Exception ioe2) {
                    // just ignore it
                }
            }
        }
    }

    public static String walk(String path, String cls, String pkg) {

        File root = new File(path);
        File[] list = root.listFiles();
        String s = "";

        for (File f : list) {
            if (f.isDirectory() && f.getAbsoluteFile().toString().contains("src")) {
                s = walk(f.getAbsolutePath(), cls, pkg);
            } else {
                if (f.getAbsoluteFile().toString().contains(cls + ".java") && f.getAbsoluteFile().toString().contains(pkg)) {
                    if (f.getAbsoluteFile().toString().length() > 0 && s.length() < 1) {
                        realpath = f.getAbsoluteFile().toString();

                    }
                }

            }
        }
        return s;
    }

    static void getMtBody(String pth, String sach) {
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(pth));
            String line = "";
            int cnt = 0;
            while ((line = bReader.readLine()) != null) {
                if (line.contains(sach)) {
                    if (line.contains("{")) {
                        cnt++;
                    }
                    p(line);
                } else {
                    if (cnt > 0) {
                        if (line.contains("{")) {
                            cnt++;
                        } else if (line.contains("}")) {
                            cnt--;
                        }
                        p(line);
                    }
                }
            }
        } catch (Exception e) {
            //handle this
        }
    }

    static boolean confirmMtd(String pth, String sach, boolean chk) {
        try {
            BufferedReader bReader = new BufferedReader(new FileReader(pth));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                if (line.contains(sach)) {
                    chk = true;
                }
            }
        } catch (Exception e) {
            //handle this
        }
        return chk;
    }
}
