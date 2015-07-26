package datamining.cn;
import java.util.*;
public class attrInfo {
	String attrName;
	String tag;
	ArrayList<colInfo> colist;
	public attrInfo()
	{
		
	}
	public attrInfo(String attrName)
	{
	this.attrName=attrName;
	this.colist=null;
	}
	public attrInfo(String attrName,colInfo coin)
	{
		this.attrName=attrName;
		if(!this.colist.contains(coin))
		{
			this.colist.add(coin);
		}
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public ArrayList<colInfo> getColist() {
		return colist;
	}
	public void setColist(ArrayList<colInfo> colist) {
		this.colist = colist;
	}
	

}
