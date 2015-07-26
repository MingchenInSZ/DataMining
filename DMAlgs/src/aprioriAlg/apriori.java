package aprioriAlg;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
public class apriori {
	static ArrayList<String> asl=new ArrayList<String>();
	static ArrayList<Node> last=new ArrayList<Node>();
    static ArrayList<rule> ruleSet=new ArrayList<rule>();
    static ArrayList<Node> form=new ArrayList<Node>();
  public static void main(String [] args) throws Exception
  {
	  readFile("in.txt");//The dataset is restored in the file in.txt
	  
	  form=findCandFirst(0.2,0.8);//Find the first frequent items ;And the two parameter are minsup and minconf accordingly
	  /**
	   * Find all the frequent items on the basis of first frequent items
	   * And all the rules is generated simultaneously 
	   */
	  findCandiate(form,0.2);
	 /*
	  * The following are printing the result
	  * 	  
	  * */
	 
	  printDetails();
	  DecimalFormat df=new DecimalFormat("0.000");
	  System.out.println("Association Rules are: ");
	  for(rule r:ruleSet)
	  {
		  System.out.println("Rule  "+r.head+"-->"+r.tail+"   conf: "+df.format(r.getConf()));
	  }
	  System.out.println("Enter any input to terminate!");
	 int d=System.in.read();
	 
  }
/**
 * Read all the records to a certain dataset
 * @param file
 */
  public static void readFile(String file)
  {
	  try{
		 BufferedReader br=new BufferedReader(new FileReader(new File(file)));
		 String line=br.readLine();
		  line=br.readLine();
		 while(line!=null)
		 {
			 
			 String rs="";
			 String [] arr=line.split("\t");
			 for(int i=1;i<arr.length;i++)
			 {
				 if(arr[i].indexOf("1")>=0)
				 {
				  rs=rs.concat(Integer.toString(i)+" "); 
				 }
				 
			 }
			 rs=rs.trim();
			 if(!asl.contains(rs))
			 {
				 asl.add(rs);
			 }
			 line=br.readLine();
		 }
		
		 br.close();
	  }catch(Exception e)
	  {
		  e.printStackTrace();
	  }
  }
/**
 * This function is used to find the first frequent items 
 * @param minsup-----predefined minimum support
 * @param minconf----predefined minimun confidence
 * @return  first frequent items
 */
 public static ArrayList<Node> findCandFirst(double minsup,double minconf)
 {
	 ArrayList<Node> arr=new ArrayList<Node>();
	 for(String tmp:asl)
	 {
		 String [] strArr=tmp.split(" ");
		 for(int i=0;i<strArr.length;i++)
		 {
			 Node node=new Node(strArr[i]);
			 node.addCount();
			 Node n;
			 if((n=contains(arr, node))!=null)
			 {
				 n.addCount();
			 }else{
				 arr.add(node);
			 }
		 }
	 }
	arrSort(arr);
	prune(arr,0.2,0.8);
	last.addAll(arr);

	return(arr);
 }
 /**
  * Find all the candidates not less than the minsup and then generate all the effective rules
  * @param list
  * @param minsup
  * @return
  */
 public static ArrayList<Node> findCandiate(ArrayList<Node> list,double minsup)
 {
	 int len=list.size();
	 if(len==0)
		 return(null);
	/* for(Node node:list)
		{
			System.out.println(node.getName()+"--"+node.getCount());
		}*/
	 ArrayList<Node> tmp=aprioriGen(list);
	 prune(tmp,0.2,0.8);
	 last.addAll(tmp);
	 apGenRules(tmp,0.8);
	 form=tmp;
	 return(findCandiate(tmp,minsup));
 }
 public static Node contains(ArrayList<Node> arr,Node node)
 {
	 Node n=null;
	 for(Node nd:arr)
	 {
		 if(nd.getName().equals(node.getName()))
		 {
			n=nd;
			break;
		 }			 
	 }
	 return(n);
 }
 /**
  * Prune the list according to the parameter minsup 
  * @param arr
  * @param minsup
  * @param minconf
  */
 public static void prune(ArrayList<Node>arr,double minsup,double minconf)
 {
	 int size=asl.size();
	 for(int i=0;i<arr.size();i++)
	 {
		 Node node=arr.get(i);
		// System.out.println(node.getCount()+"<->"+size);
		 double d=node.getCount()/(double)size;
		 //System.out.println("--------------"+d);
		 if(d<minsup)
		 {
			 arr.remove(node);
		 } 
	 }
 }

