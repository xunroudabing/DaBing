package com.dabing.emoj.utils;

import org.apache.http.message.BasicNameValuePair;

import com.tencent.weibo.api.BasicAPI;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.utils.QArrayList;
import com.tencent.weibo.utils.QHttpClient;
import com.tencent.weibo.utils.QStrOperate;

public class DaBingRequest extends BasicAPI {
	static final String format = "json";
	static final String TAG = DaBingRequest.class.getSimpleName();
	public DaBingRequest(String OAuthVersion){
		super(OAuthVersion);
	}
	public DaBingRequest(String OAuthVersion, QHttpClient qHttpClient) {
		super(OAuthVersion, qHttpClient);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setAPIBaseUrl(String apiBaseUrl) {
		// TODO Auto-generated method stub
		this.apiBaseUrl = apiBaseUrl;
	}
	/**
	 * 话题时间线
	 * @param oAuth
	 * @param ht 话题内容，长度有限制。
	 * @param reqnum 请求数量（1-100） 
	 * @param pageflag pageflag=1表示向下翻页：tweetid和time是上一页的最后一个帖子ID和时间； 
			  pageflag=2表示向上翻页：tweetid和time是下一页的第一个帖子ID和时间；
	 * @param tweetid 微博帖子ID（详细用法见pageflag） 
	 * @param time 微博帖子生成时间（详细用法见pageflag） 
	 * @param contenttype 正文类型（按位使用）。1-带文本(这一位一定有)，2-带链接，4-带图片，8-带视频，0x10-带音频 
			      建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80 

	 * @return
	 * @throws Exception
	 */
	public String getHt_timeline(OAuth oAuth,String ht,String reqnum,String pageflag,String tweetid,String time,String contenttype) throws Exception{
		final String url = this.apiBaseUrl + "/statuses/ht_timeline_ext";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("httext", ht));
		parms.add(new BasicNameValuePair("htid", "0"));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("tweetid", tweetid));
		parms.add(new BasicNameValuePair("time", time));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		parms.add(new BasicNameValuePair("type", "1"));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 话题时间线
	 * @param oAuth
	 * @param httext 话题内容，长度有限制。
	 * @param reqnum 请求数量（1-100） 
	 * @param pageflag pageflag=1表示向下翻页：tweetid和time是上一页的最后一个帖子ID和时间； 
			  pageflag=2表示向上翻页：tweetid和time是下一页的第一个帖子ID和时间；
	 * @param flag 是否拉取认证用户，用作筛选。0-拉取所有用户，0x01-拉取认证用户 
	 * @param tweetid
	 * @param time
	 * @param type 1-原创发表，2-转载 
	 * @param contenttype 正文类型（按位使用）。1-带文本(这一位一定有)，2-带链接，4-带图片，8-带视频，0x10-带音频 
			      建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80 
	 * @return
	 * @throws Exception
	 */
	public String getHt_timeline(OAuth oAuth,String httext,String reqnum,String pageflag,String flag,String tweetid,String time,String type,String contenttype) throws Exception{
		final String url = this.apiBaseUrl + "/statuses/ht_timeline_ext";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("httext", httext));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("flag", flag));
		parms.add(new BasicNameValuePair("tweetid", tweetid));
		parms.add(new BasicNameValuePair("time", time));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		parms.add(new BasicNameValuePair("type", type));
		return this.requestAPI.getResource(url, parms, oAuth);
	}
	/**
	 * 获取话题ID
	 * @param oAuth
	 * @param ht 话题名字列表，用“,”分隔，如abc,efg（最多30个） 
	 * @return 
	 * @throws Exception
	 */
	public String getHt_ids(OAuth oAuth,String ht) throws Exception{
		final String url = this.apiBaseUrl + "/ht/ids";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("httexts", ht));
		return this.requestAPI.getResource(url, parms, oAuth);
	}
	/**
	 * 获取微群数据
	 * @param oAuth
	 * @param grouId 需要拉取主页微博消息的微群ID列表，多个微群ID之间使用逗号“,”分隔，如123,456
	 * @param zoneId 区域标识符。1：表示获取共享区的信息；2：表示获取灌水区的信息。 
	 * @param pageflag 分页标识。0：第一页；1：向下翻页；2：向上翻页。
	 * @param pagetime 本页起始时间。第一页：0；向下翻页：上一次请求返回的最后一条记录时间；向上翻页：上一次请求返回的第一条记录的时间。
	 * @param reqnum 每次请求记录的条数。取值为1-70条。
	 * @param lastid 上次查询的最后一个微博的ID。
	 * @param contenttype 获取的微博的内容类型。0: 所有类型；0x1：带文本的微博；0x2：带链接的微博；0x4：带图片的微博；0x8：带视频的微博；0x10：带音频的微博。
	 * @param type 获取微博的类型。0：所有类型；0x1：原创发表；0x2：转播；0x8：回复；0x10：没有内容的回复；0x20：提及；0x40：评论。如果需要拉取多种类型的微博，请使用“|”，如要拉取原创发表和转播，(0x1|0x2)得到0x3，此时type=0x3即可。
	 * @return
	 * @throws Exception 
	 */
	public String getWeiBoGroupTimeLine(OAuth oAuth,String grouId,String zoneId,String pageflag,String pagetime,String reqnum,String lastid,String contenttype,String type) throws Exception{
		final String url = "http://113.108.20.23/v3" + "/tgroup/get_multihome_timeline";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("ids", grouId));
		parms.add(new BasicNameValuePair("zoneid", zoneId));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("lastid", lastid));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		parms.add(new BasicNameValuePair("type", type));
		return this.requestAPI.getResource(url, parms, oAuth);
	}
	/**
	 * 微群时间线
	 * @param oAuth
	 * @param grouId 需要拉取主页微博消息的微群id列表，多个微群ID之间使用逗号“,”分隔，如123,456 
	 * @param zoneId 区域标识符。1-表示共享区，2-表示灌水区 
	 * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页） 
	 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间） 
	 * @param reqnum 每次请求记录的条数（1-70条） 
	 * @param lastid 上次查询的最后一个微博id 
	 * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频 
	 * @param type 拉取类型 
				0x1 原创发表 
				0x2 转载 
				0x8 回复 
				0x10 空回 
				0x20 提及 
				0x40 点评 
				如需拉取多个类型请使用|，如(0x1|0x2)得到3，此时type=3即可，填零表示拉取所有类型 
	 * @return
	 * @throws Exception
	 */
	public String getWeiQunTimeLine(OAuth oAuth,String grouId,String zoneId,String pageflag,String pagetime,String reqnum,String lastid,String contenttype,String type) throws Exception{
		final String url = this.apiBaseUrl + "/weiqun/multihome_timeline";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("ids", grouId));
		parms.add(new BasicNameValuePair("zoneid", zoneId));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("lastid", lastid));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		parms.add(new BasicNameValuePair("type", type));
		return this.requestAPI.getResource(url, parms, oAuth);
	}
	/**
	 * 获取用户资料
	 * @param oAuth
	 * @return
	 * @throws Exception
	 */
	public String getUserInfo(OAuth oAuth) throws Exception{
		final String url = this.apiBaseUrl + "/user/info";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		return this.requestAPI.getResource(url, parms, oAuth);
	}
	/**
	 * 获取评论转播列表
	 * @param oAuth
	 * @param flag 类型标识。0－转播列表，1－点评列表，2－点评与转播列表 
	 * @param rootid 转发或回复的微博根结点id（源微博id） 
	 * @param pageflag 分页标识，用于翻页（0：第一页，1：向下翻页，2：向上翻页） 
	 * @param pagetime 本页起始时间，与pageflag、twitterid共同使用，实现翻页功能（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间） 
	 * @param reqnum 每次请求记录的条数（1-100条） 
	 * @param twitterid 微博id，与pageflag、pagetime共同使用，实现翻页功能（第1页填0，继续向下翻页，填上一次请求返回的最后一条记录id） 
	 * @return
	 * @throws Exception
	 */
	public String getComment(OAuth oAuth,String flag,String rootid,String pageflag,String pagetime,String reqnum,String twitterid) throws Exception{
		final String url = this.apiBaseUrl + "/t/re_list";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("flag", flag));
		parms.add(new BasicNameValuePair("rootid", rootid));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("twitterid", twitterid));
		return this.requestAPI.getResource(url, parms, oAuth);
	}
	/**
	 * 
	 * @param oauth
	 * @param content
	 * @param ip
	 * @param flag
	 * @param issue
	 * @param groupid
	 * @return
	 * @throws Exception
	 */
	public String add_weiqun(OAuth oauth,String content,String ip,String flag,String issue,String groupid) throws Exception{
		final String urlString="http://open.t.qq.com/api/weiqun/add";
		QArrayList parms=new QArrayList();
		parms.add(new BasicNameValuePair("format", "json"));
		parms.add(new BasicNameValuePair("content", content));
		parms.add(new BasicNameValuePair("clientip", ip));
		parms.add(new BasicNameValuePair("jing", ""));
		parms.add(new BasicNameValuePair("wei", ""));
		parms.add(new BasicNameValuePair("flag", flag));
		parms.add(new BasicNameValuePair("issue", issue));
		parms.add(new BasicNameValuePair("groupid", groupid));
		return this.requestAPI.postContent(urlString, parms, oauth);
	} 
	
	public String add_weiqun(OAuth oauth,String content,String ip,String flag,String issue,String groupid,String filepath) throws Exception{
		final String urlString="http://open.t.qq.com/api/weiqun/add_pic";
		QArrayList parms=new QArrayList();
		parms.add(new BasicNameValuePair("format", "json"));
		parms.add(new BasicNameValuePair("content", content));
		parms.add(new BasicNameValuePair("clientip", ip));
		parms.add(new BasicNameValuePair("jing", ""));
		parms.add(new BasicNameValuePair("wei", ""));
		parms.add(new BasicNameValuePair("flag", flag));
		parms.add(new BasicNameValuePair("issue", issue));
		parms.add(new BasicNameValuePair("groupid", groupid));
		QArrayList pic = new QArrayList();
		pic.add(new BasicNameValuePair("pic", filepath));
		return this.requestAPI.postFile(urlString, parms, pic, oauth);
	} 
	/**
	 * 获取微博详细
	 * @param oAuth
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String getWeibo(OAuth oAuth,String id) throws Exception{
		final String url = this.apiBaseUrl + "/t/show";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", "json"));
		parms.add(new BasicNameValuePair("id", id));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 多用户时间线
	 * @param oAuth
	 * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页） 
	 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间） 
	 * @param reqnum 每次请求记录的条数（1-70条）  
	 * @param lastid 和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id） 
	 * @param names 你需要读取用户列表用“,”隔开，例如：abc,bcde,effg（可选，最多30个） 
	 * @param type 拉取类型 
				0x1 原创发表 
				0x2 转载 
				如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型 
	 * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频 
				建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80 
	 * @return
	 * @throws Exception
	 */
	public String getMultiUserTimeLine(OAuth oAuth,String pageflag,String pagetime,String reqnum,String lastid,String names,String type,String contenttype) throws Exception{
		final String url = this.apiBaseUrl + "/statuses/users_timeline";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("lastid", lastid));
		parms.add(new BasicNameValuePair("names", names));
		parms.add(new BasicNameValuePair("type", type));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 其他用户发表时间线
	 * @param oAuth
	 * @param pageflag
	 * @param pagetime
	 * @param reqnum
	 * @param lastid
	 * @param names
	 * @param type
	 * @param contenttype
	 * @return
	 * @throws Exception
	 */
	public String getOtherUserTimeLine(OAuth oAuth,String pageflag,String pagetime,String reqnum,String lastid,String name,String fopenid,String type,String contenttype) throws Exception{
		final String url = this.apiBaseUrl + "/statuses/user_timeline";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("lastid", lastid));
		parms.add(new BasicNameValuePair("name", name));
		parms.add(new BasicNameValuePair("fopenid", fopenid));
		parms.add(new BasicNameValuePair("type", type));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 主页时间线
	 * @param oAuth
	 * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页） 
	 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间） 
	 * @param reqnum 每次请求记录的条数（1-70条） 
	 * @param type 拉取类型（需填写十进制数字） 
		0x1 原创发表 0x2 转载 如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型 
	 * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频 建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80 
	 * @return
	 * @throws Exception
	 */
	public String getHomeTimeLine(OAuth oAuth,String pageflag,String pagetime,String reqnum,String type,String contenttype) throws Exception{
		final String url = this.apiBaseUrl + "/statuses/home_timeline";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("type", type));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 用户提及时间线
	 * @param oAuth
	 * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页） 
	 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间） 
	 * @param reqnum 每次请求记录的条数（1-70条） 
	 * @param lastid 用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id） 
	 * @param type  拉取类型 
		0x1 原创发表 
		0x2 转载 
		0x8 回复 
		0x10 空回 
		0x20 提及 
		0x40 点评 
		如需拉取多个类型请使用|，如(0x1|0x2)得到3，则type=3即可，填零表示拉取所有类型 
	 * @param contenttype 内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频 
		建议不使用contenttype为1的类型，如果要拉取只有文本的微博，建议使用0x80 
	 * @return
	 * @throws Exception
	 */
	public String getMentionsTimeline(OAuth oAuth,String pageflag,String pagetime,String reqnum,String lastid,String type,String contenttype) throws Exception{
		final String url = this.apiBaseUrl + "/statuses/mentions_timeline";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("lastid", lastid));
		parms.add(new BasicNameValuePair("type", type));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 获取私信
	 * @param oAuth
	 * @param pageflag
	 * @param pagetime
	 * @param reqnum
	 * @param lastid
	 * @param contenttype
	 * @return
	 * @throws Exception
	 */
	public String getRecv(OAuth oAuth,String pageflag,String pagetime,String reqnum,String lastid,String contenttype) throws Exception{
		final String url = this.apiBaseUrl + "/private/recv";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("lastid", lastid));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 获取其他用户信息
	 * @param oAuth
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String getOtherInfo(OAuth oAuth,String name,String openid) throws Exception{
		final String url = this.apiBaseUrl + "/user/other_info";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		if(!name.equals("")){
			parms.add(new BasicNameValuePair("name", name));
		}
		if(!openid.equals("")){
			parms.add(new BasicNameValuePair("fopenid", openid));
		}
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 数据更新查看
	 * @param oAuth
	 * @param op 请求类型 
		0-仅查询，1-查询完毕后将相应计数清0 
	 * @param type 5-首页未读消息计数，6-@页未读消息计数，7-私信页消息计数，8-新增听众数，9-首页广播数（原创的） 
		op=0时，type默认为0，此时返回所有类型计数；op=1时，需带上某种类型的type，清除该type类型的计数，并返回所有类型计数 
	 * @return
	 * @throws Exception
	 */
	public String getUpdate(OAuth oAuth,String op,String type) throws Exception{
		final String url = this.apiBaseUrl + "/info/update";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("op", op));
		parms.add(new BasicNameValuePair("type", type));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	//****************名单相关********************
	/**
	 * 名单/查询名单成员列表返回
	 */
	public String getListUsers(OAuth oAuth,String listid,String pageflag) throws Exception{
		final String url = this.apiBaseUrl + "/list/listusers";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("listid", listid));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 名单/名单订阅成员列表信息返回 这个没啥用
	 * @param oAuth
	 * @param listid 名单id
	 * @param pageflag 第一页0
	 * @return
	 * @throws Exception
	 */
	public String getListFollowers(OAuth oAuth,String listid,String pageflag) throws Exception{
		final String url = this.apiBaseUrl + "/list/list_followers";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("listid", listid));
		parms.add(new BasicNameValuePair("page", pageflag));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 查询我创建的名单
	 * @param oAuth
	 * @return
	 * @throws Exception
	 */
	public String getList(OAuth oAuth) throws Exception{
		final String url = this.apiBaseUrl + "/list/get_list";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 取名单属性
	 * @param oAuth
	 * @param listids
	 * @return
	 * @throws Exception
	 */
	public String getListAttr(OAuth oAuth,String listids) throws Exception{
		final String url = this.apiBaseUrl + "/list/list_attr";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("listids", listids));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 收录指定用户到名单返回
	 * @param oAuth
	 * @param names 用户账号列表，以“,”分隔，如aaa,bbb（可选，最多不超过8个） 
	 * @param fopenids 你需要读取的用户openid列表，用下划线“_”隔开，例如：B624064BA065E01CB73F835017FE96FA_B624064BA065E01CB73F835017FE96FB（可选，最多不超过8个） 
				names和fopenids至少选一个，若同时存在则以names值为主 
	 * @param listid 名单id 42010140760708842
	 * @return
	 * @throws Exception
	 */
	public String addToList(OAuth oAuth,String names,String fopenids,String listid) throws Exception{
		final String url = this.apiBaseUrl + "/list/add_to_list";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("names", names));
		parms.add(new BasicNameValuePair("fopenids", fopenids));
		parms.add(new BasicNameValuePair("listid", listid));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 添加用户到微群
	 * @param oAuth
	 * @param groupid
	 * @param names
	 * @param fopenids
	 * @return
	 * @throws Exception
	 */
	public String addToWeiQun(OAuth oAuth,String groupid,String names,String fopenids) throws Exception{
		final String url = this.apiBaseUrl + "/weiqun/apply4group";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("groupid", groupid));
		if(names != null){
		parms.add(new BasicNameValuePair("names", names));
		}
		if(fopenids != null){
		parms.add(new BasicNameValuePair("fopenids", fopenids));
		}
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	//刷新oauth
	public static String updateAccessToken(String refresh_token) throws Exception{
		final String url = OAuthConstants.OAUTH_V2_GET_ACCESS_TOKEN_URL;
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("client_id", AppConfig.clientId));
		parms.add(new BasicNameValuePair("grant_type", "refresh_token"));
		parms.add(new BasicNameValuePair("refresh_token", refresh_token));
		
		QHttpClient client = new QHttpClient();
		return client.httpGet(url, QStrOperate.getQueryString(parms));
		
	}
	//**********收听**************
	/**
	 * 收听
	 */
	public String add(OAuth oAuth,String name) throws Exception{
		final String url = this.apiBaseUrl + "/friends/add";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("name", name));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	
	public String addSpecial(OAuth oAuth,String name) throws Exception{
		final String url = this.apiBaseUrl + "/friends/addspecial";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("name", name));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 
	 * @param oAuth
	 * @param keyword 搜索关键字（1-128字节） 
	 * @param pagesize 每页大小（1-30个） 
	 * @param page 页码  
	 * @param contenttype 消息的正文类型（按位使用） 
		0-所有，0x01-纯文本，0x02-包含url，0x04-包含图片，0x08-包含视频，0x10-包含音频 
	 * @param sorttype 排序方式 
			  0-表示按默认方式排序(即时间排序(最新)) 
	 * @param msgtype 消息的类型（按位使用） 
			  0-所有，1-原创发表，2 转载，8-回复(针对一个消息，进行对话)，0x10-空回(点击客人页，进行对话) 
	 * @param searchtype 搜索类型 
				0-默认搜索类型（现在为模糊搜索） 
				1-模糊搜索：时间参数starttime和endtime间隔小于一小时，时间参数会调整为starttime前endtime后的整点，即调整间隔为1小时 
				8-实时搜索：选择实时搜索，只返回最近几分钟的微博，时间参数需要设置为最近的几分钟范围内才生效，并且不会调整参数间隔 
	 * @return {"data":null,"errcode":0,"msg":"result is empty","ret":0,"seqid":5805010293287075632}
				{"data":{"hasnext":1,"info":[{"city_code":"24","count":0,"country_code":"1","emotiontype":0,"emotionurl":"","from":"QQ空间分享","fromurl":"http:\/\/rc.qzone.qq.com\/share\/\u000a","geo":"","head":"http:\/\/app.qlogo.cn\/mbloghead\/5d0851162fc2dcf0f7f2","id":"114923039618687","image":["http:\/\/app.qpic.cn\/mblogpic\/d8e044e0f4e752528846"],"isrealname":1,"isvip":0,"latitude":"0","location":"中国 毕节","longitude":"0","mcount":0,"music":null,"name":"walp520chen","nick":"武军","openid":"9578A9ED31DAE784F5FC85BDB9D7B263","origtext":"【美国将送菲律宾一艘退役巡逻舰 成菲最先进军舰  】 (分享自 @Qzone) http:\/\/url.cn\/1E5EON ","province_code":"52","self":0,"source":null,"status":0,"storetime":"1351584289","text":"【美国将送菲律宾一艘退役巡逻舰 成菲最先进军舰  】 (分享自 @Qzone) <a href=\"http:\/\/url.cn\/1E5EON\" target=\"_blank\">http:\/\/url.cn\/1E5EON<\/a> ","timestamp":1351584289,"type":1,"video":null},{"city_code":"1","count":0,"country_code":"1","emotiontype":0,"emotionurl":"","from":"微博小保姆","fromurl":"http:\/\/www.weibobaomu.net\/?qz_height=1024\u000a","geo":"","head":"http:\/\/app.qlogo.cn\/mbloghead\/16aba641c91190cdb9c6","id":"156858088860465","image":["http:\/\/app.qpic.cn\/mblogpic\/26c79d71c4adb663d18c"],"isrealname":1,"isvip":0,"latitude":"0","location":"中国 哈尔滨","longitude":"0","mcount":0,"music":null,"name":"li-fenghong","nick":"幽默搞笑家_Ai枫红先生","openid":"591720362A54002C265FC5865271EC33","origtext":"中国人和美国人一起吹牛。 美国人说我们美国人吃口香糖从来不乱吐，都回收做成避孕套出口中国。 中国人听了说这算什么，我们中国人ML，避孕套从来不乱扔，做成口香糖出口美国。搞笑","province_code":"23","self":0,"source":null,"status":0,"storetime":"1351584284","text":"中国人和美国人一起吹牛。 美国人说我们美国人吃口香糖从来不乱吐，都回收做成避孕套出口中国。 中国人听了说这算什么，我们中国人ML，避孕套从来不乱扔，做成口香糖出口美国。搞笑","timestamp":1351584284,"type":1,"video":null},{"city_code":"","count":0,"country_code":"1","emotiontype":0,"emotionurl":"","from":"手机(t.3g.qq.com)","fromurl":"http:\/\/t.qq.com\/client.php?t=mobile\u000a","geo":"","head":"http:\/\/app.qlogo.cn\/mbloghead\/7a5a758205d85636ca40","id":"147705094654470","image":["http:\/\/app.qpic.cn\/mblogpic\/12d242ed8deb820153ba"],"isrealname":1,"isvip":0,"latitude":"0","location":"中国","longitude":"0","mcount":0,"music":null,"name":"x965400","nick":"未来","openid":"3657F351B47D1E4A325F48F3039FF150","origtext":"呵呵！美国就是馊主意比较多，看不得中国强大，挑逗是非专家，这回顾不上了吧！不会有好结果的。 【评】“史上最强风暴”桑迪逼近美国东海岸 http:\/\/url.cn\/9HicTZ ","province_code":"32","self":0,"source":null,"status":0,"storetime":"1351584282","text":"呵呵！美国就是馊主意比较多，看不得中国强大，挑逗是非专家，这回顾不上了吧！不会有好结果的。 【评】“史上最强风暴”桑迪逼近美国东海岸 <a href=\"http:\/\/url.cn\/9HicTZ\" target=\"_blank\">http:\/\/url.cn\/9HicTZ<\/a> ","timestamp":1351584282,"type":1,"video":null},{"city_code":"","count":0,"country_code":"1","emotiontype":0,"emotionurl":"","from":"手机(t.3g.qq.com)","fromurl":"http:\/\/t.qq.com\/client.php?t=mobile\u000a","geo":"","head":"http:\/\/app.qlogo.cn\/mbloghead\/c77fd4a9fe82535776d0","id":"142951096181518","image":["http:\/\/app.qpic.cn\/mblogpic\/6ee3def31e164bf53acc"],"isrealname":1,"isvip":0,"latitude":"0","location":"中国","longitude":"0","mcount":0,"music":null,"name":"baobeiziran","nick":"宝贝紫然","openid":"92AF79E0614D76A5B3E6D9DA3510EA6A","origtext":"让美国政府也忙忙他们自己的事情吧!只是什么时候最可怜的还是老百姓, 【评】飓风桑迪袭美加致13人死 核电站高度警惕 http:\/\/url.cn\/6uvZjj ","province_code":"14","s

	 * @throws Exception
	 */
	public String searcht(OAuth oAuth,String keyword,String pagesize,String page,String contenttype,String sorttype,String msgtype,String searchtype) throws Exception{
		final String url = this.apiBaseUrl + "/search/t";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("keyword", keyword));
		parms.add(new BasicNameValuePair("pagesize", pagesize));
		parms.add(new BasicNameValuePair("page", page));
		parms.add(new BasicNameValuePair("contenttype", contenttype));
		parms.add(new BasicNameValuePair("sorttype", sorttype));
		parms.add(new BasicNameValuePair("msgtype", msgtype));
		parms.add(new BasicNameValuePair("searchtype", searchtype));
		return this.requestAPI.postContent(url, parms, oAuth);	
		
	}
	/**
	 * 搜索话题
	 * @param oAuth
	 * @param keyword 搜索关键字（1-128字节） 
	 * @param pagesize 本次请求的记录条数（1-20个） 
	 * @param page 请求的页码，从1开始 
	 * @return {"data":{"hasnext":1,"info":[{"favnum":106210,"id":"8199280370551943707","text":"钓鱼岛","tweetnum":3393248},{"favnum":9321,"id":"8760654161495838146","text":"保卫钓鱼岛","tweetnum":320864},{"favnum":19433,"id":"863922225927291538","text":"钓鱼岛是中国的","tweetnum":1079543},{"favnum":5635,"id":"6699082513294934716","text":"怎样开发钓鱼岛","tweetnum":179125},{"favnum":3019,"id":"14184007396556018879","text":"中国军舰驶向钓鱼岛","tweetnum":162151},{"favnum":1728,"id":"3719800549327667638","text":"日本拟购买钓鱼岛","tweetnum":39461},{"favnum":83,"id":"17184192316440587058","text":"登上钓鱼岛","tweetnum":218547},{"favnum":952,"id":"15870231126978082522","text":"日议员考察钓鱼岛","tweetnum":13100},{"favnum":1,"id":"4034245329865988726","text":"钓鱼岛 ","tweetnum":2},{"favnum":0,"id":"2813992653727698088","text":"钓鱼̶̶̶岛̶","tweetnum":2},{"favnum":1,"id":"12439487515383108877","text":"..钓鱼岛","tweetnum":1},{"favnum":0,"id":"5467920152896846806","text":"..............钓鱼岛","tweetnum":1},{"favnum":0,"id":"5833353102024444644","text":"钓鱼岛- -","tweetnum":1},{"favnum":0,"id":"14579104573569817377","text":"钓鱼岛....","tweetnum":1},{"favnum":0,"id":"4001907123624014601","text":"钓鱼岛......","tweetnum":1},{"favnum":0,"id":"7048850059293831227","text":":钓鱼岛","tweetnum":1},{"favnum":0,"id":"7527375689453251137","text":"&#39;钓鱼岛","tweetnum":1},{"favnum":0,"id":"8520341111709866649","text":"钓鱼岛.........","tweetnum":1},{"favnum":0,"id":"12836560944203300860","text":"钓鱼岛 ..","tweetnum":1},{"favnum":1,"id":"1234437149402133005","text":"钓鱼岛&#39;&#39;","tweetnum":1}],"timestamp":1351587141,"totalnum":6950},"errcode":0,"msg":"ok","ret":0,"seqid":5805022568299824140}

	 * @throws Exception
	 */
	public String searchHT(OAuth oAuth,String keyword,String pagesize,String page) throws Exception{
		final String url = this.apiBaseUrl + "/search/ht";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("keyword", keyword));
		parms.add(new BasicNameValuePair("pagesize", pagesize));
		parms.add(new BasicNameValuePair("page", page));
		return this.requestAPI.postContent(url, parms, oAuth);
		
	}
	/**
	 * 搜索用户
	 * @param oAuth
	 * @param keyword 搜索关键字（1-20字节） 
	 * @param pagesize 本次请求的记录条数（1-15个） 
	 * @param page 请求的页码，从1开始 
	 * @return
	 * @throws Exception
	 */
	public String searchUser(OAuth oAuth,String keyword,String pagesize,String page) throws Exception{
		final String url = this.apiBaseUrl + "/search/user";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("keyword", keyword));
		parms.add(new BasicNameValuePair("pagesize", pagesize));
		parms.add(new BasicNameValuePair("page", page));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	
	public String addPicUrl(OAuth oAuth,String content,String clientip,String pic_url) throws Exception{
		final String url = this.apiBaseUrl + "/t/add_pic_url";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("content", content));
		parms.add(new BasicNameValuePair("clientip", clientip));
		parms.add(new BasicNameValuePair("pic_url", pic_url));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 
	 * @param oAuth
	 * @param ids 要查询的微博id列表，用“,”隔开，如123456,456789（请求数量不要超过50个） 
	 * @return
	 * @throws Exception
	 */
	public String getWeiboList(OAuth oAuth,String ids) throws Exception{
		final String url = this.apiBaseUrl+"/t/list";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("ids", ids));
		return this.requestAPI.postContent(url, parms, oAuth);
	}
	/**
	 * 
	 * @param oAuth
	 * @param pageflag 分页标识（0：第一页，1：向下翻页，2：向上翻页） 
	 * @param pagetime 本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
	 * @param lastid  用于翻页，和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id） 
	 * @param name 请求的微频道标签名称 可空
	 * @param id 请求的微频道标签ID,频道Id和名称都可以为空，为空时获取频道首页的时间线 可空
	 * @param reqnum 请求的条数1-100,默认是返回20条
	 * @param type 请求的时间线中微博的类型，0不区分，1原创 2，转播 7评论
	 * @param content_type 请求的时间线中微博的正文类型， 2带链接，4带图片 8带视频 16带音频
	 * @return
	 * @throws Exception 
	 */
	public String getChannelList(OAuth oAuth,String pageflag,String pagetime,String lastid,String name,String id,String reqnum,String type,String content_type) throws Exception{
		final String url = this.apiBaseUrl + "/channel/timeline";
		QArrayList parms = new QArrayList();
		parms.add(new BasicNameValuePair("format", format));
		parms.add(new BasicNameValuePair("pageflag", pageflag));
		parms.add(new BasicNameValuePair("pagetime", pagetime));
		parms.add(new BasicNameValuePair("lastid", lastid));
		if(name != null && !name.equals("")){
			parms.add(new BasicNameValuePair("name", name));
		}
		if(id != null && !id.equals("")){
			parms.add(new BasicNameValuePair("id", id));
		}
		parms.add(new BasicNameValuePair("reqnum", reqnum));
		parms.add(new BasicNameValuePair("type", format));
		parms.add(new BasicNameValuePair("content_type", content_type));
		return this.requestAPI.postContent(url, parms, oAuth);
		
	}
}
