package ru.sbrf.trade.data.da.entity.ch;

public class MoexDto {

    public String boardid;
    public String tradedate;
    public String shortname;
    public String secid;
    public Double numtrades;
    public Double value;
    public Double open;
    public Double low;
    public Double high;
    public Double legalcloseprice;
    public Double waprice;
    public Double close;
    public Double volume;
    public Double marketprice2;
    public Double marketprice3;
    public Double admittedquote;
    public Double mp2VALTRD;
    public Double marketprice3TRADESVALUE;
    public Double admittedvalue;
    public Double waval;
    public Integer tradingsession;

    @Override
    public String toString() {
        return "MoexDto{" +
                "boardid='" + boardid + '\'' +
                ", tradedate='" + tradedate + '\'' +
                ", shortname='" + shortname + '\'' +
                ", secid='" + secid + '\'' +
                ", numtrades=" + numtrades +
                ", value=" + value +
                ", open=" + open +
                ", low=" + low +
                ", high=" + high +
                ", legalcloseprice=" + legalcloseprice +
                ", waprice=" + waprice +
                ", close=" + close +
                ", volume=" + volume +
                ", marketprice2=" + marketprice2 +
                ", marketprice3=" + marketprice3 +
                ", admittedquote=" + admittedquote +
                ", mp2VALTRD=" + mp2VALTRD +
                ", marketprice3TRADESVALUE=" + marketprice3TRADESVALUE +
                ", admittedvalue=" + admittedvalue +
                ", waval=" + waval +
                ", tradingsession=" + tradingsession +
                '}';
    }
}
