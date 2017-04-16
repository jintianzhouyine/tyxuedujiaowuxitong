package com.example.mycomputer.tyxuedujiaowuxitong;

import java.util.List;

/**
 * Created by mycomputer on 2017/4/11.
 */

public class JXHTML {
    public static void JxHtml(String string, List<Course> courses){

        int index=0;
        String rule ="<td align=\"center\">";
        while(string.indexOf(rule,index)>0){
            Course course = new Course();
            if(string.indexOf(rule,index)>0){
                index=string.indexOf(rule,index)+22;
                course.setID(string.substring(index,string.indexOf("</td>",index)-7));
            }
            if(string.indexOf(rule,index)>0){
                index=string.indexOf(rule,index)+22;
                course.setID1(string.substring(index,string.indexOf("</td>",index)-6));
            }
            if(string.indexOf(rule,index)>0){
                index=string.indexOf(rule,index)+22;
                course.setName(string.substring(index,string.indexOf("</td>",index)-5));
            }
            if(string.indexOf(rule,index)>0){
                index=string.indexOf(rule,index)+22;
                course.setEnglishName(string.substring(index,string.indexOf("</td>",index)-7));
            }
            if(string.indexOf(rule,index)>0){
                index=string.indexOf(rule,index)+22;
                course.setCredit(string.substring(index,string.indexOf("</td>",index)-3));
            }
            if(string.indexOf(rule,index)>0){
                index=string.indexOf(rule,index)+22;
                course.setProperty(string.substring(index,string.indexOf("</td>",index)-6));
            }
            if(string.indexOf(rule,index)>0){
                index=string.indexOf(rule,index)+47;
                course.setScore(string.substring(string.indexOf("<p align=\"center\">",index)+18,string.indexOf("&nbsp;",index)));
            }
            courses.add(course);
        }
    }
}
