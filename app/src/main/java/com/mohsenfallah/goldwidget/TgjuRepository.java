package com.mohsenfallah.goldwidget;
import java.io.*; import java.net.*; import java.nio.charset.StandardCharsets; import java.util.regex.*;
public class TgjuRepository {
 private static final String[] HOSTS={"https://www.tgju.org/profile/","https://gem.tgju.org/profile/"};
 private static final Pattern CURRENT=Pattern.compile("نرخ\\s*فعلی\\s*::?\\s*([0-9,]+(?:\\.[0-9]+)?)");
 private static final Pattern RIAL=Pattern.compile("قیمت\\s*ریالی\\s*:?\\s*([0-9,]+)");
 public static class Data{double gold18,gold24,coin,dollar,tether,ounce,silver;}
 public static Data fetch() throws Exception{
  Data d=new Data();
  d.gold18=parse("geram18"); d.gold24=parse("geram24"); d.coin=parse("sekee");
  d.dollar=parse("price_dollar_rl"); d.ounce=parse("ons"); d.silver=parse("silver");
  Matcher m=RIAL.matcher(strip(get("crypto-tether"))); if(!m.find()) throw new IllegalStateException("Tether not found"); d.tether=num(m.group(1));
  d.gold18/=10; d.gold24/=10; d.coin/=10; d.dollar/=10; d.tether/=10; return d;
 }
 private static double parse(String p)throws Exception{String h=strip(get(p)); Matcher m=CURRENT.matcher(h); if(!m.find()) throw new IllegalStateException("Price not found: "+p); return num(m.group(1));}
 private static String get(String p)throws Exception{Exception last=null; for(String host:HOSTS){try{return request(host+p); }catch(Exception e){last=e;}} throw last==null?new IOException("network"):last;}
 private static String request(String u)throws Exception{HttpURLConnection c=(HttpURLConnection)new URL(u).openConnection(); c.setInstanceFollowRedirects(true); c.setConnectTimeout(15000); c.setReadTimeout(15000); c.setRequestMethod("GET"); c.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 Chrome/120 Mobile Safari/537.36"); c.setRequestProperty("Accept","text/html,application/xhtml+xml"); c.setRequestProperty("Accept-Language","fa-IR,fa;q=0.9,en;q=0.8"); c.setRequestProperty("Referer","https://www.tgju.org/"); try{int code=c.getResponseCode(); if(code<200||code>=300)throw new IOException("HTTP "+code); BufferedReader b=new BufferedReader(new InputStreamReader(c.getInputStream(),StandardCharsets.UTF_8)); StringBuilder s=new StringBuilder(); String l; while((l=b.readLine())!=null)s.append(l).append('\n'); b.close(); return s.toString();}finally{c.disconnect();}}
 private static String strip(String h){return h.replaceAll("(?is)<script.*?</script>"," ").replaceAll("(?is)<style.*?</style>"," ").replaceAll("(?s)<[^>]*>"," ").replace("&nbsp;"," ").replace("&#160;"," ").replaceAll("\\s+"," ").trim();}
 private static double num(String s){return Double.parseDouble(s.replace(",","").trim());}
}