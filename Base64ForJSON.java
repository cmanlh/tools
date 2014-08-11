/**
 * It's not standard base64 algorithm converter, just for converting text to
 * String which can be included in JSON String
 * 
 * @author cmanlh@163.com
 * 
 */
public class Base64ForJSON {
	private static char[] CHAR_SET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '+', '/' };

	private static int[] CHAR_MAP = new int[128];

	static {
		for (int i = 0; i < CHAR_SET.length; i++) {
			CHAR_MAP[CHAR_SET[i]] = i;
		}
	}

	public static String encode(String source) {
		if (null == source) {
			return "";
		}

		int len = source.length();
		StringBuilder target = new StringBuilder(len * 4);

		for (int i = 0; i < len; i++) {
			target.append("=").append(charToU64(source.codePointAt(i)));
		}

		return target.toString();
	}

	public static String decode(String source) {
		if (null == source) {
			return "";
		}

		int len = source.length();
		StringBuilder target = new StringBuilder(len / 3);

		String[] charArray = source.split("=");
		int size = charArray.length;

		for (int i = 1; i < size; i++) {
			target.append(u64ToChar(charArray[i]));
		}

		return target.toString();
	}

	public static char u64ToChar(String u64) {
		int len = u64.length(), val = 0;
		for (int i = 0; i < len; i++) {
			val += CHAR_MAP[u64.charAt(i)] << (i * 6);
		}

		return (char) val;
	}

	public static String charToU64(int codePoint) {
		char[] buf = new char[4];
		int idx = 0, mask = 63;
		do {
			buf[idx++] = CHAR_SET[codePoint & mask];
			codePoint >>>= 6;
		} while (codePoint != 0);

		return new String(buf, 0, idx);
	}

	public static void main(String[] args) {
		System.out
				.println(encode("国际儿童节，顾名思义，是为儿童所设立的国际性节日，如今，全世界共有100个国家和地区庆祝儿童节。但因为世界各地政治、经济和社会文化情况的差异，儿童节也呈现各种地区性和文化上的差异。每年的6月1日，很多国家和地区会为儿童和家长，特别开放免费的公共文化展演、游园游戏服务以及亲子活动的场所等等。随着全世界城市化的发展、网络时代的来临，在商业化的潮流中，儿童节越来越变得物质化、礼品化和功利化；另一方面，因为每每也受限于儿童节并不是全民公共假期，孩子的节日与父母的周末很难在同一天，导致了我们成长里大多数的儿童节是——跟着老师去公园，去完公园就回家——的记忆，家人与爸爸妈妈的陪伴，在儿童节的印象里并不深刻。但幸运的是，2014年的儿童节正值周末。如何将儿童节转化为一个全家节日，我们的专家团队特别为您精心推荐以下攻略:全家的儿童节：请在这一天，全家人在一起，共同度过一个相互陪伴的儿童节； 也请和孩子说，这个世界上存在着各种各样的家庭（比如说双亲家庭、单亲家庭、以儿童为户主的家庭、以监护人为户主的家庭、大家庭、核心家庭和非传统家庭等等），但每个家庭都承载着重要的爱与关怀的使命；每个家庭成员都会承担着不同的家庭角色，都有不同的需求。比如说孩子需要爸爸妈妈陪伴，爷爷奶奶、外公外婆需要爸爸妈妈陪伴。随着孩子们的不断长大，他们的世界和情感会超越家庭的界限。朋友和同龄人对于他们而言变得尤其重要。自然的儿童节：请搭乘地铁和公共交通，把私家车暂停一日。带孩子去郊外的森林公园、河边野营，享受与阳光、青草、树木和昆虫的陪伴。最好的教育就是最自然的爸爸妈妈的陪伴。培养孩子发现自然发现美的眼睛。不插电的儿童节：请拔掉电源，关闭4G和GPS,把PAD和手机收起来，我们与孩子一起过一个不插电的儿童节。当我们要求孩子远离电脑、手机和游戏的时候，自己首先应该示范。最好的教育永远是，在孩子身边，言传身教。与孩子成为BFF的儿童节：让孩子策划自己的儿童节活动，请他来列出儿童节BFF(Best Friend Forever)名单。我们更需要告诉他：朋友有很多种（好朋友、坏朋友、男朋友、女朋友）；友谊是建立在信任、分享、同情和团结的接触上的；人与人之间的关系包含着多种不同类型的爱，而表达爱的方式有很多种，比如，邀请最好的朋友共度儿童节就是一种方式。友谊与爱可以帮助我们建立美好的自我感觉！成长教育的儿童节: 带孩子去科学馆，参观一次神奇的身体的展览。请在儿童节的这一天，用科学的方式告诉孩子：他从哪里来；请告诉孩子：每个人的身体都是独一无二的，都值得尊重，包括残疾人在内:男人、女人、男孩、女孩的身体各不相同，并会随时发生变化——这些变化就是成长；当中国的孩子、大孩子、后青春期的孩子们都在六一儿童节里快乐地享受春光与自然、在爸爸妈妈的陪伴下重新认识自己节日的时候，我们也来看一看，那些在六月一日没有儿童节的国家和地区，孩子们是在那一天度过自己的节日的？台湾香港地区的儿童节是4月4日，缘起于1931年中华慈幼协济会的一项提议，如今，香港台湾地区仍然在4月初春时分为孩子们庆祝自己的节日。澳门地区的儿童节是6月1日，小学生大多会参加政府和学校组织的游园活动。每一年澳门政府和社会组织都会为孩子们安排非常丰富的儿童公益活动，比如参观驻澳部队，儿童填色公益比赛，甚至举办监狱儿童节庆祝活动。2001年，美国政府才决定，每年6月的第二个周日是美国的儿童节。但和复活节、万圣节、圣诞节比起来，儿童节在美国实在是缺人问津，没啥人知道。美国儿童节的出现也仅仅是因为，2000年时的总统克林顿，收到一位女孩的来信，她要求总统先生给美国的孩子设立一个自己的节日。克林顿一时兴起，就将2000年10月8日定为美国儿童节。而第二年又被小布什改为6月3日，之后就演变成今天的日子。日本的儿童节是5月5日，这个原本是日本端午节的日子在历史的变迁中成为了今天的儿童节。这一天，日本每个家庭都会在屋顶和庭院中悬挂鱼旗，以保佑孩子平安、顺利成长。新加坡的儿童节是10月第一个星期五，但10月1日孩子们是可以放假休息的。新加坡的儿童节更像家庭节，那一日的功能，在宪法上写着：促进家庭生活。瑞典的儿童节是8月7日，那一天，瑞典男孩都要身着龙虾装，以证明自己像龙虾一样勇敢。瑞典儿童节又叫龙虾节；瑞士的儿童节是11月20日，这是遵循1925年在瑞士日内瓦召开的儿童福利的国际会议的呼唤和联合国教育科学文化组织的提议。英国的儿童节是7月14日。家长们必须携带家庭自制餐点和食物到学校与孩子共庆节日、参加校园义卖和游园活动。无论全世界这一百多个国家的儿童节是如何异彩纷呈地在春天、夏天、秋天或冬天里出现的，我们都希望能在这一天里，追赶陪伴童年的脚步。让我们陪伴父母去弥补我们年少时光里，未能与他们相处的儿童节；更让我们陪伴快乐童年里的孩子们，让他们从今天就深刻铭记，儿童节不仅仅是孩子的节日，更是家庭的节日。（文中下划线标示部分出自《联合国教科文组织国际性教育技术指导纲要》"));
		System.out
				.println(decode("=9bF=FZJ=/EF=lrH=CKI=M8P=+hJ=NQF=dAG=J5E=M8P=vYG=64E=/EF=lrH=AJG=+uI=LrH=EaH=9bF=FZJ=nAG=CKI=lXG=M8P=CmF=K7E=M8P=oFF=W4E=MVH=xFF=JcG=x=w=w=q4E=9bF=2uF=MSF=wcF=6MF=G6F=dlH=/EF=lrH=CKI=CAD=G9E=gbF=64E=W4E=MVH=EQF=wcF=/UG=7yG=BAD=P7H=O1G=MSF=+kH=a8E=HWG=WMF=FDG=1GF=EaH=u3F=C8F=M8P=/EF=lrH=CKI=f5E=IRF=wOH=EQF=NnH=wcF=6MF=nAG=MSF=HWG=WMF=K4E=EaH=u3F=C8F=CAD=PvG=05F=EaH=2=IcG=x=lXG=M8P=I+F=akF=9bF=2uF=MSF=wcF=6MF=a8E=64E=/EF=lrH=MSF=2uF=/VJ=M8P=5JH=rIF=A8F=+UG=NFF=50I=EaH=sFF=xFF=HWG=WMF=VxF=U8G=BAD=44G=tbF=44G=PIG=NcG=hKF=l7E=KPF=y6E=QtF=70G=oKF=EaH=6cF=AJG=JtH=JtH=CAD=PaJ=AdH=oFF=W4E=MVH=OfF=C4F=WMF=EaH=RPF=VxF=BAD=R9H=c7H=2XG=j7E=EaH=ldG=04E=M8P=ocF=GVF=a4E=WMF=EaH=u9G=B1G=t4E=M8P=/EF=lrH=CKI=K2I=ldG=K2I=YPF=X+F=pJH=o0I=WMF=BAD=8kH=BTF=WMF=MSF=fKF=pIF=WMF=b8P=mPF=A4E=5WG=idJ=M8P=gbF=64E=PvG=PvG=f5E=XPF=QZJ=O6E=/EF=lrH=CKI=25F=N4E=vYG=oFF=RwG=sFF=xFF=HBF=fcG=M8P=ptF=QtF=EaH=CKI=lXG=O4E=2IH=NvG=EaH=oRF=rcG=I+F=+aJ=ocF=MQF=A4E=pkF=M8P=8vF=0HI=G6E=RIG=s7E=QIG=/VJ=MHJ=nkF=akF=wVG=EaH=/EF=lrH=CKI=vYG=UAC=UAC=f3I=AdH=BAI=I4F=7OF=sFF=tbF=M8P=7OF=MuF=sFF=tbF=xwF=ebF=2uF=UAC=UAC=EaH=wuI=G/F=M8P=2uF=66E=O4E=4IH=4IH=ImF=ImF=EaH=qZJ=08E=M8P=ocF=/EF=lrH=CKI=EaH=wNF=hxI=MHJ=25F=N4E=x3G=7IF=CAD=G9E=45F=Q/I=EaH=vYG=M8P=y=w=x=0=05F=EaH=/EF=lrH=CKI=jtG=8AF=oRF=rcG=CAD=CmF=V9E=GwF=/EF=lrH=CKI=s9I=WMF=64E=A4E=q4E=oFF=2uF=CKI=lXG=M8P=RIG=s7E=EaH=T4E=2uF=ibF=fYJ=5JH=rIF=64E=oCG=+yH=D/F=oOG=QNI=l7E=L4E=7UG=lVH=6=oFF=2uF=EaH=/EF=lrH=CKI=a8P=3vI=ocF=Z/I=A4E=pkF=M8P=oFF=2uF=66E=ocF=A4E=31I=M8P=xFF=MQF=m6F=H/I=A4E=q4E=4bH=S6E=qZJ=08E=EaH=/EF=lrH=CKI=b8P=g=f5E=3vI=MSF=ptF=QtF=0vI=M8P=Z/I=q4E=W4E=MVH=K4E=YtF=ocF=AdH=EQF=NnH=EQF=3gG=EaH=2uF=t6F=I8P=UvG=CmF=0vI=MPF=y6E=2uF=t6F=BAD=VNF=y6E=2uF=t6F=BAD=l7E=/EF=lrH=64E=3IG=74E=EaH=2uF=t6F=BAD=l7E=RbH=kKG=66E=64E=3IG=74E=EaH=2uF=t6F=BAD=nkF=2uF=t6F=BAD=4gG=D/F=2uF=t6F=MSF=edJ=g8E=f7H=2uF=t6F=JtH=JtH=J8P=M8P=G9E=PvG=q4E=2uF=t6F=9DJ=/JG=99I=AdH=NHJ=BmI=EaH=xIH=O4E=zFF=AAG=EaH=/9E=9RF=b8P=PvG=q4E=2uF=t6F=QIG=YRF=9DJ=a8E=/JG=FLG=AdH=N4E=MQF=EaH=2uF=t6F=SnI=yJI=M8P=9DJ=JcG=N4E=MQF=EaH=AcJ=CxG=CAD=UvG=CmF=0vI=ptF=QtF=AcJ=BmI=4IH=4IH=ImF=ImF=qZJ=08E=M8P=3IH=3IH=2lF=2lF=BAD=WkF=sFF=WkF=GpF=AcJ=BmI=4IH=4IH=ImF=ImF=qZJ=08E=CAD=PaJ=AdH=ptF=QtF=s7E=EaH=N4E=tWG=/VJ=nkF=M8P=W7E=s7E=EaH=W4E=MVH=MSF=FDG=fEG=a8E=F2I=K2I=2uF=t6F=EaH=MVH=QZJ=CAD=LcG=LPF=MSF=MQF=E+J=66E=5vF=O6E=W7E=s7E=MAI=AoI=YPF=X+F=kwF=2FF=NHJ=BmI=CAD=qHI=2EH=EaH=/EF=lrH=CKI=a8P=3vI=tQG=Y5E=wcF=BTJ=MSF=sFF=xFF=k6E=aAJ=M8P=KKG=BnH=2uF=m9I=CaG=cBF=A4E=lXG=CAD=m4F=ptF=QtF=7OF=KDJ=WkF=EaH=ujG=XeG=sFF=tbF=BAD=zyG=5+I=OHJ=lQI=M8P=r6E=XPF=O4E=zYJ=JFF=BAD=SdJ=JNI=BAD=RgG=ocG=MSF=GYG=rZI=EaH=qZJ=08E=CAD=AcG=9lF=EaH=ZVG=yCI=xwF=vYG=AcG=qHI=2EH=EaH=4IH=4IH=ImF=ImF=EaH=qZJ=08E=CAD=5fF=7FF=ptF=QtF=RPF=wOH=qHI=2EH=RPF=wOH=O+H=EaH=8cH=bdH=CAD=N4E=SPG=1UH=EaH=/EF=lrH=CKI=a8P=3vI=ULG=JOG=1UH=Q6G=M8P=zFF=tXJ=0=HB=MSF=HB=QB=TB=s=KKG=QB=BB=EB=MSF=LJG=6cG=2UG=31I=ldG=M8P=RIG=s7E=O4E=ptF=QtF=A4E=31I=H/I=A4E=q4E=N4E=SPG=1UH=EaH=/EF=lrH=CKI=CAD=T9F=RIG=s7E=BmI=CxG=ptF=QtF=c/I=7mH=1UH=REI=BAD=LJG=6cG=MSF=44G=PIG=EaH=2XG=ZAF=M8P=qHI=x3F=WmJ=IFF=U6F=lvI=6kH=DMI=CAD=AcG=9lF=EaH=ZVG=yCI=4wG=c/I=vYG=M8P=ocF=ptF=QtF=r6I=5+I=M8P=AoI=g8E=r6I=ZVG=CAD=O4E=ptF=QtF=QIG=64E=CB=GB=GB=EaH=/EF=lrH=CKI=a8P=puI=ptF=QtF=WtH=SIF=qHI=x3F=EaH=/EF=lrH=CKI=70G=oKF=M8P=3vI=W7E=ldG=XIF=6HF=/EF=lrH=CKI=CB=GB=GB=o=CB=lB=zB=0B=g=GB=yB=pB=lB=uB=kB=g=GB=vB=yB=lB=2B=lB=yB=p=NQF=VNF=CAD=RIG=s7E=0bG=AcJ=BmI=KRF=JvI=W7E=a8P=LcG=LPF=JcG=I+F=akF=NnH=I8P=9lF=LcG=LPF=BAD=PdF=LcG=LPF=BAD=3UH=LcG=LPF=BAD=zlF=LcG=LPF=J8P=b8P=LPF=KwI=vYG=67F=LrH=ocF=h/E=77E=BAD=GIF=r6E=BAD=MQF=FDG=MSF=ibF=T7H=EaH=lOG=mnI=K4E=EaH=b8P=66E=O4E=66E=L5E=0XJ=EaH=zFF=7zH=FMF=rQF=AdH=akF=NnH=N4E=MQF=7xH=LeF=EaH=xIH=M8P=MAI=ohI=++I=xIH=EaH=5WG=P8F=JcG=I+F=akF=NnH=M8P=UvG=CmF=M8P=ACJ=3vI=AcG=9lF=EaH=LcG=LPF=xFF=m6F=/EF=lrH=CKI=xwF=vYG=A4E=NnH=5WG=P8F=CAD=LPF=KwI=O4E=xIH=vPF=l7E=u4F=pKF=RIG=s7E=67F=LrH=O+H=9lF=EaH=qHI=RIG=fEG=JnI=B8P=QIG=/VJ=ZVG=yCI=EaH=/EF=lrH=CKI=6=g=m4F=ptF=QtF=7OF=RnH=mtF=GmJ=M8P=CPF=CnI=A4E=hsG=elH=HlF=EaH=r6I=T9E=EaH=VxF=InI=CAD=3vI=ocF=/EF=lrH=CKI=EaH=Z/I=A4E=pkF=M8P=oUH=RnH=mtF=EaH=5WG=P8F=KRF=JvI=ptF=QtF=a8P=W7E=O7E=qTF=MHJ=ldG=b8P=3vI=KRF=JvI=ptF=QtF=a8P=PvG=q4E=66E=EaH=r6I=T9E=9DJ=vYG=sLH=A4E=gXG=M6E=EaH=M8P=9DJ=8AF=X+F=KwF=NHJ=M8P=FMF=sLG=LuG=+WH=66E=ocF=FGF=6=3UH=66E=BAD=zlF=66E=BAD=3UH=ptF=BAD=zlF=ptF=EaH=r6I=T9E=EQF=N4E=4bH=MQF=M8P=25F=a8E=PaJ=2XG=RPF=fUH=YPF=WMF=UAC=UAC=Z/I=b6E=YPF=WMF=xwF=vYG=QIG=/VJ=b8P=T9F=t4E=9bF=EaH=ptF=QtF=BAD=nkF=ptF=QtF=BAD=OQF=SdJ=lYG=fcG=EaH=ptF=QtF=s7E=9DJ=ocF=tFF=A4E=/EF=lrH=CKI=MHJ=r/F=Q5E=wcF=r6E=XPF=lYG=JFF=O4E=qHI=2EH=BAD=ocF=4IH=4IH=ImF=ImF=EaH=qZJ=08E=L4E=NHJ=wWG=kuI=GvI=qHI=x3F=CKI=lXG=EaH=2XG=ZAF=M8P=RIG=s7E=f5E=ldG=LcH=A4E=LcH=M8P=jCJ=b6E=ocF=tFF=IcG=A4E=lXG=hyG=JcG=/EF=lrH=CKI=EaH=9bF=2uF=MSF=wcF=6MF=M8P=ptF=QtF=s7E=vYG=ocF=jCJ=A4E=pkF=m6F=H/I=qHI=x3F=EaH=CKI=lXG=EaH=f8P=wPF=+5G=ZmJ=v4G=wcF=6MF=EaH=/EF=lrH=CKI=vYG=0=IcG=0=lXG=M8P=Y8H=31I=O6E=x=5=z=x=05F=t4E=ONF=IFG=85F=PNF=O1G=a8E=EaH=A4E=5hJ=QPG=uuI=M8P=CmF=K7E=M8P=ZmJ=v4G=wPF=+5G=wcF=6MF=N7E=2EH=ocF=0=IcG=dIF=lYG=2XG=GIF=64E=ptF=QtF=s7E=G6F=dlH=qHI=x3F=EaH=CKI=lXG=CAD=z+G=oXJ=wcF=6MF=EaH=/EF=lrH=CKI=vYG=2=IcG=x=lXG=M8P=PwF=mtF=fUH=nkF=akF=a8E=CPF=gKF=/UG=c6F=MSF=mtF=hgG=E7H=H7H=EaH=44G=tbF=70G=oKF=CAD=PvG=A4E=05F=z+G=oXJ=/UG=c6F=MSF=+kH=a8E=E7H=H7H=9DJ=a8E=64E=ptF=QtF=s7E=JuF=SOG=edJ=44F=w4E=MvF=EaH=/EF=lrH=sFF=KbH=70G=oKF=M8P=UvG=CmF=CPF=CnI=7pJ=z+G=oDJ=fYJ=M8P=/EF=lrH=rhF=yJI=sFF=KbH=UvG=b1I=M8P=aUH=zHI=+4E=eKF=RbH=xLH=/EF=lrH=CKI=G6F=dlH=70G=oKF=CAD=y=w=w=x=05F=M8P=O+H=9bF=/UG=c6F=NJG=zGF=auF=M8P=PvG=05F=2=IcG=EaH=ssH=M6E=q4E=oRF=lXG=vYG=O+H=9bF=EaH=/EF=lrH=CKI=CAD=G9E=MSF=NkF=70G=CKI=BAD=H4E=jcF=CKI=BAD=jcF=evI=CKI=UvG=31I=ldG=M8P=/EF=lrH=CKI=ocF=O+H=9bF=euF=ocF=vYG=68H=66E=uXJ=l0G=M8P=hyG=lVF=66E=lfH=TBJ=CAD=O+H=9bF=/EF=lrH=CKI=EaH=6HF=wOH=f5E=F7E=F7E=vYG=gbF=64E=M8P=y=w=w=w=05F=2XG=EaH=7AG=f7H=LFF=XeG=/hJ=M8P=2UG=wIF=A4E=N9E=zlF=ptF=EaH=ldG=h/E=M8P=5lF=BmI=CxG=7AG=f7H=IFF=fUH=Z7H=O+H=9bF=EaH=ptF=QtF=+uI=LrH=A4E=q4E=qHI=x3F=EaH=CKI=lXG=CAD=LFF=XeG=/hJ=A4E=2XG=0FF=31I=M8P=xwF=GwF=y=w=w=w=05F=x=w=IcG=4=lXG=auF=64E=O+H=9bF=/EF=lrH=CKI=CAD=MAI=ssH=M6E=05F=IPF=riI=PwF=D4F=A7E=5UG=64E=2=IcG=z=lXG=M8P=L5E=OQF=xwF=U8G=YPF=QIG=K7E=pkF=EaH=lXG=QtF=CAD=lXG=scG=EaH=/EF=lrH=CKI=vYG=1=IcG=1=lXG=M8P=Z/I=q4E=fOF=scG=vYG=lXG=scG=vrH=INF=CKI=EaH=lXG=QtF=ocF=GOF=yPF=EaH=YPF=B/I=t4E=QIG=64E=G6E=K7E=pkF=EaH=/EF=lrH=CKI=CAD=Z/I=A4E=pkF=M8P=lXG=scG=PvG=q4E=2uF=t6F=9DJ=a8E=ocF=LxF=2hJ=MSF=t6F=iZJ=t4E=sCG=CMG=8xJ=XXG=M8P=l7E=d/E=R9E=ptF=QtF=z5F=JuF=BAD=6hJ=pIF=QIG=/VJ=CAD=wWG=gKF=hdF=EaH=/EF=lrH=CKI=vYG=x=w=IcG=ssH=A4E=q4E=fYG=fcG=U6E=M8P=G9E=x=w=IcG=x=lXG=ptF=QtF=s7E=vYG=vPF=l7E=+UG=HBF=R8E=vBG=EaH=CAD=wWG=gKF=hdF=EaH=/EF=lrH=CKI=0bG=PDF=2uF=t6F=CKI=M8P=jCJ=A4E=lXG=EaH=fKF=9DI=M8P=ocF=quF=VzG=K4E=ZGF=AdH=a8P=D/E=b/I=2uF=t6F=fUH=70G=CAD=eRH=4FF=EaH=/EF=lrH=CKI=vYG=4=IcG=3=lXG=M8P=jCJ=A4E=pkF=M8P=eRH=4FF=3UH=ptF=9DJ=BmI=r6I=AdH=Z+J=+ZI=FjI=M8P=l7E=BvI=OYG=qHI=x3F=PDF=Z+J=+ZI=A4E=3gG=HLF=iVG=CAD=eRH=4FF=/EF=lrH=CKI=IPF=rPF=Z+J=+ZI=CKI=b8P=eRH=rjF=EaH=/EF=lrH=CKI=vYG=x=x=IcG=y=w=lXG=M8P=Z/I=vYG=1BJ=q+F=x=5=y=1=05F=ocF=eRH=rjF=lXG=FGF=mTH=sPF=A8F=EaH=/EF=lrH=PmH=pIF=EaH=9bF=FZJ=a8E=uuI=EaH=8RF=kUF=MSF=UBI=IQF=9bF=ZVG=yCI=RnH=mtF=HWG=WMF=E7H=H7H=EaH=QPG=uuI=CAD=xLI=9bF=EaH=/EF=lrH=CKI=vYG=3=IcG=x=0=lXG=CAD=2uF=/VJ=s7E=F/F=7hJ=6QG=m4F=2uF=t6F=qHI=2IF=QkJ=5CH=MSF=fjJ=pJH=wIF=mtF=hgG=O4E=ptF=QtF=xFF=G6F=CKI=lXG=BAD=CPF=gKF=hgG=tbF=J5E=WNF=MSF=44G=tbF=70G=oKF=CAD=gXG=6uI=oFF=W4E=MVH=Z/I=A4E=+ZH=akF=q4E=9bF=2uF=EaH=/EF=lrH=CKI=vYG=CmF=V9E=C8F=p9F=36H=IRF=wcF=ocF=lYG=pkF=BAD=PkF=pkF=BAD=LnH=pkF=WIG=sGF=pkF=MHJ=6HF=wOH=EaH=M8P=RIG=s7E=9DJ=M4F=bcG=9DI=ocF=Z/I=A4E=pkF=MHJ=M8P=9/I=21I=qZJ=08E=lrH=05F=EaH=aEI=ltG=CAD=puI=RIG=s7E=qZJ=08E=2IH=NvG=7OF=l8F=lhI=RIG=s7E=05F=RwF=2XG=JFF=MHJ=M8P=qcG=9DI=O4E=W7E=s7E=4bH=EkF=EaH=/EF=lrH=CKI=b8P=0bG=puI=RIG=s7E=qZJ=08E=r/F=Q5E=lrH=05F=MHJ=EaH=ptF=QtF=s7E=M8P=puI=W7E=s7E=O7E=K7E=pkF=xwF=x3G=7IF=tTJ=wuI=M8P=/EF=lrH=CKI=N4E=F7E=F7E=vYG=ptF=QtF=EaH=CKI=lXG=M8P=0bG=vYG=2uF=t6F=EaH=CKI=lXG=CAD=I8P=HWG=t4E=L4E=SIF=/6H=HgG=6kH=oDJ=GIF=6HF=qHI=KAD=UBI=IQF=9bF=ZVG=RnH=HWG=E7H=H7H=9bF=FZJ=nAG=ZVG=yCI=AKG=vcG=HMG=8vF=y6H=BmI=LAD"));

		System.out.println(encode(""));
		System.out.println(decode(""));

	}
}
