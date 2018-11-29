/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xrfslave;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 *
 * @author jonat
 */
public class XHTMLConvert {
    public static String convertToXHTML (String html){
        Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);    
        document.outputSettings().charset("UTF-8");
        return document.toString();
    }
}
