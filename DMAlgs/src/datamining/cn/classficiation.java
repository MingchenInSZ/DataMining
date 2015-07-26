package datamining.cn;
import java.io.*;
import java.util.*;
import java.lang.Math;
public class classficiation {
	//static ArrayList<attrInfo> arl;
	static String [] head=new String [100];
	static int len;
	static String cl="";
	static String parent="";
	//static  ArrayList<attrInfo> fileCon;
public static void main(String [] args)
   {
	readFile("weather.csv",true);
	
	String str="0,1,2,3,4,5,6,7,8,9,10,11,12,13,14";
	
	ArrayList<attrInfo> arl=getFileCon(convertToIntArr(str,","),4);
	printDetailInfo(arl);
	//System.out.println("@outlook".indexOf('@'));
	System.out.println("---------------------------------------------------");

	int [] arr={0,1,2,3};
	printToFile("tree.out","This is the root. ");
	//printToDot("E:\\Postcourse\\DataMining\\exp1\\tree.out","digraph  G {\n");
	C45Alg(arl,4,arr);

	//printToDot("tree.out","tree.dot");
	
   }
public static String arrSplit(ArrayList<attrInfo> arl,int col,int tag)
{
	String rs="";
	attrInfo atf=arl.get(col);
	for(int i=0;i<atf.colist.size();i++)
	{
		  if(i==tag)
			  rs=rs.concat(atf.colist.get(i).ids);
	}
	return(rs);
}
public static void C45Alg(ArrayList<attrInfo> arl,int label,int [] arr)
{
	String remLabel=eraseLast(getRemLabel(arl,label),',');
	if(isPure(convertToIntArr(remLabel,","),label))
	{
	    String content="[LEAF]"+cl+System.getProperty("line.separator");
	  
	    printToFile("tree.out",content);
		return;
	}
	double [] drr=new double[arr.length];
	for(int i=0;i<arr.length;i++)
	{
		if(arr[i]!=-1)
		{
			drr[i]=gainRatio(arl,i);
		}
		else
		{
			drr[i]=-1.0;
		}
	}

	int tag=max(drr);
	if(drr[tag]!=-1){

	arr[tag]=-1;
	for(int j=0;j<arl.get(tag).colist.size();j++)
	{  
		String content="";
		content=content.concat("@"+arl.get(tag).attrName+System.getProperty("line.separator"));

	
		String records=arrSplit(arl,tag,j);
		content=content.concat("["+arl.get(tag).attrName+"="+arl.get(tag).colist.get(j).value+j+":"+eraseLast(records,',')+"]"+System.getProperty("line.separator"));
		printToFile("tree.out",content);
		
		C45Alg(getFileCon(convertToIntArr(records,","),label),label,arr);
	}
	}
}

public static void printToFile(String file,String content)
{
	BufferedWriter out = null;     
    try {     
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));     
        out.write(content);     
    } catch (Exception e) {     
        e.printStackTrace();     
    } finally {     
        try {     
            if(out != null){  
                out.close();     
            }  
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
    }     

}
public static void printToDot(String infile,String outfile)
{
	String content="digraph G {"+System.getProperty("line.separator");
	String label="";
	BufferedWriter out = null;
	BufferedReader in=null;
    try {     
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile, true))); 
        out.write(content);
        in=new BufferedReader(new FileReader(new File(infile)));
        String line=in.readLine();
        int tag=0;
        String temp="";
        while(line!=null &&line!="")
        {
        	
        	if(line.indexOf('@')>=0)
        	{
        		tag++;
        		if(tag>1)
        		{
        		String tp=line.substring(line.indexOf('@')+1);
        		//temp=;
        		out.write(temp.concat("->"+tp+"[label=\""+label+"\"];"+System.getProperty("line.separator")));
        		out.write(temp+"[shape=box];"+System.getProperty("line.separator")+tp+"[shape=box];"+System.getProperty("line.separator"));
        		tag=0;
        		}
        		temp=line.substring(line.indexOf('@')+1);
        		
        	}else if(line.indexOf("[LEAF]")>=0)
        	{
        		String tp=line.substring(line.indexOf(']')+1);
        		String cont=temp.concat("->"+tp).concat("[label=\""+label+"\"];"+System.getProperty("line.separator"));
        		out.write(cont);
        		out.write(temp+"[shape=box];"+System.getProperty("line.separator")+tp+"[shape=ellipse];"+System.getProperty("line.separator"));

        		tag=0;
        	}else
        	{
        		label=line.substring(1,line.indexOf(":")-1);
        	}
        line=in.readLine();
        }
        //out.write(content);     
    } catch (Exception e) {     
        e.printStackTrace();     
    } finally {     
        try {  
        	in.close();
        	out.write(System.getProperty("line.separator")+"}");
            if(out != null){  
                out.close();     
            }  
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
    }     
}
public static void printDetailInfo(ArrayList<attrInfo> arl)
{
	for(attrInfo ai:arl)
	{
		System.out.println("@"+ai.attrName);
		for(colInfo col:ai.colist)
		{
			System.out.println(col.getValue()+"-----"+col.getCount()+"-----"+col.ids);
			System.out.print(" === "+col.box.size()+": ");
			for(block b:col.box)
			{
				System.out.print(b.vl+","+b.n+"\t");
			}
			System.out.println();
		}
	}
}
public static void getFileCon(int label)
{
	ArrayList<attrInfo>arl=new ArrayList<attrInfo>();
	for(int i=0;i<len-1;i++)
	{
		String attrN=head[i].substring(0,head[i].indexOf(':'));
		attrInfo aif=new attrInfo(attrN);
		aif.colist=getColInfo(i,label);
		if(!arl.contains(aif))
		{
			arl.add(aif);
		}
	}
}
public static ArrayList<attrInfo> getFileCon(int [] la,int label)
{
	ArrayList<attrInfo>arl=new ArrayList<attrInfo>();
	for(int i=0;i<len;i++)
	{
		String attrN=head[i].substring(0,head[i].indexOf(':'));
		attrInfo aif=new attrInfo(attrN);
		aif.colist=getColInfo(la,i,label);
		if(!arl.contains(aif))
		{
			arl.add(aif);
		}
	}
	return(arl);
}
public static String getRemLabel(ArrayList<attrInfo> arl,int label)
{
	String rs="";
	 attrInfo atf=arl.get(label);
	 for(colInfo ci:atf.colist)
	 {
		 rs=rs.concat(ci.ids);
	 }
	return(rs);
}
public static ArrayList<colInfo>getColInfo(int col,int label)
{
	ArrayList<colInfo> al=new ArrayList<colInfo>();
	//ArrayList<String>bl=null;
	
	String arr[]=head[col].split(":");
	String brr[]=head[label].split(":");
	//String attr1=arr[0];
	//String attr2=brr[0];
	String indetail=arr[1];
	String outdetail=brr[1];
	int id=0;
	for(String tmp:indetail.split(","))
	{
		colInfo cinfo=new colInfo(tmp);
		
		int pil=0;
		if(al.isEmpty())
		{
		cinfo.count++;
		cinfo.ids=cinfo.ids.concat(Integer.toString(id)+",");
		al.add(cinfo);
		id++;
		continue;
		}
		for(colInfo ci:al)
		{
			 
			  if(ci.getValue().equals(tmp))
			  {
				  al.get(pil).count++;
				  al.get(pil).ids= al.get(pil).ids.concat(Integer.toString(id)+",");
				  //System.out.println("In the first for loop "+al.get(pil).getValue()+"------"+al.get(pil).getCount());
				  break;
			  }
			  pil++;
		}
		if(pil==al.size())
		  {
			  cinfo.count++;
			  cinfo.ids=cinfo.ids.concat(Integer.toString(id)+",");
			  al.add(cinfo);
		  }
		id++;
	}
	String out[]=outdetail.split(",");
	String in[]=indetail.split(",");
	for(int i=0;i<out.length;i++)
	{
		block b=new block(out[i]);
		for(colInfo c:al)
		{
			if(c.getValue().equals(in[i]))
			{
				 if(c.box.isEmpty())
				 {
					 b.n++;
					 c.box.add(b);
					 break;
				 }else{
					 
					 int tp=0;
					 for(block k:c.box)
					 {
						 
						 if(k.vl.equals(out[i]))
						 {
							 k.n++;
							 break;
						 }
						 tp++;
					 }
					 if(tp==c.box.size())
					 {
						 b.n++;
						 c.box.add(b);
						 break;
					 }
				 }
			}
		}
	}
	
	
	return(al);
}
public static ArrayList<colInfo>getColInfo(int [] la,int col,int label)
{
	ArrayList<colInfo> al=new ArrayList<colInfo>();
	//ArrayList<String>bl=null;
	
	String arr[]=head[col].split(":");
	String brr[]=head[label].split(":");
	//String attr1=arr[0];
	//String attr2=brr[0];
	String indetail=arr[1];
	String outdetail=brr[1];
	int id=0;
	for(String tmp:indetail.split(","))
	{
		colInfo cinfo=new colInfo(tmp);
		if(haveIn(la,id)){
		int pil=0;
		if(al.isEmpty())
		{
		cinfo.count++;
		cinfo.ids=cinfo.ids.concat(Integer.toString(id)+",");
		al.add(cinfo);
		id++;
		continue;
		}
		for(colInfo ci:al)
		{
			 
			  if(ci.getValue().equals(tmp))
			  {
				  al.get(pil).count++;
				  al.get(pil).ids= al.get(pil).ids.concat(Integer.toString(id)+",");
				  //System.out.println("In the first for loop "+al.get(pil).getValue()+"------"+al.get(pil).getCount());
				  break;
			  }
			  pil++;
		}
		if(pil==al.size())
		  {
			  cinfo.count++;
			  cinfo.ids=cinfo.ids.concat(Integer.toString(id)+",");
			  al.add(cinfo);
		  }
		id++;
	}else{
		id++;
	}
	}
	String out[]=outdetail.split(",");
	String in[]=indetail.split(",");
	for(int i=0;i<out.length;i++)
	{
		block b=new block(out[i]);
		if(haveIn(la,i)){
		for(colInfo c:al)
		{
			if(c.getValue().equals(in[i]))
			{
				 if(c.box.isEmpty())
				 {
					 b.n++;
					 c.box.add(b);
					 break;
				 }else{
					 
					 int tp=0;
					 for(block k:c.box)
					 {
						 
						 if(k.vl.equals(out[i]))
						 {
							 k.n++;
							 break;
						 }
						 tp++;
					 }
					 if(tp==c.box.size())
					 {
						 b.n++;
						 c.box.add(b);
						 break;
					 }
				 }
			}
		}
	 }
	}
	
	
	return(al);
}
public static boolean isPure(int [] arr,int label)
{
	String target=head[label].substring(head[label].indexOf(':')+1);
	String [] a=target.split(",");
	String tmp=a[arr[0]];
	for(int i=1;i<a.length;i++)
	{
		if(haveIn(arr,i)){
			
			if(!a[i].equals(tmp))
				return(false);
		}
		
	}
	cl=tmp;
	return(true);
}
public static boolean haveIn(int [] arr,int tag)
{
	for(int i=0;i<arr.length;i++)
	{
		if(arr[i]==tag)
			return(true);
	}
	return(false);
}

