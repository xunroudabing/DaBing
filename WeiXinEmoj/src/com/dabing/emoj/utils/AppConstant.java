package com.dabing.emoj.utils;

public class AppConstant {
	public static final String OAUTH_STRING = "access_token=0de0fe2bc57e4b03452cb45e56c0f6b8&expires_in=1209600&openid=1F897B956FF99B6865FDF04639FE19CF&openkey=94C16092C9BDB069C49413525A824BBF&refresh_token=51de74361c8dd5f1fa975fbede3498ad&state=&name=xunroudabing_&nick=%E7%86%8F%E8%82%89%E5%A4%A7%E9%A5%BC";
	//测试:wxbbd909e16304292f 正式:wx715555f987420fd8
	public static final String WEIXIN_APPID = "wx715555f987420fd8";
	public static final String WEIXIN_PUBLIC_ACCOUNT = "http://weixin.qq.com/r/4XUcE0bEc3gMh1dwnyDF";
	// QQ UMENG WAPS
	public static final String AD_TYPE = "WAPS";
	//广告开关
	public static final boolean AD_ENABLE = true;
	//积分开关 是否开启积分 所有需要积分的都免费
	public static final boolean BONUS_ENABLE = true;
	//是否隐藏积分 和积分有关的都不显示
	public static final boolean BONUS_HIDE = true;
	//收听的微博账号名
	public static final String WEIBO_IDOL = "xunroudabing";
	//默认的广告TAG
	public static final String AD_DEFAULT_ADSHOWTYPE = "YOUMI_BANNER";
	//签名md5
	public static final String SIGN = "b7ac740f2a1bfb5b391b3d869bcca97b";
	//万普广告形式 BANNER MINI CUSTOM
	public static final String WAPS_AD_TYPE = "CUSTOM";
	public static final String WAPS_INDEX1_AD_TYPE = "BANNER";
	public static final String WAPS_INDEX2_AD_TYPE = "CUSTOM";
	public static final String WAPS_INDEX3_AD_TYPE = "CUSTOM";
	//万普广告id
	public static final String WAPS_ID = "4bedbf302e55d6a0e8833404f073e31c";
	public static final String WAPS_PID = "QQ";
	//有米广告id
	public static final String YOUMI_ID = "9f2d289d6d67a71b";
	public static final String YOUMI_KEY = "69d10da4eebfb08c";
	//腾讯应用墙
	public static final int APP_WALL_ID = 10274;
	//小米推送服务ID
	public static final String MI_APP_ID = "1008936";
	public static final String MI_APP_KEY = "760100869936";
	public static final String MI_APP_SECRET = "";
	//加密seed
	public static final String ENCRYPT_SEED = "1122";
	public static final String EMOJ_INDEX = "[{\"id\":\"000\",\"o\":1,\"d\":\"\",\"t\":\"最近使用\",\"b\":\"blue\",\"c\":0,\"p\":[]},{\"id\":\"099\",\"o\":2,\"d\":\"\",\"t\":\"热门表情\",\"b\":\"red\",\"c\":2,\"p\":[]},{\"id\":\"032\",\"o\":2,\"d\":\"\",\"t\":\"小纯洁\",\"b\":\"lime\",\"c\":1,\"p\":[\"wxemoj032\"]},{\"id\":\"015\",\"o\":3,\"d\":\"\",\"t\":\"西游沙僧\",\"b\":\"brown\",\"c\":1,\"p\":[\"wxemoj015\"]},{\"id\":\"034\",\"o\":4,\"d\":\"\",\"t\":\"卡通猫咪\",\"b\":\"orange\",\"c\":1,\"p\":[\"wxemoj034\"]},{\"id\":\"005\",\"o\":5,\"d\":\"\",\"t\":\"碎碎猫\",\"b\":\"pink\",\"c\":1,\"p\":[\"wxemoj005\"]},{\"id\":\"003\",\"o\":6,\"d\":\"\",\"t\":\"小幺鸡\",\"b\":\"purple\",\"c\":1,\"p\":[\"wxemoj003\"]},{\"id\":\"025\",\"o\":7,\"d\":\"\",\"t\":\"小阿狸\",\"b\":\"red\",\"c\":1,\"p\":[\"wxemoj025\"]},{\"id\":\"021\",\"o\":8,\"d\":\"\",\"t\":\"西游悟空\",\"b\":\"red\",\"c\":1,\"p\":[\"wxemoj021\"]},{\"id\":\"031\",\"o\":9,\"d\":\"\",\"t\":\"酷酷鸟\",\"b\":\"red\",\"c\":1,\"p\":[\"wxemoj031\"]},{\"id\":\"033\",\"o\":17,\"d\":\"\",\"t\":\"茉莉小妞\",\"b\":\"magenta\",\"c\":1,\"p\":[\"wxemoj033\"]},{\"id\":\"037\",\"o\":11,\"d\":\"\",\"t\":\"一家a口\",\"b\":\"lime\",\"c\":1,\"p\":[\"wxemoj037\"]},{\"id\":\"001\",\"o\":13,\"d\":\"\",\"t\":\"麦拉风\",\"b\":\"brown\",\"c\":1,\"p\":[\"wxemoj001\"]},{\"id\":\"019\",\"o\":14,\"d\":\"\",\"t\":\"HELLO菜菜\",\"b\":\"red\",\"c\":1,\"p\":[\"wxemoj019\"]},{\"id\":\"017\",\"o\":15,\"d\":\"\",\"t\":\"西游唐僧\",\"b\":\"orange\",\"c\":1,\"p\":[\"wxemoj017\"]},{\"id\":\"036\",\"o\":16,\"d\":\"\",\"t\":\"乐愚驴\",\"b\":\"lime\",\"c\":1,\"p\":[\"wxemoj036\"]},{\"id\":\"009\",\"o\":10,\"d\":\"\",\"t\":\"白骨精小白\",\"b\":\"magenta\",\"c\":1,\"p\":[\"wxemoj009\"]},{\"id\":\"024\",\"o\":18,\"d\":\"\",\"t\":\"蘑菇点点\",\"b\":\"teal\",\"c\":1,\"p\":[\"wxemoj024\"]},{\"id\":\"020\",\"o\":19,\"d\":\"\",\"t\":\"icoool芒果\",\"b\":\"orange\",\"c\":1,\"p\":[\"wxemoj020\"]},{\"id\":\"011\",\"o\":20,\"d\":\"\",\"t\":\"皮揣子猫\",\"b\":\"orange\",\"c\":1,\"p\":[\"wxemoj011\"]},{\"id\":\"023\",\"o\":21,\"d\":\"\",\"t\":\"长颈鹿但丁\",\"b\":\"brown\",\"c\":1,\"p\":[\"wxemoj023\"]},{\"id\":\"014\",\"o\":22,\"d\":\"\",\"t\":\"潘斯特兔子\",\"b\":\"pink\",\"c\":1,\"p\":[\"wxemoj014\"]},{\"id\":\"022\",\"o\":23,\"d\":\"\",\"t\":\"罗罗布\",\"b\":\"blue\",\"c\":1,\"p\":[\"wxemoj022\"]},{\"id\":\"028\",\"o\":25,\"d\":\"\",\"t\":\"NONOPANDA\",\"b\":\"lime\",\"c\":1,\"p\":[\"wxemoj028\"]},{\"id\":\"027\",\"o\":26,\"d\":\"\",\"t\":\"衰女恋恋\",\"b\":\"magenta\",\"c\":1,\"p\":[\"wxemoj027\"]},{\"id\":\"008\",\"o\":27,\"d\":\"\",\"t\":\"BOBO熊\",\"b\":\"teal\",\"c\":1,\"p\":[\"wxemoj008\"]},{\"id\":\"004\",\"o\":30,\"d\":\"\",\"t\":\"张小盒\",\"b\":\"brown\",\"c\":1,\"p\":[\"wxemoj004\"]},{\"id\":\"026\",\"o\":31,\"d\":\"\",\"t\":\"腾讯经典\",\"b\":\"orange\",\"c\":1,\"p\":[\"wxemoj026\"]},{\"id\":\"012\",\"o\":33,\"d\":\"\",\"t\":\"贪吃蜘蛛\",\"b\":\"purple\",\"c\":1,\"p\":[\"wxemoj012\"]},{\"id\":\"029\",\"o\":34,\"d\":\"\",\"t\":\"木牙团\",\"b\":\"lime\",\"c\":1,\"p\":[\"wxemoj029\"]},{\"id\":\"013\",\"o\":35,\"d\":\"\",\"t\":\"鸭梨山大\",\"b\":\"teal\",\"c\":1,\"p\":[\"wxemoj013\"]},{\"id\":\"018\",\"o\":36,\"d\":\"\",\"t\":\"开心果\",\"b\":\"pink\",\"c\":1,\"p\":[\"wxemoj018\"]},{\"id\":\"035\",\"o\":37,\"d\":\"\",\"t\":\"怒怒阿呆\",\"b\":\"brown\",\"c\":1,\"p\":[\"wxemoj035\"]},{\"id\":\"016\",\"o\":38,\"d\":\"\",\"t\":\"悠嘻猴\",\"b\":\"lime\",\"c\":1,\"p\":[\"wxemoj016\"]},{\"id\":\"039\",\"t\":\"菜花宝宝\",\"p\":[\"wxemoj039\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":39},{\"id\":\"041\",\"t\":\"屌屌兔\",\"p\":[\"wxemoj041\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":40},{\"id\":\"042\",\"t\":\"囧囧\",\"p\":[\"wxemoj042\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":41},{\"id\":\"043\",\"t\":\"娜娜\",\"p\":[\"wxemoj043\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":42},{\"id\":\"045\",\"t\":\"兔斯基\",\"p\":[\"wxemoj045\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":43},{\"id\":\"046\",\"t\":\"渣渣兔\",\"p\":[\"wxemoj046\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":44},{\"id\":\"047\",\"t\":\"PusheenCat\",\"p\":[\"wxemoj047\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":45},{\"id\":\"048\",\"t\":\"Line\",\"p\":[\"wxemoj048\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":46},{\"id\":\"010\",\"t\":\"彼格梨\",\"p\":[\"wxemoj010\"],\"b\":\"orange\",\"d\":\"\",\"c\":1,\"o\":47},{\"id\":\"051\",\"t\":\"箭人神棍\",\"p\":[\"wxemoj051\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":48},{\"id\":\"052\",\"t\":\"井果儿\",\"p\":[\"wxemoj052\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":49},{\"id\":\"053\",\"t\":\"铅笔贱\",\"p\":[\"wxemoj053\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":50},{\"id\":\"054\",\"t\":\"逗比肠\",\"p\":[\"wxemoj054\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":51},{\"id\":\"055\",\"t\":\"易信\",\"p\":[\"wxemoj055\"],\"b\":\"lime\",\"d\":\"\",\"c\":1,\"o\":52}]";
	//热门
	public static final String EMOJ_HOT = "[\"50ba5e2b2567484def34\",\"4b478b1a79b92dfb2e5a\",\"41d01ec0af74ce6e7dae\",\"8908d2b995706e8ab274\",\"676fcc9d6cb6f916bc8a\",\"ca7deca6fc172e770d6c\",\"6dac12c92d5d958b481e\",\"e0f24d1c7e6139ab4e0c\",\"1d88ad84f69baabe00b8\",\"aa75e5cbe2816e64c1e0\",\"5c4076209a0adc982eea\",\"16d22b5ce1523483cf14\",\"3f627c7f2c32b4bf66d6\",\"fcd2cc6a9e82250c9b98\",\"46ec0890c9d75361814a\",\"d07435e679e4144c936c\"]";
	public static final String PIC_SERVER_URL="http://app.qpic.cn/mblogpic/";
	public static final String MENU_REGULAR = "{\"id\":\"000\",\"t\":\"最近使用\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":0,\"o\":1}";
	public static final String SHARE_IMG_URL = "http://t1.qpic.cn/mblogpic/540d116b70c1a52f3040/2000";
	public static final String CATEGORY = "[{\"name\":\"美女\",\"p\":\"美女\",\"d\":\"\",\"o\":1,\"a\":\"action013\"},{\"name\":\"搞笑\",\"p\":\"搞笑gif\",\"d\":\"\",\"o\":2,\"a\":\"action014\"},{\"name\":\"动漫\",\"p\":\"动漫\",\"d\":\"\",\"o\":3,\"a\":\"action015\"},{\"name\":\"帅哥\",\"p\":\"帅哥\",\"d\":\"\",\"o\":4,\"a\":\"action016\"},{\"name\":\"美图\",\"p\":\"摄影\",\"d\":\"\",\"o\":5,\"a\":\"action017\"},{\"name\":\"有点萌\",\"p\":\"萌宠\",\"d\":\"\",\"o\":6,\"a\":\"action018\"}]";
	//微频道新 废弃
	public static final String CATEGORY_1 = "[{\"name\":\"美女\",\"p\":\"美女\",\"d\":\"\",\"o\":1,\"a\":\"action013\",\"c\":\"43\"},{\"name\":\"搞笑\",\"p\":\"搞笑gif\",\"d\":\"\",\"o\":2,\"a\":\"action014\",\"c\":\"903\"},{\"name\":\"动漫\",\"p\":\"动漫\",\"d\":\"\",\"o\":3,\"a\":\"action015\",\"c\":\"838\"},{\"name\":\"手绘\",\"p\":\"手绘\",\"d\":\"\",\"o\":4,\"a\":\"action023\",\"c\":\"1633\"},{\"name\":\"美图\",\"p\":\"摄影\",\"d\":\"\",\"o\":5,\"a\":\"action017\",\"c\":\"31\"},{\"name\":\"有点萌\",\"p\":\"萌宠\",\"d\":\"\",\"o\":6,\"a\":\"action018\",\"c\":\"41\"}]";
	//微频道 2013.10.25
	public static final String CHANNEL_CATEGORY_INDEX = "[{\"id\":903,\"n\":\"搞笑\"},{\"id\":43,\"n\":\"美女\"},{\"id\":41,\"n\":\"有点萌\"},{\"id\":838,\"n\":\"动漫\"},{\"id\":836,\"n\":\"美图\"}]";
	public static final String CHANNEL_CATEGORY = "[{\"id\":43,\"n\":\"美女\",\"info\":[{\"id\":1594,\"n\":\"校花\"},{\"id\":1948,\"n\":\"清纯\"},{\"id\":2441,\"n\":\"小清新\"},{\"id\":1938,\"n\":\"模特\"},{\"id\":1645,\"n\":\"女神\"},{\"id\":2184,\"n\":\"萌女神\"},{\"id\":1565,\"n\":\"单身美女\"},{\"id\":1945,\"n\":\"女星\"},{\"id\":2176,\"n\":\"真我\"},{\"id\":2440,\"n\":\"美女cos\"},{\"id\":1605,\"n\":\"比基尼\",\"b\":25},{\"id\":1946,\"n\":\"性感\",\"b\":25},{\"id\":1949,\"n\":\"美胸\",\"b\":25}]},{\"id\":903,\"n\":\"搞笑\",\"info\":[{\"id\":1205,\"n\":\"萌猫\"},{\"id\":1206,\"n\":\"萌狗\"},{\"id\":1575,\"n\":\"动漫萌宠\"},{\"id\":2027,\"n\":\"搞笑长图\"},{\"id\":2025,\"n\":\"暴走漫画\",\"b\":25}]},{\"id\":838,\"n\":\"动漫\",\"info\":[{\"id\":20,\"n\":\"海贼王\"},{\"id\":21,\"n\":\"火影忍者\"},{\"id\":23,\"n\":\"柯南\"},{\"id\":1258,\"n\":\"妖精的尾巴\"},{\"id\":2350,\"n\":\"进击的巨人\"},{\"id\":1236,\"n\":\"耽美\"},{\"id\":1250,\"n\":\"治愈\"},{\"id\":1171,\"n\":\"微漫画\",\"b\":25},{\"id\":2418,\"n\":\"神级COS\",\"b\":25}]},{\"id\":49,\"n\":\"时尚\",\"info\":[{\"id\":36,\"n\":\"服饰搭配\"},{\"id\":1690,\"n\":\"中国时装周\"},{\"id\":895,\"n\":\"鞋包配饰\"},{\"id\":1303,\"n\":\"奢侈品\"},{\"id\":37,\"n\":\"美容美发\"},{\"id\":891,\"n\":\"美体瘦身\"},{\"id\":2443,\"n\":\"时尚达人\"},{\"id\":1999,\"n\":\"购物社区\"}]},{\"id\":836,\"n\":\"美图\",\"info\":[{\"id\":33,\"n\":\"创意灵感\"},{\"id\":1618,\"n\":\"插画\"},{\"id\":2253,\"n\":\"大神赐画\",\"b\":25},{\"id\":2092,\"n\":\"手工DIY\"},{\"id\":1664,\"n\":\"黑白\"},{\"id\":1657,\"n\":\"风光\"},{\"id\":1656,\"n\":\"人像\"},{\"id\":1596,\"n\":\"微距\"},{\"id\":1717,\"n\":\"纪实\"}]},{\"id\":38,\"n\":\"旅行\",\"info\":[{\"id\":1989,\"n\":\"国内游\"},{\"id\":1990,\"n\":\"国外游\"},{\"id\":1993,\"n\":\"主题游\"},{\"id\":1988,\"n\":\"人在旅途\"}]}]";
	public static final String EMOJ_NEW = "[\"001\",\"003\",\"010\",\"032\",\"034\",\"051\",\"052\",\"053\",\"054\",\"055\"]";
	public static final String EMOJ_EMOTION_INDEX="[{\"id\":\"e1\",\"t\":\"大笑\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":1},{\"id\":\"e2\",\"t\":\"微笑\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":2},{\"id\":\"e3\",\"t\":\"坏笑\",\"p\":[],\"b\":\"orange\",\"d\":\"\",\"c\":1,\"o\":3},{\"id\":\"e4\",\"t\":\"节日祝福\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":4},{\"id\":\"e5\",\"t\":\"抠鼻子\",\"p\":[],\"b\":\"brown\",\"d\":\"\",\"c\":1,\"o\":5},{\"id\":\"e6\",\"t\":\"阴险\",\"p\":[],\"b\":\"purple\",\"d\":\"\",\"c\":1,\"o\":6},{\"id\":\"e7\",\"t\":\"擦汗\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":7},{\"id\":\"e8\",\"t\":\"不高兴\",\"p\":[],\"b\":\"pink\",\"d\":\"\",\"c\":1,\"o\":8},{\"id\":\"e9\",\"t\":\"闪亮登场\",\"p\":[],\"b\":\"orange\",\"d\":\"\",\"c\":1,\"o\":9},{\"id\":\"e10\",\"t\":\"可怜\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":9},{\"id\":\"e11\",\"t\":\"拳打脚踢\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":10},{\"id\":\"e12\",\"t\":\"惊讶\",\"p\":[],\"b\":\"purple\",\"d\":\"\",\"c\":1,\"o\":11},{\"id\":\"e13\",\"t\":\"打招呼\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":12},{\"id\":\"e14\",\"t\":\"可爱\",\"p\":[],\"b\":\"pink\",\"d\":\"\",\"c\":1,\"o\":13},{\"id\":\"e15\",\"t\":\"呕吐\",\"p\":[],\"b\":\"brown\",\"d\":\"\",\"c\":1,\"o\":14},{\"id\":\"e16\",\"t\":\"大哭\",\"p\":[],\"b\":\"orange\",\"d\":\"\",\"c\":1,\"o\":15},{\"id\":\"e27\",\"t\":\"重口味\",\"p\":[],\"b\":\"brown\",\"d\":\"\",\"c\":1,\"o\":16},{\"id\":\"e18\",\"t\":\"暧昧示爱\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":17},{\"id\":\"e19\",\"t\":\"装酷\",\"p\":[],\"b\":\"purple\",\"d\":\"\",\"c\":1,\"o\":18},{\"id\":\"e20\",\"t\":\"搞笑\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":19},{\"id\":\"e21\",\"t\":\"无语\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":20},{\"id\":\"e22\",\"t\":\"无奈\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":21},{\"id\":\"e23\",\"t\":\"画圈圈\",\"p\":[],\"b\":\"purple\",\"d\":\"\",\"c\":1,\"o\":22},{\"id\":\"e24\",\"t\":\"害羞\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":23},{\"id\":\"e26\",\"t\":\"发怒暴走\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":24},{\"id\":\"e25\",\"t\":\"得意\",\"p\":[],\"b\":\"pink\",\"d\":\"\",\"c\":1,\"o\":25},{\"id\":\"e17\",\"t\":\"路过\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":26},{\"id\":\"e28\",\"t\":\"惊恐\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":27},{\"id\":\"e29\",\"t\":\"尴尬\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":28},{\"id\":\"e30\",\"t\":\"骂人\",\"p\":[],\"b\":\"brown\",\"d\":\"\",\"c\":1,\"o\":29},{\"id\":\"e31\",\"t\":\"抓狂\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":30},{\"id\":\"e32\",\"t\":\"拖走\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":31},{\"id\":\"e33\",\"t\":\"十八禁\",\"p\":[],\"b\":\"orange\",\"d\":\"\",\"c\":1,\"o\":32},{\"id\":\"e34\",\"t\":\"好冷好雷\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":33},{\"id\":\"e35\",\"t\":\"无聊\",\"p\":[],\"b\":\"purple\",\"d\":\"\",\"c\":1,\"o\":34},{\"id\":\"e36\",\"t\":\"NO\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":35},{\"id\":\"e37\",\"t\":\"疑问\",\"p\":[],\"b\":\"pink\",\"d\":\"\",\"c\":1,\"o\":36},{\"id\":\"e38\",\"t\":\"鄙视\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":37},{\"id\":\"e39\",\"t\":\"顶支持\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":38},{\"id\":\"e40\",\"t\":\"血腥残忍\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":39},{\"id\":\"e41\",\"t\":\"好困啊\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":40},{\"id\":\"e42\",\"t\":\"调皮\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":41},{\"id\":\"e43\",\"t\":\"推倒\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":42},{\"id\":\"e44\",\"t\":\"晕倒\",\"p\":[],\"b\":\"brown\",\"d\":\"\",\"c\":1,\"o\":43},{\"id\":\"e45\",\"t\":\"跳舞\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":44},{\"id\":\"e46\",\"t\":\"吃饭\",\"p\":[],\"b\":\"orange\",\"d\":\"\",\"c\":1,\"o\":45},{\"id\":\"e47\",\"t\":\"潜水\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":46},{\"id\":\"e48\",\"t\":\"好囧\",\"p\":[],\"b\":\"brown\",\"d\":\"\",\"c\":1,\"o\":47},{\"id\":\"e49\",\"t\":\"撒花\",\"p\":[],\"b\":\"magenta\",\"d\":\"\",\"c\":1,\"o\":48},{\"id\":\"e50\",\"t\":\"忙碌\",\"p\":[],\"b\":\"blue\",\"d\":\"\",\"c\":1,\"o\":49},{\"id\":\"e51\",\"t\":\"发呆\",\"p\":[],\"b\":\"purple\",\"d\":\"\",\"c\":1,\"o\":50}]";
	public static final String E1 = "{\"id\":\"e1\",\"t\":\"大笑\",\"p\":[],\"b\":\"red\",\"d\":\"\",\"c\":1,\"o\":1}";
	//分享到QQ空间
	public static final String SHARE_COMMENT = "";
	//常见问题
	public static final String SETTING_PROBLEM = "[{\"p\":\"为什么无法发送表情?\",\"a\":\"首先请您将设备上的微信升级至最新版本(微信4.3以下的版本是不支持发送动态表情的)。如果您的微信已经是最新版本,请您尝试重启一下设备。如果重启设备后仍然无法发送表情,那么建议您重新安装一下微信.\"},{\"p\":\"如何把微信表情包加为微信插件？\",\"a\":\"具体设置请见【设置】-【使用说明】\"}]";		
	/**
	 * 图片源后缀
	 */
	public static final String PIC_ITEM_PREFIX = "/460";
	public static final String PIC_ITEM_SMALL_PREFIX = "/160";
	public static final String PIC_ITEM_FULL_PREFIX = "/2000";
	public static final String PIC_HEAD_PREFIX = "/100";
	public static final String PIC_HEAD_SMALL_PREFIX = "/50";
	
	public static final String INTENT_PIC_URL="INTENT_PIC_URL";
	public static final String INTENT_PIC_NAME = "INTENT_PIC_NAME";
	public static final String INTENT_TITLE = "INTENT_TITLE";
	public static final String INTENT_TEXT="INTENT_TEXT";
	public static final String INTENT_PIC_PARMS="INTENT_PIC_PARMS";
	public static final String INTENT_PIC_ARRAY = "INTENT_PIC_ARRAY";
	public static final String INTENT_TWEETID = "INTENT_TWEETID";
	public static final String INTENT_REQUESTOBJ = "INTENT_REQUESTOBJ";
	public static final String INTENT_MENU_ID = "INTENT_MENU_ID";
	public static final String INTENT_EMOTION_MODE = "INTENT_EMOTION_MODE";
	public static final String INTENT_ADINFO = "INTENT_ADINFO";
	public static final String INTENT_WAPS_AD_DETAIL_HASMORE = "INTENT_WAPS_ADDETAIL_HASMORE";
	public static final String INTENT_USER_DEFINE_ADD_FILE_ID = "INTENT_USER_DEFINE_ADD_FILE_ID";
	public static final String INTENT_USER_DEFINE_DBID = "INTENT_USER_DEFINE_DBID";
	public static final String INTENT_PUSH_EMOJID = "INTENT_PUSH_EMOJID";
	public static final String INTENT_PUSH_FROM_NOTIFY = "INTENT_PUSH_FROM_NOTIFY";
	/**
	 * get-从微信请求 send-发送到微信 pick
	 */
	public static final String INTENT_EMOJ_ACTION = "INTENT_EMOJ_ACTION";
	
	public static final int REQUEST_COMMON_EMOJ = 11;
	/**
	 * 自定义表情添加文件
	 */
	public static final int REQUEST_ADD_FILE = 12;
	/**
	 * 添加频道
	 */
	public static final int REQUEST_ADD_CHANNEL = 13;
}