 public static boolean inArr(int [] a,int n)
 {
	 int len=a.length;
	 if(len==0)
	 {
		 return(false);
	 }
	 int i=len-1;
	 for(;i>=0;i--)
	 {
		 if(a[i]==n)
		 {
			 break;
		 }
	 }
	 if(i>=0)
	 {
		 return(true);
	 }else{
		 return(false);
	 }
 }
 public  static int[][] getSubSet(int [] item, int r) {
		int ilen = item.length;
		int upper = 1, lower = 1;
		for (int i = 1; i <= r; i++) {
			upper *= (ilen - i + 1);
			lower *= i;
		}
		int nums = upper / lower;
		int[][] combinations = new int[nums][r];
		
		
		for (int i = 0; i < r; i++) {
			combinations[0][i] = i;
		}
		
		for (int i = 1; i < nums; i++) {
			int j = r - 1;
			int max = -1;
			int maxIndex = -1;
			while (j >= 0) {
				int lastOne = combinations[i - 1][j];
				if (max < lastOne && lastOne != (ilen - 1)) {
					boolean hasEqual = true;
					for (int k = j; k < r; k++) {
						if ((lastOne + 1) == combinations[i - 1][k]) {
							hasEqual = false;
						}
					}
					if (hasEqual) {
						max = lastOne;
						maxIndex = j;
					}
				}
				--j;
			}
			if (max != -1) {
				for (int k = 0; k < r; k++) {
					if (k < maxIndex) {
						combinations[i][k] = combinations[i - 1][k];
					} else if (k == maxIndex) {
						combinations[i][k] = max + 1;
					} else {
						combinations[i][k] = combinations[i][k - 1] + 1;
					}
				}
			}
		}		
		for (int i = 0; i < nums; i++) {
			for (int j = 0; j < r; j++) {
				combinations[i][j] = item[combinations[i][j]]; 				
			}			
		}
		return combinations;
	}
 /**
  * Generate rules based on the frequent items
  * @param arr
  * @return
  */
 public static ArrayList<Node> aprioriGen(ArrayList<Node> arr)
 {
	 ArrayList<Node> brr=new ArrayList<Node>();
	 int len=arr.size();
	 for(int i=0;i<len;i++)
	 {
	    Node node=arr.get(i);
		String [] tmpArr=node.getName().split(" ");
		int leng=tmpArr.length;
		String tmp=toString(tmpArr,0,leng-1);
		  for(int j=i+1;j<len;j++)
		  {
			  Node n=arr.get(j);
			  if(n.getName().indexOf(tmp)>=0)
			  {
				  Node nod=new Node(node.getName()+" "+n.getName().split(" ")[leng-1]);
				 
				  Node nodeTmp=null;
				  if((nodeTmp=contains(brr,nod))!=null)
				  {
					  nodeTmp.addCount();
				  }else{
					  //nod.addCount();
					  brr.add(nod);
				  }				  
			  }
		  }
	 }
	 for(Node ne:brr)
	 {
		 for(String rec:asl)
		 {
			 if(isSub(rec,ne.getName()))
			 {
				 ne.addCount();
			 }
		 }
	 }
	 return(brr);
 }