public static int [] convertToIntArr(String str,String sep)
{
	String [] arr=str.split(sep);
	int [] brr=new int[arr.length];
	for(int i=0;i<arr.length;i++)
	{
		if(arr[i]!=""){
		brr[i]=Integer.parseInt(arr[i]);
		}
	}
	return(brr);
}

public static  void readFile(String fileName,boolean title)
{
	try{
		BufferedReader br =new BufferedReader(new FileReader(new File(fileName)));
		String line=br.readLine();
		while(line!=null)
		{  
			
			String arr[]=line.split(",");
			if(title==true)
			{
				len=0;
				for(String tmp:arr)
				{
					head[len++]=tmp.concat(":");
				}
				title=false;
				line=br.readLine();
				continue;
				
			}
			//System.out.println("---------------"+line);
			int i=0;
			for(String tmp:arr)
			{		
				
				head[i]=head[i++].concat(tmp+",");
				
				
			}
			line=br.readLine();
		}
		for(int i=0;i<len;i++)
		{
			
			head[i]=eraseLast(head[i],',');
		}
		br.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
public static String eraseLast(String input,char tag)
{
	int index=input.lastIndexOf(tag);
	String rs=input.substring(0, index);
	return(rs);
}
public static String eraseLast(String input,String tag)
{
	int index=input.lastIndexOf(tag);
	String rs=input.substring(0, index);
	return(rs);
}
public static double[] computeSplitInfo(ArrayList<attrInfo>al,int col,int label)
{
	double rs=0.0;
	
	int tp=0;
        attrInfo at=al.get(col);
        double [] darr=new double[at.colist.size()];
		for(colInfo cf:at.colist)
		{
			int [] tempa=new int[cf.box.size()];
			for(int i=0;i<tempa.length;i++)
			{
				tempa[i]=cf.box.get(i).n;
			}
			double sum=sum(tempa);
			rs=0.0;
			for(int i=0;i<tempa.length;i++)
			{
				double temp=-(tempa[i]/(double)sum)*log2(tempa[i]/(double)sum);
				
				rs+=temp;
			}
			darr[tp++]=rs;
		}
	
	return(darr);
}
public static double orgGain(ArrayList<attrInfo> al,int label)
{
	double rs=0.0;
	attrInfo ai=al.get(label);
	int  total=0,i=0;
	int [] arr=new int[ai.colist.size()];
	for(colInfo c:ai.colist)
	{
		arr[i++]=c.count;
	    total+=c.count;	
	}
	for(i=0;i<arr.length;i++)
	{
		rs+=-(arr[i]/(double)total)*log2(arr[i]/(double)total);
	}
	return(rs);
}
public static double  gainRatio(ArrayList<attrInfo> al,int col)
{
	double [] brr=computeSplitInfo(al,col,0);
	
	double tEn=orgGain(al,4);
	attrInfo ati=al.get(col);
	int [] arr=new int[ati.colist.size()];
	int i=0;
	for(colInfo ci:ati.colist)
	{
		arr[i++]=ci.count;
	}
	float sum=sum(arr);
	for(i=0;i<arr.length;i++)
	{
		brr[i]=(arr[i]/(double)sum)*brr[i];
	}
	double res=sum(brr);
	  res=tEn-res;
	  //System.out.println("En---"+entropy(al,col));
	  res=res/entropy(al,col);
	return(res);
}
public static double entropy(ArrayList<attrInfo> al,int col) 
{
	double rs=0.0;
	attrInfo atf=al.get(col);
	int [] temp=new int[atf.colist.size()];
	int i=0;
	for(colInfo ci:atf.colist)
	{
		temp[i++]=ci.count;
		//System.out.print(ci.count+":::");
	}
	float sum=sum(temp);
	//System.out.println("sum--"+sum);
	for(i=0;i<temp.length;i++)
	{
		
		rs+=-(temp[i]/(double)sum)*log2(temp[i]/(double)sum);
	}
	return(rs);
}
public static double log2(double f)
{
	double rs=0.0f;
	rs=Math.log(f)/Math.log(2.0);
	return(rs);
}
public static float sum(int [] arr)
{
	float rs=0.0f;
	for(int i=0;i<arr.length;i++)
		rs+=arr[i];
	return(rs);
}
public static double sum(double [] arr)
{
	double rs=0.0;
	for(int i=0;i<arr.length;i++)
		rs+=arr[i];
	return(rs);
}
public static int min(double [] arr)
{
	int  m=0;
	for(int i=1;i<arr.length;i++)
	{
		if((arr[i]<1) &&(arr[m]>arr[i]))
			m=i;
	}
	return(m);
}
public static int max(double [] arr)
{
	int  m=0;
	for(int i=1;i<arr.length;i++)
	{
		if( arr[m]<arr[i])
			m=i;
	}
	return(m);

}
}
