/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tc.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author thinhnt
 */
public class URLParser {

    private Set<String> componentSelector;

    public URLParser() {
        componentSelector = new HashSet();
    }

    public String parse(String url) throws IOException {
        String rs = null;
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        rs = getContent(doc);
        return rs;
    }

    protected String getContent(org.jsoup.nodes.Document doc) {
        StringBuilder builder = new StringBuilder();
        for (String next : componentSelector) {
            Elements es = doc.select(next);
            es.stream().forEach((e) -> {
                builder.append(e.text());
            });
        }
        return new String(builder);
    }
    
    public void addSelector(String selector){
        componentSelector.add(selector);
    }

    public static void main(String[] args) throws IOException {
//        URLParser vnExpParser = new URLParser();
//        vnExpParser.addSelector("#left_calculator");
//                String rs = vnExpParser.parse("http://kinhdoanh.vnexpress.net/tin-tuc/bat-dong-san/gan-1-000-nguoi-du-le-mo-ban-nha-lien-ke-khu-do-thi-van-phuc-3508463.html");
//        System.out.println(rs);
        URLParser vnExpParser = new URLParser();
        vnExpParser.addSelector(".content");
        vnExpParser.addSelector(".details-content.article-content");
                String rs = vnExpParser.parse("http://thethao.thanhnien.vn/bong-da-viet-nam/hlv-huu-thang-xai-phi-cong-phuong-van-toan-70397.html");
        System.out.println(rs);

    }
}
