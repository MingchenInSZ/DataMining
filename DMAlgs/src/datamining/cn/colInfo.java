package datamining.cn;
import java.util.*;
public class colInfo {
	String value;
	int count;
	String ids;
	ArrayList<block> box;
	public colInfo(String value,int count,String ids)
	{
		this.value=value;
		this.count=count;
		this.ids=ids;
	}
	public colInfo(String value)
	{
		this.value=value;
		this.count=0;
		this.ids="";
		this.box=new ArrayList<block>();
	}
	public colInfo()
	{

	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	

}