 public static String toString(String [] arr,int start,int end)
 {
	 String res="";
	 for(int i=start;i<end;i++)
		 res=res.concat(arr[i]+" ");
	 return(res.trim());
		 
 }
 public static String toString(int [] a,int [] b)
 {
	 String res="";
     int [] arr=new int[b.length];
     int tag=0;
     for(int i=0;i<b.length;i++)
    	 for(int j=0;j<a.length;j++)
    	 {
    		 if(b[i]==a[j])
    			 arr[tag++]=j;
    	 }
     for(int i=0;i<arr.length;i++)
     {
    	 res=res.concat(a[arr[i]]+" ");
     }
	 return(res.trim());
 }
 public static String getRemainString(int[] a,int [] b)
 {
	 String res="";
	 int tag=0;
	 int [] arr=new int[a.length-b.length];
	 for(int i=0;i<a.length;i++)
	 {
		 int j;
		 for(j=0;j<b.length;j++)
		 {
			 if(a[i]==b[j])
				 break;
		 }
		 if(j>=b.length)
		 {
			 arr[tag++]=i;
		 }
	 }
	 for(int i=0;i<arr.length;i++)
		 {
		 res=res.concat(a[arr[i]]+" ");
		 }
				 
	 return(res.trim());
 }
 public static int [] toIntArr(String string)
 {
	 String [] arr=string.split(" ");
	 int len=arr.length;
	 int  intArr[]=new  int[len];
	 for(int i=0;i<len;i++)
	 {
		 intArr[i]=Integer.parseInt(arr[i]);
	 }
	 return(intArr);
 }
public static boolean isSub(String target,String sub)
{
	boolean tag=true;
	String [] arr=sub.split(" ");
	String [] brr=target.split(" ");
	for(int i=0;i<arr.length;i++)
	{
		int j=0;
		for(;j<brr.length;j++)
		{
			if(arr[i].equals(brr[j]))
				break;
		}
		if(j<brr.length)
		{
			tag&=true;
		}else{
			tag&=false;
		}
	}
	return(tag);
}
/**
 *  Generate rules based on the frequent items and the minconf predefined
 * @param nodeList
 * @param minconf
 */
public static void apGenRules(ArrayList<Node> nodeList,double minconf)
{
  	for(Node node:nodeList)
  	{
  		String name=node.getName();
  		int len=name.split(" ").length;
  		for(int i=1;i<len;i++)
  		{
  			int [] a=toIntArr(name);
  			//print(a);
  			int [][] arr=getSubSet(a,i);
  			//print(arr[0]);
  			int rows=arr.length;
  			for(int j=0;j<rows;j++)
  			{
  				String head=getRemainString(a,arr[j]);
  				String tail=toString(a,arr[j]);
  				rule r=new rule(head,tail);
  				//System.out.println(head+"::::"+tail+new Node(head+" "+tail).getName());
  				String [] tmpArr=(head+" "+tail).split(" ");
  				Arrays.sort(tmpArr);
  				Node nod=contains(last,new Node(toString(tmpArr,0,tmpArr.length)));
  				tmpArr=head.split(" ");
  				Arrays.sort(tmpArr);
  				Node nod1=contains(last,new Node(toString(tmpArr,0,tmpArr.length)));
  				//System.out.println(nod.getName()+":"+nod.getCount()+"----------"+nod1.getName()+":"+nod1.getCount());
  				double conf=nod.getCount()/(double)nod1.getCount();

  				if((conf>=minconf) &&!contain(r))
  				{
  					//System.out.println(head+"->"+tail+"    "+conf);
  					r.setConf(conf);
  					ruleSet.add(r);
  				}
  			}
  		}
  	}
}
 public static void print(int [] a)
 {
	 for(int aa:a)
		 System.out.print(aa+" ");
	 System.out.println();
 }
  public static void arrSort(ArrayList<Node> arr)
 {
	 Collections.sort(arr,new Comparator(){
		 public int compare(Object o1, Object o2) {
             return ((Node)o1).getName().compareTo(((Node)o2).getName());
  } 
	 });
 }
  public static void printDetails()
    {
	  System.out.println("The frequent itemsets are: ");
	  for(Node node:last)
	  {
		  if(node.getCount()>=2)
		  System.out.println("Item(s)  "+node.name+"   N "+node.getCount());
	  }  
  }
  public static boolean contain(rule r)
  {
	  boolean tag=false;
	  for(rule l:ruleSet)
	  {
		  if(l.getHead().equals(r.getHead())&&l.getTail().equals(r.getTail()))
		  {
			  tag=true;
		  }
	  }
	  return(tag);
  }
}
