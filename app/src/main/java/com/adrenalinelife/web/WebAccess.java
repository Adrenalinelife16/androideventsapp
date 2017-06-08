package com.adrenalinelife.web;

import android.net.Uri;

import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * The Class WebAccess.
 */
public class WebAccess
{
	
	/** The base url. */
	public static String BASE_URL = "http://www.adrenalinelife.org/";
	
	/** The Constant PAGING. */
	protected static final String PAGING = "page/@@/pagesize/$$";

	/** The Constant FEED_URL. */
	protected static final String FEED_URL = "webservices/tweet-fb.php";
	
	/** The Constant EVENT_LIST_URL. */
	protected static final String EVENT_LIST_URL = "Adrenaline_Custom/api/getEvents/";
	//"Adrenaline_Custom/api/getEvents/";

	// protected static final String EVENT_BY_MONTH_URL =
	// "api/getEventsByMonth/month/@@/year/$$";
	/** The Constant EVENT_BY_MONTH_URL. */
	protected static final String EVENT_BY_MONTH_URL = "api/getEventsByMonth";
	
	/** The Constant CHECK_BOOK_URL. */
	protected static final String CHECK_BOOK_URL = "api/getUserTickets/checkBook";
	
	/** The Constant LOGIN_URL. */
	protected static final String LOGIN_URL = "api/Login";
	
	/** The Constant CREATE_EVENT_URL. */
	protected static final String CREATE_EVENT_URL = "http://www.adrenalinelife.org/Adrenaline_Custom/add_event.php";

	/** The Constant REGISTER_URL. */
	protected static final String REGISTER_URL = "Adrenaline_Custom/api/Register";
	
	/** The Constant BOOK_TKT_URL. */
	protected static final String BOOK_TKT_URL = "api/getUserTickets/bookTicket";
	
	/** The Constant TKT_LIST_URL. */
	protected static final String TKT_LIST_URL = "api/getUserTickets";

	public static final String GET_FAV_EVENTS = "Adrenaline_Custom/api/getFavEvents";

	protected static final String ADD_REMOVE_FAV = "Adrenaline_Custom/api/addRemoveFav";

	protected static final String USER_HAS_FAV_EVENT = "Adrenaline_Custom/api/isFavourite";
	/**
	 * Execute request.
	 *
	 * @param restUrl the rest url
	 * @param param the param
	 * @param save the save
	 * @return the input stream
	 */
	public static String executePostRequest(String restUrl, ArrayList<NameValuePair> param, boolean save)
	{

		if (param == null)
			param = new ArrayList<NameValuePair>();
		try
		{
			if (Utils.isOnline())
			{
				HttpPost post = new HttpPost(BASE_URL + restUrl);
				post.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
				HttpResponse res = new DefaultHttpClient().execute(post);
				if (res != null)
				{
					String strRes = EntityUtils.toString(res.getEntity());
					Log.e("URL=" + BASE_URL + restUrl);
					Log.e("PARAM=" + param.toString());
					Log.e("RES=" + strRes);
					if (strRes != null)
					{
						if (save)
							saveToFile(restUrl, param, strRes);
						return strRes;
					}
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return save ? loadFromFile(restUrl, param) : null;

	}

	protected static String executePostRequest_2(String restUrl, ArrayList<NameValuePair> param, boolean save)
	{

		if (param == null)
			param = new ArrayList<NameValuePair>();
		try
		{
			if (Utils.isOnline())
			{

				URL url = new URL(BASE_URL + restUrl);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setRequestProperty(String.valueOf(param), "UTF-8");
				connection.setRequestMethod("POST");
				connection.setDoInput(true);
				connection.connect();
				String res = connection.getResponseMessage();
				//HttpPost post = new HttpPost(BASE_URL + restUrl);
				//post.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
				//HttpResponse res = new DefaultHttpClient().execute(connection);
				if (res != null)
				{
					//String strRes = EntityUtils.toString(res);
					Log.e("URL=" + BASE_URL + restUrl);
					Log.e("PARAM=" + param.toString());
					Log.e("RES=" + res);
					if (res != null)
					{
						if (save)
							saveToFile(restUrl, param, res);
						return res;
					}
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return save ? loadFromFile(restUrl, param) : null;

	}

	/**
	 * Execute get request.
	 *
	 * @param restUrl the rest url
	 * @param save the save
	 * @return the string
	 */
	protected static String executeGetRequest(String restUrl, boolean save)
	{
		/*long time = System.currentTimeMillis();
		if (save && time - StaticData.pref.getLong(restUrl, 0) < Const.TEN_MIN)
		{
			String str = loadFromFile(restUrl, null);
			if (str != null)
				return str;
		}*/
		try
		{

			if (Utils.isOnline())
			{
				HttpGet get = new HttpGet(BASE_URL + restUrl);
				HttpResponse res = new DefaultHttpClient().execute(get);
				if (res != null)
				{
					String strRes = EntityUtils.toString(res.getEntity());
					Log.e("URL = " + BASE_URL + restUrl);
					// Log.e("PARAM="+param.toString());
					Log.e("RES = " + strRes);
					if (strRes != null)
					{
						if (save)
							saveToFile(restUrl, null, strRes);
						return strRes;
					}
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return save ? loadFromFile(restUrl, null) : null;

	}

	/**
	 * Gets the page params.
	 *
	 * @param url the url
	 * @param page the page
	 * @param size the size
	 * @return the page params
	 */
	protected static String getPageParams(String url, int page, int size)
	{
		page = page + 1;
		String str = PAGING.replace("$$", size + "").replace("@@", page + "");
		return url + str;
	}

	/**
	 * Gets the user params.
	 *
	 * @return the user params
	 */
	public static ArrayList<NameValuePair> getUserParams()
	{
		ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("user_id", StaticData.pref.getString(
				Const.USER_ID, null)));
		return param;
	}

	/**
	 * Save to file.
	 *
	 * @param restUrl the rest url
	 * @param param the param
	 * @param res the res
	 */
	private static void saveToFile(final String restUrl,
			final ArrayList<NameValuePair> param, final String res)
	{
		StaticData.pref.edit().putLong(restUrl, System.currentTimeMillis())
				.commit();
		new Thread(new Runnable() {
			@Override
			public void run()
			{

				try
				{
					File f = getFile(restUrl, param);
					DataOutputStream dout = new DataOutputStream(
							new FileOutputStream(f));
					dout.writeUTF(res);
					dout.flush();
					dout.close();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Load from file.
	 *
	 * @param restUrl the rest url
	 * @param param the param
	 * @return the string
	 */
	private static String loadFromFile(String restUrl,
			ArrayList<NameValuePair> param)
	{

		try
		{
			File f = getFile(restUrl, param);
			DataInputStream din = new DataInputStream(new FileInputStream(f));
			String res = din.readUTF();
			din.close();
			if (res != null && res.length() == 0)
				return null;
			return res;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the file.
	 *
	 * @param restUrl the rest url
	 * @param param the param
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static final File getFile(String restUrl,
			ArrayList<NameValuePair> param) throws IOException
	{

		StringBuffer b = new StringBuffer(restUrl);
		if (param != null)
		{
			for (NameValuePair pair : param)
				b.append("/" + pair.getValue());
		}

		File f = new File(Const.WEB_DIR, Uri.encode(b.toString()));
		if (!f.exists())
			f.createNewFile();
		return f;
	}

}
