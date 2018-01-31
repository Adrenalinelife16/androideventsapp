package com.adrenalinelife.web;

import android.util.Log;

import com.adrenalinelife.database.DbHelper;
import com.adrenalinelife.model.Event;
import com.adrenalinelife.model.Feed;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.TreeSet;

/**
 * The Class WebHelper class holds all the methods that communicate with server
 * API and parses the results in required Java Bean classes.
 */
public class WebHelper extends WebAccess
{
	public String stringOfFav;
	/**
	 * Gets the adrenalinelife.
	 * 
	 * @param page
	 *            the page
	 * @param pageSize
	 *            the page size
	 * @return the adrenalinelife
	 */
	public static ArrayList<Event> getEvents(int page, int pageSize)
	{
		try
		{
			String url = EVENT_LIST_URL;
			url = getPageParams(url, page, pageSize);
			//url=getUserParams(url);
			String res = executeGetRequest(url, true);
			return parseEvents(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Event> grabFavEvents(int page, int pageSize)
	{
		try
		{
			String url = EVENT_LIST_URL;
			url = getPageParams(url, page, pageSize);
			String res = executeGetRequest(url, true);
			return parseFavEvents(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Parses the adrenalinelife.
	 * 
	 * @param res
	 *            the res
	 * @return the array list
	 * @throws Exception
	 *             the exception
	 */

	public static ArrayList<Event> parseEvents(String res) throws Exception
	{
		Log.e("parseEvents ", res);
		JSONArray obj = new JSONArray(res);

		TreeSet<Event> al = new TreeSet<Event>(new Comparator<Event>() {

			@Override
			public int compare(Event lhs, Event rhs)
			{
				if (lhs.getStartDateTime() == rhs.getStartDateTime())
					return rhs.getTitle().compareTo(lhs.getTitle());
				return rhs.getStartDateTime() > lhs.getStartDateTime() ? -1 : 1;
			}
		});
		for (int i = 0; i < obj.length(); i++)
		{
			JSONObject js = obj.getJSONObject(i);
			if (js.has("status") && js.has("message") && obj.length() == 1)
				return new ArrayList<Event>();
			al.add(parseEvent(js));
		}
		return new ArrayList<Event>(al);
	}

	public static ArrayList<Event> parseFavEvents(String res) throws Exception
	{

		JSONArray obj = new JSONArray(res);

		TreeSet<Event> al = new TreeSet<Event>(new Comparator<Event>() {

			@Override
			public int compare(Event lhs, Event rhs)
			{
				if (lhs.getStartDateTime() == rhs.getStartDateTime())
					return rhs.getTitle().compareTo(lhs.getTitle());
				return rhs.getStartDateTime() > lhs.getStartDateTime() ? -1 : 1;
			}
		});
		for (int i = 0; i < obj.length(); i++)
		{
			JSONObject js = obj.getJSONObject(i);
			if (js.has("status") && js.has("message") && obj.length() == 1)
				return new ArrayList<Event>();
			al.add(parseFavEvent(js));
		}
		return new ArrayList<Event>(al);
	}

	/**
	 * Parses the event.
	 * 
	 * @param js
	 *            the js
	 * @return the event
	 * @throws Exception
	 *             the exception
	 */
	private static Event parseEvent(JSONObject js) throws Exception
	{

		Event e = new Event();
		e.setTitle(js.getString("event_name"));
		e.setId(js.getString("event_id"));
		e.setStartDate(js.getString("event_start_date"));
		e.setStartTime(js.getString("event_start_time"));
		e.setImage(js.getString("event_image_url"));
		e.setDesc(js.getString("event_content"));
		e.setCategory(js.getString("event_category"));
		Log.e("Events category = ", e.getCategory());
		e.setEndDate(js.getString("event_end_date"));
		e.setEndTime(js.getString("event_end_time"));
		e.setLocation(js.getString("location_address") + ", "
				+ js.getString("location_town") + ", "
				+ js.getString("location_state") + ", "
				+ js.getString("location_region") + ", "
				+ js.getString("location_country") + "- "
				+ js.getString("location_postcode"));
		e.setAddress(js.getString("location_address"));
		e.setCitystate(js.getString("location_town") + ", "
				+ js.getString("location_state"));
		e.setZip(js.getString("location_postcode"));
		e.setLocation(e.getLocation().replaceAll(", null", ""));
		e.setLocation(e.getLocation().replaceAll("- null", ""));

		// e.setFav("1".equalsIgnoreCase(js.optString("is_favorite")));
		e.setFav(DbHelper.isEventFavorite(e.getId()));
		e.setLatitude(Commons.strToDouble(js.getString("location_latitude")));
		e.setLongitude(Commons.strToDouble(js.getString("location_longitude")));
		try
		{
			e.setPrice(Commons.strToDouble(js.getJSONObject("ticket")
					.getString("ticket_price")));
			e.setAvailSpace(Commons.strToInt(js.getJSONObject("ticket")
					.getString("avail_spaces")));
		} catch (Exception e2)
		{
			e2.printStackTrace();
		}

		/*while(e.getDesc().startsWith("/n")||e.getDesc().startsWith("/r"))
			e.setDesc(e.getDesc().substring(1));
		while(e.getDesc().endsWith("/n")||e.getDesc().endsWith("/r"))
			e.setDesc(e.getDesc().substring(0,e.getDesc().length()-1));*/
		return e;
	}

	private static Event parseFavEvent(JSONObject js) throws Exception
	{
		Event e = new Event();
		e.setTitle(js.getString("event_name"));
		e.setId(js.getString("event_id"));
		e.setStartDate(js.getString("event_start_date"));
		e.setStartTime(js.getString("event_start_time"));
		e.setImage(js.getString("event_image_url"));
		e.setDesc(js.getString("event_content"));
		e.setEndDate(js.getString("event_end_date"));
		e.setEndTime(js.getString("event_end_time"));
		e.setLocation(js.getString("location_address") + ", "
				+ js.getString("location_town") + ", "
				+ js.getString("location_state") + ", "
				+ js.getString("location_region") + ", "
				+ js.getString("location_country") + "- "
				+ js.getString("location_postcode"));
		e.setLocation(e.getLocation().replaceAll(", null", ""));
		e.setLocation(e.getLocation().replaceAll("- null", ""));

		e.setLatitude(Commons.strToDouble(js.getString("location_latitude")));
		e.setLongitude(Commons.strToDouble(js.getString("location_longitude")));

		try
		{
			e.setPrice(Commons.strToDouble(js.getJSONObject("ticket")
					.getString("ticket_price")));
			e.setAvailSpace(Commons.strToInt(js.getJSONObject("ticket")
					.getString("avail_spaces")));
		} catch (Exception e2)
		{
			e2.printStackTrace();
		}

		return e;
	}

	/**
	 * Parses the date.
	 * 
	 * @param tw
	 *            the tw
	 * @param str
	 *            the str
	 * @return the long
	 */
	private static long parseDate(boolean tw, String str)
	{
		String format = tw ? "EEE MMM dd kk:mm:ss Z yyyy"
				: "yyyy-MM-dd'T'kk:mm:ssZ";
		try
		{
			return new SimpleDateFormat(format, Locale.US).parse(str).getTime();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return System.currentTimeMillis();
	}

	/**
	 * Gets the feed list.
	 * 
	 * @return the feed list
	 */
	public static ArrayList<Feed> getFeedList()
	{
		TreeSet<Feed> al = new TreeSet<Feed>(new Comparator<Feed>() {

			@Override
			public int compare(Feed lhs, Feed rhs)
			{
				if (lhs.getDate() == rhs.getDate())
					return 1;
				return rhs.getDate() > lhs.getDate() ? 1 : -1;
			}
		});
		try
		{
			String res = executePostRequest(FEED_URL, null, true);
			JSONObject obj = new JSONObject(res);
			JSONArray arr = obj.getJSONArray("instagram_feeds");
/*
			JSONArray arr = obj.getJSONArray("twitter_feeds");
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject js = arr.getJSONObject(i);
				Feed f = new Feed();
				f.setType(Feed.FEED_TW);
				f.setDate(parseDate(true, js.getString("created_at")));
				f.setImage(js.getJSONObject("user").optString(
						"profile_image_url"));
				f.setLink(js.optString("tweet_url"));
				f.setMsg(js.optString("text"));
				f.setTitle(js.getJSONObject("user").optString("name"));

				al.add(f);
			}

			arr = obj.getJSONArray("fb_feeds");
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject js = arr.getJSONObject(i);
				Feed f = new Feed();
				f.setType(Feed.FEED_FB);
				f.setDate(parseDate(false, js.getString("created_time")));
				f.setImage(js.optString("picture"));
				f.setLink(js.optString("link"));
				f.setMsg(js.optString("message"));
				f.setTitle(js.optString(""));

				al.add(f);
			}
*/
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject js = arr.getJSONObject(i);
				Feed f = new Feed();
				f.setType(Feed.FEED_IG);
				f.setDate(Long.parseLong(js.getString("created_time")) * 1000);
				f.setImage(js.optJSONObject("images")
						.getJSONObject("low_resolution").getString("url"));
				f.setLink(js.optString("link"));
				f.setMsg(js.optString("text"));
				if (Commons.isEmpty(f.getMsg()))
					f.setMsg("@"
							+ js.optJSONObject("user").getString("username"));
				f.setTitle(js.optJSONObject("user").getString("full_name"));

				al.add(f);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return new ArrayList<Feed>(al);
	}

	/**
	 * Gets the adrenalinelife by month.
	 * 
	 * @param month
	 *            the month
	 * @param year
	 *            the year
	 * @return the adrenalinelife by month
	 */
	public static ArrayList<Event> getEventsByMonth(String month, String year)
	{
		try
		{
			Log.e("get events by month", month + "/" + year);
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("month", month));
			Log.e("param", month);
			param.add(new BasicNameValuePair("year", year));
			String res = executePostRequest(EVENT_BY_MONTH_URL, param, true);
			Log.e("execute post request", res);
			return parseEvents(res);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Event> getFavoriteEvents(int page, int pageSize)
	{
		try
		{
			page++;
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("page", page + ""));
			param.add(new BasicNameValuePair("page_size", pageSize + ""));
            String res = executePostRequest(GET_FAV_EVENTS, param, true);
			Log.e("String Res ", res);

			return parseEvents(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<Event> addRemoveFavorite(String event_id)
	{
		try
		{
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("event_id", event_id));
			String res = executePostRequest(ADD_REMOVE_FAV, param, true);
			return parseEvents(res);
		} catch (Exception e)
		{
			Log.e("Exception Caught ", e.getMessage());
			e.printStackTrace();

		}
		return null;
	}

	public static boolean isFavoriteEvent(Event e)
	{
		try
		{
			String event_id = e.getId();
            ArrayList<NameValuePair> param = getUserParams();
			param.clear();
            //param.add(new BasicNameValuePair("event_id", e.getId()));
			param.add(new BasicNameValuePair("user_id", "24"));
			param.add(new BasicNameValuePair("event_id", event_id));
			String res = executePostRequest(USER_HAS_FAV_EVENT, param, true);
			e.setFav(new Status(new JSONArray(res).getJSONObject(0)).isSuccess());
			return e.isFav();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
        com.adrenalinelife.utils.Log.e("Is Fav Event? ", e.isFav());
		return false;
	}
	/**
	 * Checks if is booked.
	 * 
	 * @param e
	 *            the e
	 * @return true, if is booked
	 */
	public static boolean isBooked(Event e)
	{
		try
		{
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("event_id", e.getId()));
			String res = executePostRequest(CHECK_BOOK_URL, param, true);
			e.setBooked(new Status(new JSONArray(res).getJSONObject(0))
					.isSuccess());
			return e.isBooked();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Do login.
	 * 
	 * @param email
	 *            the email
	 * @param pwd
	 *            the pwd
	 * @return the status
	 */
	public static Status doLogin(String email, String pwd)
	{
		try
		{
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("email", email));
			param.add(new BasicNameValuePair("pwd", pwd));
			String res = executePostRequest(LOGIN_URL, param, false);
			Log.v("Res", res);
			return new Status(res, "user_id");

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return new Status();
	}
	/**
	 * Do Create Event.
	 *
	 * @param event_name
	 *            the  event name
	 * @param start_date
	 * 			  the event start date
	 * @param start_time
	 *			  the event start time
	 * @param end_date
	 * 			  the event end date
	 * @param end_time
	 * 			  the event end time
	 * @param location_name
	 * 			  the location name
	 * @param location_address
	 * 			  the location address
	 * @param location_city
	 *            the location city
	 * @param location_state
	 *            the location state
	 * @param location_zip
	 * 			  the location zip code
	 * @return  the status
	 *
	 *
	 * Log.d("LOCATION NAME", location_name);
	Log.d("LOCATION ADDRESS", location_address);
	Log.d("LOCATION CITY", location_city);
	Log.d("LOCATION ZIP", location_zip);
	Log.d("LOCATION STATE", location_state);
	Log.d("CATEGORY", category);
	Log.d("USER ID", user);
	Log.d("EVENT NAME", event_name);
	Log.d("EVENT INFO", event_info);
	Log.d("EVENT START TIME", start_time);
	Log.d("EVENT END TIME", end_time);
	Log.d("EVENT START DATE", start_date);
	Log.d("EVENT END DATE", end_date);
	 * param.add(new BasicNameValuePair("locationname", location_name));
	param.add(new BasicNameValuePair("address", location_address));
	param.add(new BasicNameValuePair("city", location_city));
	param.add(new BasicNameValuePair("zipcode", location_zip));
	param.add(new BasicNameValuePair("state", location_state));
	param.add(new BasicNameValuePair("category", category));
	param.add(new BasicNameValuePair("user", user));
	param.add(new BasicNameValuePair("eventName", event_name));
	param.add(new BasicNameValuePair("eventInfo", event_info));
	param.add(new BasicNameValuePair("startTime", start_time));
	param.add(new BasicNameValuePair("endTime", end_time));
	param.add(new BasicNameValuePair("startDate", start_date));
	param.add(new BasicNameValuePair("endDate", end_date));


	String location_name, String location_address, String location_city, String location_zip, String location_state, String category, String user, String event_name, String event_info, String start_time, String end_time, String start_date, String end_date

	public static Status doCreateEvent(String location_name, String location_address, String location_city, String location_zip, String location_state, String category, String user, String event_name, String event_info, String start_time, String end_time, String start_date, String end_date, String image)
	{
	try
	{
	ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
	param.add(new BasicNameValuePair("location_name", location_name));
	param.add(new BasicNameValuePair("location_address", location_address));
	param.add(new BasicNameValuePair("location_city", location_city));
	param.add(new BasicNameValuePair("location_zip", location_zip));
	param.add(new BasicNameValuePair("location_state", location_state));
	param.add(new BasicNameValuePair("category", category));
	param.add(new BasicNameValuePair("user", user));
	param.add(new BasicNameValuePair("event_name", event_name));
	param.add(new BasicNameValuePair("event_info", event_info));
	param.add(new BasicNameValuePair("start_time", start_time));
	param.add(new BasicNameValuePair("end_time", end_time));
	param.add(new BasicNameValuePair("start_date", start_date));
	param.add(new BasicNameValuePair("end_date", end_date));
	param.add(new BasicNameValuePair("image", image));

	String res = executePostRequest(CREATE_EVENT_URL, param, false);
	return new Status(res, "event_id");
	} catch (Exception ex)
	{
	ex.printStackTrace();
	}
	return new Status();
	}

	 */

	public static Status doCreateEvent(String location_name, String location_address, String location_city, String location_zip, String location_state, String category, String user, String event_name, String event_info, String start_time, String end_time, String start_date, String end_date, String image)
	{
		try
		{
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("location_name", location_name));
			param.add(new BasicNameValuePair("location_address", location_address));
			param.add(new BasicNameValuePair("location_city", location_city));
			param.add(new BasicNameValuePair("location_zip", location_zip));
			param.add(new BasicNameValuePair("location_state", location_state));
			param.add(new BasicNameValuePair("category", category));
			param.add(new BasicNameValuePair("user", user));
			param.add(new BasicNameValuePair("event_name", event_name));
			param.add(new BasicNameValuePair("event_info", event_info));
			param.add(new BasicNameValuePair("start_time", start_time));
			param.add(new BasicNameValuePair("end_time", end_time));
			param.add(new BasicNameValuePair("start_date", start_date));
			param.add(new BasicNameValuePair("end_date", end_date));
			param.add(new BasicNameValuePair("image", image));

			String res = executePostRequest(CREATE_EVENT_URL, param, false);
			return new Status(res, "event_id");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return new Status();
	}

/*
	public static Status doCreateEvent(String image)
	{
		try
		{
			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("location_name", "Basement Office"));
			param.add(new BasicNameValuePair("location_address", "1200 South Cove Lane"));
			param.add(new BasicNameValuePair("location_city", "Birmingham"));
			param.add(new BasicNameValuePair("location_zip", "35216"));
			param.add(new BasicNameValuePair("location_state", "AL"));
			param.add(new BasicNameValuePair("category", "Soccer"));
			param.add(new BasicNameValuePair("user", "24"));
			param.add(new BasicNameValuePair("event_name", "User Id Test"));
			param.add(new BasicNameValuePair("event_info", "Category Testing Event"));
			param.add(new BasicNameValuePair("start_time", "18:00:00"));
			param.add(new BasicNameValuePair("end_time", "19:00:00"));
			param.add(new BasicNameValuePair("start_date", "2017-08-03"));
			param.add(new BasicNameValuePair("end_date", "2017-08-03"));
			param.add(new BasicNameValuePair("image", image));

			String res = executePostRequest(CREATE_EVENT_URL, param, false);
			return new Status(res, "event_id");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return new Status();
	}
	*/

    /**
	 *
	 * Do register.
	 * 
	 * @param name
	 *            the  name
	 * @param login_name
	 * 			  the username
	 * @param user_login
	 * @param email
	 *            the email
	 * @param pwd
	 *            the password
	 * @return the status
	 */
	public static Status doRegister(String name, String user_login, String login_name, String email, String pwd)
	{
		try
		{
			ArrayList<NameValuePair> param = new ArrayList<>();
			param.add(new BasicNameValuePair("name", name));
			param.add(new BasicNameValuePair("user_login", user_login));
			param.add(new BasicNameValuePair("login_name", login_name));
			param.add(new BasicNameValuePair("user_email", email));
			param.add(new BasicNameValuePair("pwd", pwd));
			String res = executePostRequest(REGISTER_URL, param, false);
			return new Status(res, "user_id");

		} catch (Exception ex) {


			ex.printStackTrace();
		}

		return new Status();
	}

	/**
	 * Book tkt.
	 * 
	 * @param event_id
	 *            the event_id
	 * @param space
	 *            the space
	 * @param comment
	 *            the comment
	 * @return Status
	 */
	public static Status bookTkt(String event_id, String space, String comment)
	{
		try
		{
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("event_id", event_id));
			//param.add(new BasicNameValuePair("booking_spaces", space));
			//param.add(new BasicNameValuePair("booking_comment", comment));
			param.add(new BasicNameValuePair("booking_spaces", "1"));
			param.add(new BasicNameValuePair("booking_comment", null));
			String res = executePostRequest(BOOK_TKT_URL, param, false);
			return new Status(res, "payment_page_link");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return new Status();
	}

	/**
	 * Gets the booked adrenalinelife.
	 * 
	 * @param page
	 *            the page
	 * @param pageSize
	 *            the page size
	 * @return the booked adrenalinelife
	 */
	public static ArrayList<Event> getBookedEvents(int page, int pageSize)
	{
		try
		{
			page++;
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("page", page + ""));
			param.add(new BasicNameValuePair("page_size", pageSize + ""));
			String res = executePostRequest(TKT_LIST_URL, param, true);
			return parseEvents(res);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
